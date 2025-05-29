package com.uy.enRutaBackend.errors;

public class ResultadoOperacion<T> {
    private boolean success;
    private String message;
    private ErrorCode errorCode;
    private T data;

    // Constructores
    public ResultadoOperacion(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResultadoOperacion(boolean success, String message, ErrorCode errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}