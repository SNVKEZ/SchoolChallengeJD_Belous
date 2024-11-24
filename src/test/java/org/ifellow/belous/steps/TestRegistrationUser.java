package org.ifellow.belous.steps;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestRegistrationUser {

    public void testRegistration(String jsonFilePath, int status, String login, String password, String name, String surname) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get(jsonFilePath))));

        body.put("login", login);
        body.put("password", password);
        body.put("name", name);
        body.put("surname", surname);

        given()
                .header("Content-type", "application/json")
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/register")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }

}
