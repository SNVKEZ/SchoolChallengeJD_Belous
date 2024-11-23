package org.ifellow.belous.steps;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TestOutUser {
    public void testOutUser(int status, String token) throws IOException {

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri("http://localhost:8080")
                .when()
                .get("/out")
                .then()
                .statusCode(status)
                .log().body()
                .extract()
                .response();
    }

}
