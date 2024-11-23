package org.ifellow.belous;

import org.ifellow.belous.steps.TestServerConnection;
import org.junit.jupiter.api.BeforeAll;

public class TestHooks {
    @BeforeAll
    public static void checkServer() {
        new TestServerConnection().testConnection();
    }
}
