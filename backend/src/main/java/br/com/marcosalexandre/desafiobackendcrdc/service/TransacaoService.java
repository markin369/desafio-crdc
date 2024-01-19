package br.com.marcosalexandre.desafiobackendcrdc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.marcosalexandre.desafiobackendcrdc.entity.TipoTransacao;
import br.com.marcosalexandre.desafiobackendcrdc.entity.Transacao;
import br.com.marcosalexandre.desafiobackendcrdc.entity.TransacaoReport;
import br.com.marcosalexandre.desafiobackendcrdc.repository.TransacaoRepository;

@Service
public class TransacaoService {
  private final TransacaoRepository repository;
  
  public TransacaoService(TransacaoRepository repository) {
    this.repository = repository;
  }

  public List<TransacaoReport> getTotaisTransacoesByRazaoSocial() {
    List<Transacao> transacoes = repository.findAllByOrderByRazaoSocialAscIdDesc();

    // preserves order
    Map<String, TransacaoReport> reportMap = new LinkedHashMap<>();

    transacoes.forEach(transacao -> {
      var razaoSocial = transacao.razaoSocial();
      var valor = transacao.valorTransacao();
      var tipoTransacao = TipoTransacao.findByTipoTransacaoToString(transacao.tipoTransacao());

      reportMap.compute(razaoSocial, (key, existingReport) -> {
        TransacaoReport report = (existingReport != null) ? existingReport
            : new TransacaoReport(BigDecimal.ZERO, key, new ArrayList<>());
        return report.addTotal(valor).addTransacao(transacao.addTipoTransacao(tipoTransacao));
      });
    });

    return new ArrayList<>(reportMap.values());
  }
}
