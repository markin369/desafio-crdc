package br.com.marcosalexandre.desafiobackendcrdc.config;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableWebMvc
public class OpenApiSwaggerConfig {

    @Bean
    public OpenAPI customApiSwagger() {
        return new OpenAPI()
                .info(new Info().title("Gerenciamento da importação de dados CNABs")
                        .contact(new Contact().name("Marcos Alexandre").email("marcos.alexandre34@gmail.com"))
                        .description("Aplicação de back-end desenvolvida para o controle da importação e visualização de dados de arquivos CNABs")
                        .version("v0.0.1"));
    }

}

