package org.ifellow.belous.steps;

import static io.restassured.RestAssured.given;

public class TestServerConnection {
    public void testConnection() {
            given()
                    .relaxedHTTPSValidation()
                    .get("http://localhost:8080")
                    .then()
                    .extract()
                    .response();
    }
}
