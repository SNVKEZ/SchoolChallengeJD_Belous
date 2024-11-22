package org.ifellow.belous;

import lombok.SneakyThrows;
import org.ifellow.belous.steps.TestRegistrationUser;
import org.ifellow.belous.steps.TestServerConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ServerTest {

    @Test
    @DisplayName("Тест, что сервер запущен")
    public void unitTest1() {
        new TestServerConnection().testConnection();
    }

    @SneakyThrows
    @Test
    @DisplayName("Тест на регистрацию пользователя")
    public void unitTest2(){
        new TestRegistrationUser().testRegistration();
    }

}
