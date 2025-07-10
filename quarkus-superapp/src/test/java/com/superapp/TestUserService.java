package com.superapp;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import com.superapp.service.UserService;

@ApplicationScoped
@Alternative
public class TestUserService implements UserService {

    public enum Scenario { SUCCESS, NOT_FOUND, CONNECTION_ERROR, TIMEOUT }

    private Scenario scenario = Scenario.SUCCESS;

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public Optional<String> fetchUser(String user) throws TimeoutException, IOException {
        switch (scenario) {
            case SUCCESS:
                return Optional.of("Data for " + user);
            case NOT_FOUND:
                return Optional.empty();
            case CONNECTION_ERROR:
                throw new IOException("connection error");
            case TIMEOUT:
                throw new TimeoutException("timeout");
            default:
                return Optional.empty();
        }
    }
}
