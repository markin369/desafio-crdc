package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;

public enum TipoTransacao {
  CREDITO("C"), DEBITO("D"), TRANSFERENCIA("T");

  private String tipo;

  private TipoTransacao(String tipo) {
    this.tipo = tipo;
  }

  // Similar to Strategy Pattern
  public BigDecimal getSinal() {
    return switch (tipo) {
      case "C" -> new BigDecimal(1);
      case "D", "T" -> new BigDecimal(-1);
      default -> new BigDecimal(0);
    };
  }

  public static TipoTransacao findByTipoTransacao(String tipo) {
    for (TipoTransacao tipoTransacao : values()) {
      if (tipoTransacao.tipo.equals(tipo)) {
        return tipoTransacao;
      }
    }
    throw new IllegalArgumentException("Invalid tipo: " + tipo.toString());
  }

   public static String findByTipoTransacaoToString(String tipo) {
    for (TipoTransacao tipoTransacao : values()) {
      if (tipoTransacao.tipo.equals(tipo)) {
        return tipoTransacao.name();
      }
    }
    throw new IllegalArgumentException("Tipo de transação inválido. " + tipo.toString());
  }
}
