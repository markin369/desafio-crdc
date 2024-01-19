package br.com.marcosalexandre.desafiobackendcrdc.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.marcosalexandre.desafiobackendcrdc.entity.TransacaoReport;
import br.com.marcosalexandre.desafiobackendcrdc.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("transacoes")
@Tag(name = "02. Relatório dos dados aprovados pela importação dos arquivos CNABs")
public class TransacaoController {
  private final TransacaoService transacaoService;
  private static final Logger logger = LogManager.getLogger(CnabController.class);

  public TransacaoController(TransacaoService transacaoService) {
    this.transacaoService = transacaoService;
  }


  @CrossOrigin(origins = { "http://localhost:9090", "https://frontend-desafio.onrender.com" })
  @GetMapping
  @Operation(summary = "Listar informações autorizadas dos arquivos CNABs importados.")
  public List<TransacaoReport> listAll() {
    logger.info("Recebida requisição para listar todas as transações.");
    try {
      List<TransacaoReport> transacoes = transacaoService.getTotaisTransacoesByRazaoSocial();
      logger.info("Listagem de transações concluída com sucesso. Total de empresas listadas: {}", transacoes.size());
      return transacoes;
    } catch (Exception e) {
      logger.error("Erro ao processar a requisição para listar transações.", e);
      throw e; // Propaga a exceção para o tratamento global de exceções
    }
  }
}
