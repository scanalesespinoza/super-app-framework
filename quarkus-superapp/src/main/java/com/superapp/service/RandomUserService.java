package com.superapp.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RandomUserService implements UserService {

    private Random random = new Random();

    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public Optional<String> fetchUser(String user) throws TimeoutException, IOException {
        int pick = random.nextInt(4);
        switch (pick) {
            case 0:
                return Optional.of("Data for " + user);
            case 1:
                return Optional.empty();
            case 2:
                throw new IOException("connection error");
            default:
                throw new TimeoutException("timeout");
        }
    }
}
