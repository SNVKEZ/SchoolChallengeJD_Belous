package org.ifellow.belous.steps;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestCreateComment {
    public void testCreateComment(String jsonFilePath, int status, String token, String name, String executor) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject body = new JSONObject(jsonString);

        body.put("name", name);
        body.put("executor", executor);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/comment/create")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }
}
