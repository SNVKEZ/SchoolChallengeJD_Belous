package org.ifellow.belous.steps;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestCreateSound {
    public void testCreateSound(String jsonFilePath, int status, String token, int duration, String[] composers, String[] authors) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject body = new JSONObject(jsonString);
        body.put("duration", duration);

        JSONArray composersArray = new JSONArray(composers);
        JSONArray authorsArray = new JSONArray(authors);

        body.put("composers", composersArray);
        body.put("authors", authorsArray);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/song/create")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }

    public void testCreateSound(String jsonFilePath, int status, String token, String name, int duration, String[] composers, String[] authors) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject body = new JSONObject(jsonString);
        body.put("duration", duration);
        body.put("name", name);

        JSONArray composersArray = new JSONArray(composers);
        JSONArray authorsArray = new JSONArray(authors);

        body.put("composers", composersArray);
        body.put("authors", authorsArray);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .body(body.toString())
                .when()
                .post("/song/create")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }
}
