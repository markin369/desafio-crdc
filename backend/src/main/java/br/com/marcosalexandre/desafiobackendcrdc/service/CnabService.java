package br.com.marcosalexandre.desafiobackendcrdc.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.marcosalexandre.desafiobackendcrdc.api.ApiResponse;
import br.com.marcosalexandre.desafiobackendcrdc.entity.MyMultipartRequest;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ArquivoInvalidoException;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ExceptionHolder;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ValidacaoException;

@Service
public class CnabService {
  private final JobLauncher jobLauncher;
  private final Job job;
  private final Path fileStorageLocation;
  private CnabValidationService cnabValidationService;
  private ExceptionHolder exceptionHolder;
  private TransacaoService transacaoService;
  private static final Logger logger = LogManager.getLogger(CnabService.class);

  public CnabService(
      JobLauncher jobLauncher,
      Job job,
      CnabValidationService cnabValidationService,
      TransacaoService transacaoService,
      ExceptionHolder exceptionHolder,
      @Value("${file.upload-dir}") String fileUploadDir) {
    this.jobLauncher = jobLauncher;
    this.job = job;
    this.cnabValidationService = cnabValidationService; 
    this.transacaoService = transacaoService;
    this.exceptionHolder = exceptionHolder;
    this.fileStorageLocation = Paths.get(fileUploadDir);
  }

  public ResponseEntity<Object> uploadCnabFile(MyMultipartRequest cnabFile) throws Exception {
    logger.info("Iniciando o processo de upload do arquivo CNAB."); 

    MultipartFile file = cnabFile.getFile();

    if (!Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".txt")) {
      throw new ArquivoInvalidoException("Erro ao processar o arquivo CNAB. Certifique-se de que o arquivo esteja no formato correto(.txt).");
    }
    
    // Inicializa um objeto para a resposta padronizada
    ApiResponse response = new ApiResponse();
    cnabValidationService.validateCnabFile(file);

    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    // Certificando que o diretório de destino exista
    Files.createDirectories(fileStorageLocation);
    Path targetLocation = fileStorageLocation.resolve(fileName);
    file.transferTo(targetLocation);

    var jobParameters = new JobParametersBuilder()
      .addJobParameter("cnab", file.getOriginalFilename(), String.class, true)
      .addJobParameter("cnabFile", "file:" + targetLocation.toString(), String.class, false)
      .toJobParameters();

        
    JobExecution execucao = jobLauncher.run(job, jobParameters);

    Throwable exception = new Throwable();
    exception = exceptionHolder.getException();
    exceptionHolder.limparException();
    
    if(exception!=null){
      if (exception instanceof ValidacaoException) {
        ValidacaoException validacaoException = (ValidacaoException) exception;
        throw new ValidacaoException(exception.getMessage(), validacaoException.getErrors());
      }
    }

    // Atualiza a resposta para sucesso
    if(!execucao.getStatus().equals(BatchStatus.COMPLETED)){
      logger.error("Erro interno no job que processa o arquivo CNAB. Status: {}", execucao.getStatus());
      response.setStatus("error");
      response.setMessage("Ocorreu um erro interno no job que processa o arquivo. Por favor, tente novamente mais tarde.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    logger.info("Arquivo CNAB processado com sucesso.");
    response.setStatus("success");
    response.setMessage("Arquivo CNAB processado com sucesso.");
    response.setData(transacaoService.getTotaisTransacoesByRazaoSocial());
    return ResponseEntity.ok(response);
  }
}
