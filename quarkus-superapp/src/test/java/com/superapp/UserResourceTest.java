package com.superapp;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import com.superapp.framework.ServiceRuntime;

@QuarkusTest
public class UserResourceTest {

    @Inject
    TestUserService testService;

    @Inject
    ServiceRuntime runtime;

    @Test
    public void testUserFound() {
        testService.setScenario(TestUserService.Scenario.SUCCESS);
        RestAssured.given().body("{\"user\":\"sergio\"}").contentType("application/json")
                .when().post("/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("Data for sergio"));
    }

    @Test
    public void testThrottleAndRecovery() {
        testService.setScenario(TestUserService.Scenario.TIMEOUT);
        RestAssured.given().body("{\"user\":\"sergio\"}").contentType("application/json")
                .when().post("/user")
                .then()
                .statusCode(503);
        org.junit.jupiter.api.Assertions.assertFalse(runtime.isHealthy());

        testService.setScenario(TestUserService.Scenario.SUCCESS);
        RestAssured.given().body("{\"user\":\"sergio\"}").contentType("application/json")
                .when().post("/user")
                .then()
                .statusCode(200);
        org.junit.jupiter.api.Assertions.assertTrue(runtime.isHealthy());
    }
}
