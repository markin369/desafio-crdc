package br.com.marcosalexandre.desafiobackendcrdc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DesafioBackendCrdcApplication {

	private static final Logger logger = LogManager.getLogger(DesafioBackendCrdcApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DesafioBackendCrdcApplication.class, args);
		logger.info("DesafioBackendCRDC iniciado com sucesso.");
	}

}