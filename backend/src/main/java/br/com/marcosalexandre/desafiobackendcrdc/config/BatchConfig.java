package br.com.marcosalexandre.desafiobackendcrdc.config;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.marcosalexandre.desafiobackendcrdc.api.ErrorDetails;
import br.com.marcosalexandre.desafiobackendcrdc.entity.RegistroCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.entity.TipoTransacao;
import br.com.marcosalexandre.desafiobackendcrdc.entity.Transacao;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ValidacaoException;
import br.com.marcosalexandre.desafiobackendcrdc.service.CnabValidationService;
import br.com.marcosalexandre.desafiobackendcrdc.utils.RegistroFieldSetMapperCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.utils.RegistroLineTokenizerCNAB;

@Configuration
public class BatchConfig {
  private final PlatformTransactionManager transactionManager;
  private final JobRepository jobRepository;
  private RegistroCNAB registroCNAB = new RegistroCNAB();
  
  @Autowired
  private CnabValidationService cnabValidationService;

  private static final Logger logger = LogManager.getLogger(BatchConfig.class);

  public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
    this.transactionManager = transactionManager;
    this.jobRepository = jobRepository;
  }

  @Bean
  Job job(Step step, JobExecutionListener customJobListener) {
    logger.info("Configurando e construindo o Job.");
    return new JobBuilder("job", jobRepository)
        .start(step)
        .listener(customJobListener)
        .build();
  }

  @Bean
  Step step(
      FlatFileItemReader<RegistroCNAB> reader,
      ItemProcessor<RegistroCNAB, List<Transacao>> processor,
      ItemWriter<List<Transacao>> writer) {
      logger.info("Configurando o step do job.");
    return new StepBuilder("step", jobRepository)
        .<RegistroCNAB, List<Transacao>>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @StepScope
  @Bean
  FlatFileItemReader<RegistroCNAB> reader(
      @Value("#{jobParameters['cnabFile']}") Resource resource) {
        logger.info("Configurando e construindo o FlatFileItemReader.");
        Map<String, Range[]> columnRanges = new HashMap<>();
        // Defina os intervalos das colunas para cada tipo de registro
        columnRanges.put("001", new Range[]{new Range(1, 3), new Range(4, 33), new Range(34, 47), new Range(48, 80)});
        columnRanges.put("002", new Range[]{new Range(1, 3), new Range(4, 4), new Range(5, 20), new Range(21, 34), new Range(35, 52)});
        columnRanges.put("003", new Range[]{new Range(1, 3), new Range(4, 80)});
    
    return new FlatFileItemReaderBuilder<RegistroCNAB>()
        .name("reader")
        .resource(resource)
        .lineMapper(new DefaultLineMapper<RegistroCNAB>() {{
            setLineTokenizer(new RegistroLineTokenizerCNAB(columnRanges));
            setFieldSetMapper(new RegistroFieldSetMapperCNAB(registroCNAB.getLine(), cnabValidationService));
        }})
        .targetType(RegistroCNAB.class)
        .build();
  }

  @Bean
  ItemProcessor<RegistroCNAB, List<Transacao>> processor() {
    logger.info("Configurando e construindo o ItemProcessor.");
    return item -> {
      logger.info("Iniciando processamento do item: {}", item);
      
      completeRegistroCNAB(item);
      if(registroCNAB.isFull()){
        //transacao esta autorizada?
        validarRegistroCNAB();
        List<Transacao> transacoes = new ArrayList<>();
        for(var transacaoCNAB : registroCNAB.getAllTransacoes()){
          var tipoTransacao = TipoTransacao.findByTipoTransacao(transacaoCNAB.getTipoTransacao());
          var valorNormalizado = transacaoCNAB.getValorTransacao()
              .divide(new BigDecimal(100))
              .multiply(tipoTransacao.getSinal());

          var transacao = new Transacao(
              null, registroCNAB.getCabecalho().getRazaoSocial(), registroCNAB.getCabecalho().getIdentificadorEmpresa(), registroCNAB.getCabecalho().getReservadoCabecalho(),
              transacaoCNAB.getTipoTransacao(), valorNormalizado, transacaoCNAB.getContaOrigem(),
              transacaoCNAB.getContaDestino(), registroCNAB.getRodape().getReservadoRodape());

          transacoes.add(transacao);
        }

        return transacoes;
      }
      return null;
    };
  }


  @Bean
  public JdbcBatchItemWriter<List<Transacao>> writer(DataSource dataSource) {
    logger.info("Configurando e construindo o JdbcBatchItemWriter.");
    return new JdbcBatchItemWriterBuilder<List<Transacao>>()
        .dataSource(dataSource)
        .sql("INSERT INTO transacao (razao_social, identificador_empresa, reservado_cabecalho, tipo_transacao, valor_transacao, conta_origem, conta_destino, reservado_rodape) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
        .itemPreparedStatementSetter(new TransacaoItemPreparedStatementSetter())
        .build();
  }

  private class TransacaoItemPreparedStatementSetter implements ItemPreparedStatementSetter<List<Transacao>> {
    @Override
    public void setValues(List<Transacao> transacoes, PreparedStatement ps) throws SQLException {
      logger.info("Configurando PreparedStatement para escrita no banco de dados.");
        for (Transacao transacao : transacoes) {
            ps.setString(1, transacao.razaoSocial());
            ps.setString(2, transacao.identificadorEmpresa());
            ps.setString(3, transacao.reservadoCabecalho());
            ps.setString(4, transacao.tipoTransacao());
            ps.setBigDecimal(5, transacao.valorTransacao());
            ps.setLong(6, transacao.contaOrigem());
            ps.setLong(7, transacao.contaDestino());
            ps.setString(8, transacao.reservadoRodape());

            ps.addBatch();
        }
         
        //limpando registroCNAB
        limparRegistroCNAB();  
    }
  }

  private void validarRegistroCNAB(){
    logger.info("Validando o Registro CNAB.");
    List<ErrorDetails> erros = new ArrayList<>();
    erros = registroCNAB.getErrors();
    if(!erros.isEmpty()){
      limparRegistroCNAB();
      throw new ValidacaoException("O arquivo CNAB possui formato inválido.", erros);
    }
  }

  private void limparRegistroCNAB(){
    logger.info("Limpando o Registro CNAB.");
    registroCNAB = new RegistroCNAB();
  }
   
  private void completeRegistroCNAB(RegistroCNAB item){
    logger.info("Preenchendo o Registro CNAB com suas propriedades de cada linha: {}", item);
    registroCNAB.setLine(item.getLine());
    registroCNAB.setErrors(item.getErrors());
    if(item.getCabecalho()!=null)
        registroCNAB.setCabecalho(item.getCabecalho());
      
      if(item.getAllTransacoes().size() > 0)
        registroCNAB.setTransacoes(item.getAllTransacoes());

      if(item.getRodape()!=null) 
        registroCNAB.setRodape(item.getRodape()); 
  }
  
}
