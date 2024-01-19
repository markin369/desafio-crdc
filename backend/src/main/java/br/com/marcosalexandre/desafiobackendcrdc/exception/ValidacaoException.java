package br.com.marcosalexandre.desafiobackendcrdc.exception;

import java.util.List;

import br.com.marcosalexandre.desafiobackendcrdc.api.ErrorDetails;

public class ValidacaoException extends RuntimeException {

    private final List<ErrorDetails> errors;

    public ValidacaoException(String message, List<ErrorDetails> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ErrorDetails> getErrors() {
        return errors;
    }
}

