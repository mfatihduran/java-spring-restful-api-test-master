package uk.co.huntersix.spring.rest.exception;

public class PersonNotFoundException extends Throwable {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
