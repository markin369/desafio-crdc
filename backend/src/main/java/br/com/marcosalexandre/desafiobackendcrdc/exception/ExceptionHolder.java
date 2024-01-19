package br.com.marcosalexandre.desafiobackendcrdc.exception;

import org.springframework.stereotype.Component;

@Component
public class ExceptionHolder {

    private Throwable exception;

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void limparException(){
        this.exception = new Exception(); 
    }
}

