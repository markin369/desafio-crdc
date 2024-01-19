package br.com.marcosalexandre.desafiobackendcrdc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.marcosalexandre.desafiobackendcrdc.api.ApiResponse;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ArquivoInvalidoException;
import br.com.marcosalexandre.desafiobackendcrdc.exception.ValidacaoException;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger logger = LogManager.getLogger(CnabController.class);
    
    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> handleFileAlreadyImported(
      JobInstanceAlreadyCompleteException exception) {
        logger.error("JobInstanceAlreadyCompleteException: O arquivo informado já foi importado no sistema!", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage("O arquivo informado já foi importado no sistema!");
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(response);
    }

   @ExceptionHandler(JobExecutionAlreadyRunningException.class)
    private ResponseEntity<Object> handleFileAlreadyImported(
      JobExecutionAlreadyRunningException exception) {
        logger.error("JobExecutionAlreadyRunningException: O arquivo informado já foi importado no sistema ou ainda está em execução!", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage("O arquivo informado já foi importado no sistema ou ainda esta em execução!");
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(response);
    }

    @ExceptionHandler(ValidacaoException.class)
    private ResponseEntity<Object> validacaoException(
      ValidacaoException exception) {
        logger.error("ValidacaoException: O arquivo CNAB possui formato inválido.", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage("O arquivo CNAB possui formato inválido.");
        response.setErrors(exception.getErrors());;
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(response);
    }
  
    @ExceptionHandler(ArquivoInvalidoException.class)
    private ResponseEntity<Object> arquivoInvalidoException(
      ArquivoInvalidoException exception) {
        logger.error("ArquivoInvalidoException: Erro ao processar o arquivo CNAB. Certifique-se de que o arquivo esteja no formato posicional correto.", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<Object> nullPointerException(
        NullPointerException exception) {
        logger.error("NullPointerException: Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage("Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> exception(
        Exception exception) {
        logger.error("Exception: Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.", exception);
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage("Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
    }
}
