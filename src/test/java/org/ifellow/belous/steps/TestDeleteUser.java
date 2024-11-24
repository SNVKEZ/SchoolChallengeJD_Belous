package org.ifellow.belous.steps;

import static io.restassured.RestAssured.given;

public class TestDeleteUser {
    public void testDeleteUser(String login, String token) {
        given()
                .relaxedHTTPSValidation()
                .header("Authorization", token)
                .get("http://localhost:8080/user/delete?login=" + login)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();
    }
}
