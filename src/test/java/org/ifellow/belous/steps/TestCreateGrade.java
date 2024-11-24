package org.ifellow.belous.steps;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestCreateGrade {
    public void testCreateGrade(String jsonFilePath, int status, String token) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject body = new JSONObject(jsonString);


        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .put("/song/rate")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }

    public void testCreateGrade(String jsonFilePath, int status, String token, String name) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject body = new JSONObject(jsonString);

        body.put("name", name);


        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .put("/song/rate")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }
}
