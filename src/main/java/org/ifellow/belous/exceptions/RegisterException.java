package org.ifellow.belous.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterException extends RuntimeException {

    private static final Logger LOGGER = Logger.getLogger(RegisterException.class.getName());

    public RegisterException(String message) {
        super(message);
        LOGGER.log(Level.INFO, message);
    }
}
