package br.com.dio.exception;

public class CustomException extends Exception {
    public CustomException(String message, final Throwable cause) {
        super(message, cause);
    }
}
