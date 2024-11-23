package org.ifellow.belous.exceptions.user;

public class NotExistTokenSession extends RuntimeException {
    public NotExistTokenSession(String message) {
        super(message);
    }
}
