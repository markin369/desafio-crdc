package br.com.marcosalexandre.desafiobackendcrdc.entity;

public class CabecalhoCNAB extends RegistroCNAB {
    // Atributos específicos para o registro de cabeçalho

    String razaoSocial;
    String identificadorEmpresa;
    String reservadoCabecalho;

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getIdentificadorEmpresa() {
        return identificadorEmpresa;
    }

    public void setIdentificadorEmpresa(String identificadorEmpresa) {
        this.identificadorEmpresa = identificadorEmpresa;
    }

    public String getReservadoCabecalho() {
        return reservadoCabecalho;
    }

    public void setReservadoCabecalho(String reservado) {
        this.reservadoCabecalho = reservado;
    }

    
}
