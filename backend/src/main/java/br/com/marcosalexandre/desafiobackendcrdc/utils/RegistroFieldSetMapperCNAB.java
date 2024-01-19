package br.com.marcosalexandre.desafiobackendcrdc.utils;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import br.com.marcosalexandre.desafiobackendcrdc.api.ErrorDetails;
import br.com.marcosalexandre.desafiobackendcrdc.entity.CabecalhoCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.entity.RegistroCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.entity.RodapeCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.entity.TransacaoCNAB;
import br.com.marcosalexandre.desafiobackendcrdc.service.CnabValidationService;

@ComponentScan(basePackages = "br.com.marcosalexandre.desafiobackendcrdc")
public class RegistroFieldSetMapperCNAB implements FieldSetMapper<RegistroCNAB> {

    private static final Logger logger = LogManager.getLogger(RegistroFieldSetMapperCNAB.class);

    private final String REGISTRO_CABECALHO = "001";
    private final String REGISTRO_TRANSACAO = "002";
    private final String REGISTRO_RODAPE = "003";
    private Integer line;

    @Autowired
    private CnabValidationService cnabValidationService;

    public RegistroFieldSetMapperCNAB(Integer line, CnabValidationService cnabValidationService) {
        this.line = line;
        this.cnabValidationService = cnabValidationService;
    }

    @Override
    public RegistroCNAB mapFieldSet(FieldSet fieldSet) {
        logger.info("Iniciando mapeamento por partes do Registro CNAB. Linha #{}", line);
        RegistroCNAB registroCNAB = new RegistroCNAB();
        registroCNAB.setTipoRegistro(fieldSet.readString(0));
        List<ErrorDetails> errors;

        switch (registroCNAB.getTipoRegistro()) {
            case REGISTRO_CABECALHO:
                errors = cnabValidationService.validateCabecalhoCNAB(fieldSet, line);
                if (errors != null) 
                    registroCNAB.setErrors(errors);
                CabecalhoCNAB cabecalho = new CabecalhoCNAB();
                cabecalho.setRazaoSocial(fieldSet.readString(1));
                cabecalho.setIdentificadorEmpresa(fieldSet.readString(2));
                cabecalho.setReservadoCabecalho(fieldSet.readString(3));
                registroCNAB.setCabecalho(cabecalho);
                break;
        
            case REGISTRO_TRANSACAO:
                errors = cnabValidationService.validateTransacaoCNAB(fieldSet, line);
                if (errors != null) 
                    registroCNAB.setErrors(errors);
            
                TransacaoCNAB transacao = new TransacaoCNAB();
                transacao.setTipoTransacao(fieldSet.readString(1));
                transacao.setValorTransacao(new BigDecimal(fieldSet.readString(2)));
                transacao.setContaOrigem(Long.parseLong(fieldSet.readString(3)));
                transacao.setContaDestino(Long.parseLong(fieldSet.readString(4)));
                registroCNAB.setTransacao(transacao);
                break;
        
            case REGISTRO_RODAPE:
                RodapeCNAB rodape = new RodapeCNAB();
                rodape.setReservadoRodape(fieldSet.readString(1));
                registroCNAB.setRodape(rodape);
                break;
        
            default:
                throw new IllegalArgumentException("Tipo de registro desconhecido: " + registroCNAB.getTipoRegistro());
        }
        logger.info("Finalizando parte do mapeamento do Registro CNAB. Linha #{}", line);
        registroCNAB.setLine(line++);
        return registroCNAB;
        
    }
}
