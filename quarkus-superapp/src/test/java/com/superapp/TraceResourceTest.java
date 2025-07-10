package com.superapp;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import com.superapp.framework.ServiceRuntime;

@QuarkusTest
public class TraceResourceTest {

    @Inject
    TestUserService testService;

    @Inject
    ServiceRuntime runtime;

    @Test
    public void testTraceEndpoint() {
        testService.setScenario(TestUserService.Scenario.SUCCESS);
        RestAssured.given().body("{\"user\":\"ana\"}").contentType("application/json")
                .when().post("/user")
                .then()
                .statusCode(200);

        RestAssured.get("/trace")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("handleIncomingRequest"))
                .body(CoreMatchers.containsString("result:"));
    }
}
