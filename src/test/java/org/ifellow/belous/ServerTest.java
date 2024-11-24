package org.ifellow.belous;

import org.ifellow.belous.hooks.TestHooks;
import org.ifellow.belous.steps.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest extends TestHooks {
    private static final Logger LOGGER = Logger.getLogger(ServerTest.class.getName());
    private static String token;

    @Test
    @DisplayName("Тест на регистрацию пользователя")
    @Order(1)
    public void unitTestRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на регистрацию пользователя");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на запрет повторной регистрации зарегистрированного пользователя")
    @Order(2)
    public void unitTestRepeatedRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторной регистрации зарегистрированного пользователя");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 409, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на обработку пустого body")
    @Order(3)
    public void unitTestEmptyBodyRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на обработку пустого body");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/empty.json", 400, null, null, null, null);
    }

    @Test
    @DisplayName("Тест на обработку ответа без указания всех обязательных полей")
    @Order(4)
    public void unitTestNecessarilyBodyRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на обработку ответа без указания всех обязательных полей");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/necessarilyFieldUser.json", 400, "test1", null, null, "test1");
    }

    @Test
    @DisplayName("Тест на авторизацию пользователя")
    @Order(5)
    public void unitTestAuthorization() throws IOException {
        LOGGER.log(Level.INFO, "Тест на авторизацию пользователя");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200);

    }

    @Test
    @DisplayName("Тест на запрет повторной авторизации уже авторизованного пользователя")
    @Order(6)
    public void unitTestRepeatedAuthorization() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторной авторизации уже авторизованного пользователя");
        new TestAuthorizationUser().testAuthorizationUser(400);

    }

    @Test
    @DisplayName("Тест на выход пользователя из сессии")
    @Order(7)
    public void unitTestOut() throws IOException {
        LOGGER.log(Level.INFO, "Тест на выход пользователя из сессии");
        new TestOutUser().testOutUser(200, token);

    }

    @Test
    @DisplayName("Тест на запрет повторного выхода с отсутствующей сессией")
    @Order(8)
    public void unitRepeatedTestOut() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторного выхода с отсутствующей сессией");
        new TestOutUser().testOutUser(400, token);

    }

    @Test
    @DisplayName("Тест на успешное создание песни")
    @Order(9)
    public void unitCreateSoundTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на успешное создание песни");
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token, 3500, composers, authors);

    }

    @Test
    @DisplayName("Тест на неуспешное создание песни при duration > 3600 и < 1")
    @Order(10)
    public void unitErrorCreateDurationsSoundTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на неуспешное создание песни при duration > 3600 и < 1");
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 0, composers, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 3601, composers, authors);

    }

    @Test
    @DisplayName("Тест на успешное добавление комментария к песне")
    @Order(11)
    public void unitCreateCommentSongTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на успешное добавление комментария к песне");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token, "Zima Blue", "Markul");

    }

    @Test
    @DisplayName("Тест на неуспешное добавление комментария к несуществующей песне")
    @Order(12)
    public void unitErrorCreateCommentSongTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на неуспешное добавление комментария к несуществующей песне");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 404, token, "loud", "sound");

    }

    @Test
    @DisplayName("Тест на недоступность изменения оценки владельцем предложенной песни")
    @Order(13)
    public void unitErrorModifyGradeByOwner() throws IOException {
        LOGGER.log(Level.INFO, "Тест на недоступность изменения оценки владельцем предложенной песни");
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 422, token);
    }

    @Test
    @DisplayName("Тест на оценивание песни не владельцем")
    @Order(14)
    public void unitGradeSong() throws IOException {
        LOGGER.log(Level.INFO, "Тест на оценивание песни не владельцем");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 200, token);
    }

    @Test
    @DisplayName("Тест на оценивание несуществующей песни не владельцем")
    @Order(15)
    public void unitGradeNotExistSong() throws IOException {
        LOGGER.log(Level.INFO, "Тест на оценивание несуществующей песни не владельцем");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/gradeNotExistMusic.json", 404, token);
    }

    @Test
    @DisplayName("Тест удаления пользователя")
    @Order(16)
    public void unitDeleteUser() throws IOException {
        LOGGER.log(Level.INFO, "Тест удаления пользователя");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token, 3500, composers, authors);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 200, token);
        new TestDeleteUser().testDeleteUser(login, token);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
    }
}