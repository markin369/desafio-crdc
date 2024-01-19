package br.com.marcosalexandre.desafiobackendcrdc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.marcosalexandre.desafiobackendcrdc.entity.MyMultipartRequest;
import br.com.marcosalexandre.desafiobackendcrdc.service.CnabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("cnab")
@Tag(name = "01. Endpoint para importação e validação do arquivo CNAB")
public class CnabController {
  private final CnabService cnabService;
  private static final Logger logger = LogManager.getLogger(CnabController.class);

  public CnabController(CnabService cnabService) {
    this.cnabService = cnabService;
  }

  @Operation(summary = "Realiza upload do arquivo CNAB.")
  @CrossOrigin(origins = { "http://localhost:9090", "https://frontend-crdc.onrender.com" })
  @PostMapping("upload")
  @RequestBody(content = @Content(
		mediaType = "multipart/form-data",
		schema = @Schema(implementation = MyMultipartRequest.class)), required = true)
  public ResponseEntity<Object> upload (MyMultipartRequest file) throws Exception {
    logger.info("Recebendo solicitação para upload do arquivo CNAB.");
    return cnabService.uploadCnabFile(file);
  }
}
