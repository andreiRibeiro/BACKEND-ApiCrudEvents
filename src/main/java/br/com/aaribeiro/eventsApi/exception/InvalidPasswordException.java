package br.com.aaribeiro.eventsApi.exception;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
