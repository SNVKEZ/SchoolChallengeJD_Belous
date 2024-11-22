package org.ifellow.belous.steps;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestRegistrationUser {

    public void testRegistration() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/jsons/newUser.json"))));

        given()
                .header("Content-type","application/json")
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .log().body()
                .extract()
                .response();
    }

}
