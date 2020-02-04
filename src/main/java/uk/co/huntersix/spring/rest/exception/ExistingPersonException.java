package uk.co.huntersix.spring.rest.exception;

public class ExistingPersonException extends Throwable {
    public ExistingPersonException(String message) {
        super(message);
    }
}
