package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Transacao(
    @Id
    Long id,
    @Column("razao_social") 
    String razaoSocial,
    @Column("identificador_empresa") 
    String identificadorEmpresa,
    @Column("reservado_cabecalho") 
    String reservadoCabecalho,
    @Column("tipo_transacao") 
    String tipoTransacao,
    @Column("valor_transacao") 
    BigDecimal valorTransacao,
    @Column("conta_origem") 
    Long contaOrigem,
    @Column("conta_destino") 
    Long contaDestino,
    @Column("reservado_rodape") 
    String reservadoRodape) {

      
  // Wither Pattern
  public Transacao withValorTransacao(BigDecimal valorTransacao) {
    return new Transacao(
        id, razaoSocial, identificadorEmpresa, reservadoCabecalho, tipoTransacao, valorTransacao, 
        contaOrigem, contaDestino, reservadoRodape );
  }

  public Transacao addTipoTransacao(String tipoTransacao) {
     return new Transacao(
        id, razaoSocial, identificadorEmpresa, reservadoCabecalho, tipoTransacao, valorTransacao, 
        contaOrigem, contaDestino, reservadoRodape );
  }

}
