package org.ifellow.belous;

import org.ifellow.belous.hooks.TestHooks;
import org.ifellow.belous.steps.*;
import org.junit.jupiter.api.*;

import java.io.IOException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest extends TestHooks {
    private static String token;

    @Test
    @DisplayName("Тест на регистрацию пользователя")
    @Order(1)
    public void unitTestRegistration() throws IOException {
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на запрет повторной регистрации зарегистрированного пользователя")
    @Order(2)
    public void unitTestRepeatedRegistration() throws IOException {
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 400, "test1", "test1", "test1", "test1");
    }

    @Test
    @DisplayName("Тест на обработку пустого body")
    @Order(3)
    public void unitTestEmptyBodyRegistration() throws IOException {
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/empty.json", 400, null, null, null, null);
    }

    @Test
    @DisplayName("Тест на обработку ответа без указания всех обязательных полей")
    @Order(4)
    public void unitTestNecessarilyBodyRegistration() throws IOException {
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/necessarilyFieldUser.json", 400, "test1", null, null, "test1");
    }

    @Test
    @DisplayName("Тест на авторизацию пользователя")
    @Order(5)
    public void unitTestAuthorization() throws IOException {
        TestAuthorizationUser testAuthorizationUser = new TestAuthorizationUser();
        token = testAuthorizationUser.testAuthorizationUser(200);

    }

    @Test
    @DisplayName("Тест на запрет повторной авторизации пользователя")
    @Order(6)
    public void unitTestRepeatedAuthorization() throws IOException {
        new TestAuthorizationUser().testAuthorizationUser(400);

    }

    @Test
    @DisplayName("Тест на выход пользователя из сессии")
    @Order(7)
    public void unitTestOut() throws IOException {
        new TestOutUser().testOutUser(200, token);

    }

    @Test
    @DisplayName("Тест на запрет повторного выхода с отсутствующей сессией")
    @Order(8)
    public void unitRepeatedTestOut() throws IOException {
        new TestOutUser().testOutUser(400, token);

    }

    @Test
    @DisplayName("Тест на успешное создание песни")
    @Order(9)
    public void unitCreateSoundTest() throws IOException {
        // обратно авторизуемся, для получения токена
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
        // ТУТ ДОЛЖНА БЫТЬ НЕ 403, везде 400
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 0, composers, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 3601, composers, authors);

    }

    @Test
    @DisplayName("Тест на неуспешное создание песни, если один из параметров composers, authors пустой")
    @Order(11)
    public void unitErrorCreateParamsSoundTest() throws IOException {
        // ТУТ ДОЛЖНА БЫТЬ НЕ 403, везде 400
        String[] composersEmpty = {""};
        String[] authors = {"Alice", "Bob"};
        String[] authorsEmpty = {""};
        String[] composers = {"Alice", "Bob"};

        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 2400, composersEmpty, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 403, token, 3000, composers, authorsEmpty);

    }

    @Test
    @DisplayName("Тест на неуспешное песни, c невалидным токен принадлежит другому пользователю")
    @Order(12)
    public void unitErrorCreateAnoutherTokenSoundTest() throws IOException {
        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, "test2", "test2", "test2", "test2");
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 400, "1", 2500, composers, authors);

    }

    @Test
    @DisplayName("Тест на успешное добавление комментария к песне")
    @Order(13)
    public void unitCreateCommentSongTest() throws IOException {
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token, "Zima Blue", "Markul");

    }

    @Test
    @DisplayName("Тест на неуспешное добавление комментария к несуществующей песне")
    @Order(13)
    public void unitErrorCreateCommentSongTest() throws IOException {
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 404, token, "loud", "sound");

    }


}