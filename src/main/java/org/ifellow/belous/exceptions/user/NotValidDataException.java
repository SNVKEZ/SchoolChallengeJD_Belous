package org.ifellow.belous.exceptions.user;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotValidDataException extends RuntimeException{
    private static final Logger LOGGER = Logger.getLogger(NotValidDataException.class.getName());

    public NotValidDataException(String message) {
        super(message);
        LOGGER.log(Level.INFO, message);
    }
}
