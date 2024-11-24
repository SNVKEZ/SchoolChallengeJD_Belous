package org.ifellow.belous.steps;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TestConcert {
    public void testConcert(int status, String token) throws IOException {

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .when()
                .get("/concert")
                .then()
                .log().body()
                .statusCode(status)
                .extract()
                .response();
    }
}
