package br.com.marcosalexandre.desafiobackendcrdc.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String message;
    private Object data;
    private List<ErrorDetails> errors;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public List<ErrorDetails> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorDetails> errors) {
        this.errors = errors;
    }

    // Getters e 
    
}


