package org.ifellow.belous.tests;

import lombok.SneakyThrows;
import org.ifellow.belous.hooks.TestHooks;
import org.ifellow.belous.steps.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ScenarioTests extends TestHooks {
    @Test
    @DisplayName("Тест-сценарий с полной прогонкой сервера")
    @SneakyThrows
    public void scenarioTest() {

        String[] composers = {"Alice", "Bob"};
        String[] authors = {"Alice", "Bob"};

        //заводим двух пользаков
        String login1 = "test" + UUID.randomUUID().toString().substring(0, 12);
        String login2 = "test" + UUID.randomUUID().toString().substring(0, 12);

        //регистрируем их
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login1, "test1", "test1", "test1");
        new TestRegistrationUser().testRegistration("src/test/resources/jsons/newUser.json", 201, login2, "test1", "test1", "test1");

        //авторизуем их
        String token1 = new TestAuthorizationUser().testAuthorizationUser(200, login1);
        String token2 = new TestAuthorizationUser().testAuthorizationUser(200, login2);

        //создаем песенки под этими пользаками
        String nameSong1 = "Свое место" + UUID.randomUUID().toString().substring(0, 5);
        String nameSong2 = "Корни" + UUID.randomUUID().toString().substring(0, 5);
        String nameSong3 = "2 минуты" + UUID.randomUUID().toString().substring(0, 5);
        String nameSong4 = "Конфеты" + UUID.randomUUID().toString().substring(0, 5);

        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token1, nameSong1, 700, composers, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token1, nameSong2, 900, composers, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token2, nameSong3, 500, composers, authors);
        new TestCreateSound().testCreateSound("src/test/resources/jsons/soundCreate.json", 200, token2, nameSong4, 700, composers, authors);

        //просматриваем программу песен в данный момент
        new TestConcert().testConcert(200, token1);

        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token1, nameSong1, "Markul");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token2, nameSong2, "Markul");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token2, nameSong3, "Markul");
        new TestCreateComment().testCreateComment("src/test/resources/jsons/commentSong.json", 200, token1, nameSong4, "Markul");

        //просматриваем программу песен в данный момент
        new TestConcert().testConcert(200, token2);

        new TestCreateGrade().testCreateGrade("src/test/resources/jsons/grade.json", 200, token2, nameSong1);

        //просматриваем программу песен в данный момент
        new TestConcert().testConcert(200, token2);

        //удаляем пользака
        new TestDeleteUser().testDeleteUser(login1, token1);
    }
}
