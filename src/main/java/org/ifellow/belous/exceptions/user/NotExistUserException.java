package org.ifellow.belous.exceptions.user;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotExistUserException extends RuntimeException {
    private static final Logger LOGGER = Logger.getLogger(NotExistUserException.class.getName());

    public NotExistUserException(String message) {
        super(message);
        LOGGER.log(Level.INFO, message);
    }
}
