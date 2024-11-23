package org.ifellow.belous.exceptions;

public class NotExistTokenSession extends RuntimeException {
    public NotExistTokenSession(String message) {
        super(message);
    }
}
