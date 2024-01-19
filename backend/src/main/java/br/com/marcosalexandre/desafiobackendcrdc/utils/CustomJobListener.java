package br.com.marcosalexandre.desafiobackendcrdc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.marcosalexandre.desafiobackendcrdc.exception.ExceptionHolder;

@Component
public class CustomJobListener implements JobExecutionListener {

    private final ExceptionHolder exceptionHolder;

    private static final Logger logger = LogManager.getLogger(CustomJobListener.class);

    @Autowired
    public CustomJobListener(ExceptionHolder exceptionHolder) {
        this.exceptionHolder = exceptionHolder;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Antes da execução do job
        logger.info("Iniciando execucao do job.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // Após a execução do job
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job concluido com sucesso.");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            logger.error("O job falhou durante a execução. Status: FAILED");
            // Lógica de tratamento de erro
            Throwable throwable = jobExecution.getAllFailureExceptions().get(0);
            // Tratar a exceção ou registrar logs
            exceptionHolder.setException(throwable);
            logger.error("Exceção durante a execução do job:", throwable);
        }
    }
}

