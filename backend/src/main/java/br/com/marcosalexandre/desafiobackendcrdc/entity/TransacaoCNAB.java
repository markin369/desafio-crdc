package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.math.BigDecimal;

public class TransacaoCNAB extends RegistroCNAB {
    // Atributos específicos para o registro de transação
    String tipoTransacao;
    BigDecimal valorTransacao;
    Long contaOrigem;
    Long contaDestino;

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public BigDecimal getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(BigDecimal valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public Long getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Long contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Long getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Long contaDestino) {
        this.contaDestino = contaDestino;
    }

}