package org.ifellow.belous;

import lombok.SneakyThrows;
import org.ifellow.belous.steps.TestRegistrationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ServerTest extends TestHooks {

    @SneakyThrows
    @Test
    @DisplayName("Тест на регистрацию пользователя")
    public void unitTest2(){
        new TestRegistrationUser().testRegistration();
    }
}
