package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(
    BigDecimal total,
    String razaoSocial,
    List<Transacao> transacoes) {

  public TransacaoReport addTotal(BigDecimal valor) {
    return new TransacaoReport(this.total().add(valor), this.razaoSocial, this.transacoes);
  }

  public TransacaoReport addTransacao(Transacao transacao) {
    var transacoes = this.transacoes();
    transacoes.add(transacao);
    return new TransacaoReport(this.total(), this.razaoSocial, transacoes);
  }

  public TransacaoReport withRazaoSocial(String razaoSocial) {
    return new TransacaoReport(this.total(), razaoSocial, this.transacoes);
  }
}
