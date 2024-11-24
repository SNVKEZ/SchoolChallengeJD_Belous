package org.ifellow.belous.steps;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestAuthorizationUser {
    private static String token;

    public String testAuthorizationUser(int status) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/jsons/authorizationUser.json"))));

        token = given()
                .header("Content-type", "application/json")
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/authorization")
                .then()
                .statusCode(status)
                .log().body()
                .extract()
                .jsonPath()
                .getString("token");

        return token;

    }

    public String testAuthorizationUser(int status, String login) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/jsons/authorizationUser.json"))));

        body.put("login", login);

        token = given()
                .header("Content-type", "application/json")
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/authorization")
                .then()
                .statusCode(status)
                .log().body()
                .extract()
                .jsonPath()
                .getString("token");

        return token;

    }

}
