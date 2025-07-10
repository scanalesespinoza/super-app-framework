package com.superapp;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SuperAppResourceTest {

    @Test
    public void testHelloEndpoint() {
        RestAssured.given()
                .when().get("/super")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("Super App Ready"));
    }
}
