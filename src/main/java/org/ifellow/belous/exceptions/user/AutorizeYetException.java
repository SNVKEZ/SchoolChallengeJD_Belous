package org.ifellow.belous.exceptions.user;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AutorizeYetException extends RuntimeException{
    private static final Logger LOGGER = Logger.getLogger(AutorizeYetException.class.getName());

    public AutorizeYetException(String message) {
        super(message);
        LOGGER.log(Level.INFO, message);
    }
}
