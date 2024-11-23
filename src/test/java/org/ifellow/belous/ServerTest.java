package org.ifellow.belous;

import org.ifellow.belous.steps.TestRegistrationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class ServerTest extends TestHooks {

    @Test
    @DisplayName("Тест на регистрацию пользователя")
    public void unitTest() throws IOException {
        new TestRegistrationUser().testRegistration();
    }
}
