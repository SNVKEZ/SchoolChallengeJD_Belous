package org.ifellow.belous.tests;

import org.ifellow.belous.hooks.TestHooks;
import org.ifellow.belous.steps.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTests extends TestHooks {
    private static final Logger LOGGER = Logger.getLogger(UnitTests.class.getName());
    private static String token;

    @Test
    @DisplayName("Тест на регистрацию пользователя")
    @Order(1)
    @Tag("Unit")
    public void unitTestRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на регистрацию пользователя");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на запрет повторной регистрации зарегистрированного пользователя")
    @Order(2)
    @Tag("Unit")
    public void unitTestRepeatedRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторной регистрации зарегистрированного пользователя");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 409, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на обработку пустого body")
    @Order(3)
    @Tag("Unit")
    public void unitTestEmptyBodyRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на обработку пустого body");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/empty.json", 400, null, null, null, null);
    }

    @Test
    @DisplayName("Тест на обработку ответа без указания всех обязательных полей")
    @Order(4)
    @Tag("Unit")
    public void unitTestNecessarilyBodyRegistration() throws IOException {
        LOGGER.log(Level.INFO, "Тест на обработку ответа без указания всех обязательных полей");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/necessarilyFieldUser.json", 400, "test1", null, null, "test1");
    }

    @Test
    @DisplayName("Тест на авторизацию пользователя")
    @Order(5)
    @Tag("Unit")
    public void unitTestAuthorization() throws IOException {
        LOGGER.log(Level.INFO, "Тест на авторизацию пользователя");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200);


    }

    @Test
    @DisplayName("Тест на запрет повторной авторизации уже авторизованного пользователя")
    @Order(6)
    @Tag("Unit")
    public void unitTestRepeatedAuthorization() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторной авторизации уже авторизованного пользователя");
        new TestAuthorizationUser().testAuthorizationUser(400);

    }

    @Test
    @DisplayName("Тест на выход пользователя из сессии")
    @Order(7)
    @Tag("Unit")
    public void unitTestOut() throws IOException {
        LOGGER.log(Level.INFO, "Тест на выход пользователя из сессии");
        new TestOutUser().testOutUser(200, token);

    }

    @Test
    @DisplayName("Тест на запрет повторного выхода с отсутствующей сессией")
    @Order(8)
    @Tag("Unit")
    public void unitRepeatedTestOut() throws IOException {
        LOGGER.log(Level.INFO, "Тест на запрет повторного выхода с отсутствующей сессией");
        new TestOutUser().testOutUser(400, token);

    }

    @Test
    @DisplayName("Тест на успешное создание песни")
    @Order(9)
    @Tag("Unit")
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
    @Tag("Unit")
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
    @Tag("Unit")
    public void unitCreateCommentSongTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на успешное добавление комментария к песне");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token, "Zima Blue", "Markul");

    }

    @Test
    @DisplayName("Тест на неуспешное добавление комментария к несуществующей песне")
    @Order(12)
    @Tag("Unit")
    public void unitErrorCreateCommentSongTest() throws IOException {
        LOGGER.log(Level.INFO, "Тест на неуспешное добавление комментария к несуществующей песне");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 404, token, "loud", "sound");

    }

    @Test
    @DisplayName("Тест на недоступность изменения оценки владельцем предложенной песни")
    @Order(13)
    @Tag("Unit")
    public void unitErrorModifyGradeByOwner() throws IOException {
        LOGGER.log(Level.INFO, "Тест на недоступность изменения оценки владельцем предложенной песни");
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 422, token);
    }

    @Test
    @DisplayName("Тест на оценивание песни не владельцем")
    @Order(14)
    @Tag("Unit")
    public void unitGradeSong() throws IOException {
        LOGGER.log(Level.INFO, "Тест на оценивание песни не владельцем");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreateOther1.json", 200, token, 3500, composers, authors);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 200, token);
    }

    @Test
    @DisplayName("Тест на оценивание несуществующей песни не владельцем")
    @Order(15)
    @Tag("Unit")
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
    @Tag("Unit")
    public void unitDeleteUser() throws IOException {
        LOGGER.log(Level.INFO, "Тест удаления пользователя");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        String login2 = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);

        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login2, "test1", "test1", "test1");
        String token2 = testAuthorizationUser.testAuthorizationUser(200, login2);

        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreateOther.json", 200, token, 400, composers, authors);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/gradeOther.json", 200, token2);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreateOther2.json", 200, token, 2870, composers, authors);
        new TestConcert().testConcert(200, token);
        new TestDeleteUser().testDeleteUser(login, token);
    }

    @Test
    @DisplayName("Тест на просмотр концерта")
    @Order(17)
    @Tag("Unit")
    public void unitShowConcert() throws IOException {
        LOGGER.log(Level.INFO, "Тест на просмотр концерта");
        String login = "test" + UUID.randomUUID().toString().substring(0, 12);
        String login2 = "test" + UUID.randomUUID().toString().substring(0, 12);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200, login);

        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login2, "test1", "test1", "test1");
        String token2 = testAuthorizationUser.testAuthorizationUser(200, login2);

        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreateOther.json", 200, token, 400, composers, authors);
        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/gradeOther.json", 200, token2);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreateOther2.json", 200, token, 2870, composers, authors);
        new TestConcert().testConcert(200, token);
        new TestDeleteUser().testDeleteUser(login, token);
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login, "test1", "test1", "test1");
        new TestConcert().testConcert(200, token);
    }
}