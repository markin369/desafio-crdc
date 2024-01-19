package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;
import java.util.Objects;

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

  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id) &&
                Objects.equals(razaoSocial, transacao.razaoSocial) &&
                Objects.equals(identificadorEmpresa, transacao.identificadorEmpresa) &&
                Objects.equals(reservadoCabecalho, transacao.reservadoCabecalho) &&
                tipoTransacaoEquals(tipoTransacao, transacao.tipoTransacao) &&
                Objects.equals(valorTransacao, transacao.valorTransacao) &&
                Objects.equals(contaOrigem, transacao.contaOrigem) &&
                Objects.equals(contaDestino, transacao.contaDestino) &&
                Objects.equals(reservadoRodape, transacao.reservadoRodape);
    }

    private boolean tipoTransacaoEquals(String tipo1, String tipo2) {
        return tipo1.substring(0,1).equals(tipo2.substring(0,1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, razaoSocial, identificadorEmpresa, reservadoCabecalho, tipoTransacao, valorTransacao, contaOrigem, contaDestino, reservadoRodape);
    }

}
