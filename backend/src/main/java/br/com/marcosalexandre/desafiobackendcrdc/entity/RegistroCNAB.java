package br.com.marcosalexandre.desafiobackendcrdc.entity;

import java.util.ArrayList;
import java.util.List;

import br.com.marcosalexandre.desafiobackendcrdc.api.ErrorDetails;

public class RegistroCNAB {
    private String tipoRegistro;
    private CabecalhoCNAB cabecalho;
    private List<TransacaoCNAB> transacoes = new ArrayList<TransacaoCNAB>();
    private RodapeCNAB rodape;
    private Integer line = 1;
    private List<ErrorDetails> errors = new ArrayList<>();

    public List<ErrorDetails> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorDetails> errors) {
        this.errors.addAll(errors);
    }
    public CabecalhoCNAB getCabecalho() {
        return cabecalho;
    }
    public void setCabecalho(CabecalhoCNAB cabecalho) {
        this.cabecalho = cabecalho;
    }
    public List<TransacaoCNAB> getAllTransacoes() {
        return transacoes;
    }

    public void setTransacao(TransacaoCNAB transacao) {
        this.transacoes.add(transacao);
    }

    public void setTransacoes(List<TransacaoCNAB> transacoes) {
        this.transacoes.addAll(transacoes);
    }

    public RodapeCNAB getRodape() {
        return rodape;
    }
    public void setRodape(RodapeCNAB rodape) {
        this.rodape = rodape;
    }
    public String getTipoRegistro() {
        return tipoRegistro;
    }
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public boolean isFull(){
        return cabecalho!=null && !transacoes.isEmpty() && rodape!=null;
    }
    public void setLine(Integer line) {
        this.line = line;
    }
    public Integer getLine(){
        return line;
    }
    
}
