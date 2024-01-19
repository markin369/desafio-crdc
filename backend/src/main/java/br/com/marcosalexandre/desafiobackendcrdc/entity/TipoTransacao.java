package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;
import java.util.Arrays;

public enum TipoTransacao {
  CREDITO("C"), DEBITO("D"), TRANSFERENCIA("T");

  private String tipo;

  private TipoTransacao(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  } 

  public BigDecimal getSinal() {
    return switch (tipo) {
      case "C" -> new BigDecimal(1);
      case "D", "T" -> new BigDecimal(-1);
      default -> BigDecimal.ZERO;
    };
  }

  public static TipoTransacao findByTipoTransacao(String tipo) {
    return Arrays.stream(values())
                 .filter(transacao -> transacao.tipo.equals(tipo))
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException("Invalid tipo: " + tipo));
  }

  public static String findByTipoTransacaoToString(String tipo) {
    return Arrays.stream(values())
                 .filter(transacao -> transacao.tipo.equals(tipo))
                 .findFirst()
                 .map(Enum::name)
                 .orElseThrow(() -> new IllegalArgumentException("Tipo de transação inválido: " + tipo));
  }
  
}
