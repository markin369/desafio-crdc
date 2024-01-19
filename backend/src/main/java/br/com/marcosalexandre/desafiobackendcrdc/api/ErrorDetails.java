package br.com.marcosalexandre.desafiobackendcrdc.api;

public class ErrorDetails {
    private int line;
    private String error;
    
    public ErrorDetails(int lineNumber, String description) {
        this.line = lineNumber;
        this.error = description;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    
}
