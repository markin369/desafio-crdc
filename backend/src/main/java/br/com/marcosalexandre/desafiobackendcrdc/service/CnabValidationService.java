package br.com.marcosalexandre.desafiobackendcrdc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.marcosalexandre.desafiobackendcrdc.api.ErrorDetails;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ArquivoInvalidoException;

@Service
public class CnabValidationService {

    private static final Logger logger = LogManager.getLogger(CnabValidationService.class);

    public void validateCnabFile(MultipartFile file) {
        List<ErrorDetails> errors = new ArrayList<>();

        // Verificando se o arquivo não está vazio
        if (file == null || file.isEmpty()) { 
            errors.add(new ErrorDetails(0, "Arquivo CNAB está vazio."));
        }

        if (!errors.isEmpty()) {
            logger.error("Erro ao validar arquivo CNAB: {}", errors);
            throw new ArquivoInvalidoException("Erro ao processar o arquivo CNAB. Certifique-se de que o arquivo esteja no formato posicional correto.");
        }
    }

    
    public List<ErrorDetails> validateCabecalhoCNAB(FieldSet cabecalhoFieldSet, Integer line) {
        logger.info("Validando registros do cabecalho do arquivo CNAB.");
        List<ErrorDetails> errors = new ArrayList<>();

        String razaoSocial = cabecalhoFieldSet.readString(1);
        String identificadorEmpresa =  cabecalhoFieldSet.readString(2);
        
        if (isApenasNumeros(razaoSocial))
            errors.add(new ErrorDetails(line, "Razão social não está no formato correto."));

        if (!isApenasNumeros(identificadorEmpresa))
            errors.add(new ErrorDetails(line, "Identificador da empresa não está no formato correto."));  

        if (!errors.isEmpty()) {
            logger.error("Erros ao validar cabeçalho CNAB: {}", errors);
            return errors;
        } 
        logger.info("Finalizando validacao do cabecalho do arquivo CNAB.");
        return null;

    } 

    public List<ErrorDetails> validateTransacaoCNAB(FieldSet transacaoFieldSet, Integer line) {
        logger.info("Validando registros de transacao do arquivo CNAB.");
        List<ErrorDetails> errors = new ArrayList<>();
    
        validateCampo("Tipo de transação", transacaoFieldSet.readString(1), "[CDT]", line, errors);
        validateNumerico("Valor da transação", transacaoFieldSet.readString(2), line, errors, true, "Valor da transação não pode ser nulo");
        validateNumerico("Conta origem", transacaoFieldSet.readString(3), line, errors, true, "Conta origem é obrigatória");
        validateNumerico("Conta destino", transacaoFieldSet.readString(4), line, errors, true, "Conta destino é obrigatória");
    
        if (!errors.isEmpty()) {
            logger.error("Erros ao validar transação CNAB: {}", errors);
            return errors;
        }
        logger.info("Finalizando validacao das transacoes do arquivo CNAB.");
        return null;
    }
    
    private void validateCampo(String nomeCampo, String valor, String regex, Integer line, List<ErrorDetails> errors) {
        if (valor.isBlank() || valor.isEmpty() || !valor.matches(regex)) {
            errors.add(new ErrorDetails(line, nomeCampo + " inválido."));
        }
    }
    
    private void validateNumerico(String nomeCampo, String valor, Integer line, List<ErrorDetails> errors, boolean obrigatorio, String mensagem) {
        validateCampo(nomeCampo, valor.trim(), "\\d+", line, errors);
    
        if (obrigatorio) {
            BigDecimal decimal = new BigDecimal(valor);
            if (decimal.compareTo(BigDecimal.ZERO) == 0) {
                errors.add(new ErrorDetails(line, mensagem));
            }
        }
    }  

    private boolean isApenasNumeros(String value) {
        // Aqui, estou usando uma expressão regular para garantir que o valor contenha apenas dígitos
        return Pattern.matches("\\d+", value.trim());
    }

}

