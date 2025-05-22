package com.uy.enRutaBackend.errors;

public class ResultadoOperacion<T> {
    private boolean success;
    private String message;
    private String errorCode; // opcional, como enum más adelante
    private T data;

    // Constructores
    public ResultadoOperacion(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResultadoOperacion(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}