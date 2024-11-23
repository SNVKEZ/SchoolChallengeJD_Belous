package org.ifellow.belous;

import org.ifellow.belous.steps.TestServerConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.logging.Logger;

public class TestHooks {
    private static final Logger LOGGER = Logger.getLogger(TestHooks.class.getName());
    @BeforeAll
    public static void checkServer() {
        try {
            new TestServerConnection().testConnection();
        } catch (Exception e) {
            Assertions.fail("Включи сервер в классе main/java/Server -> psvm, пожалуйста");
        }
    }
}
