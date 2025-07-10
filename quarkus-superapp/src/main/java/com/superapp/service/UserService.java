package com.superapp.service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public interface UserService {
    Optional<String> fetchUser(String user) throws TimeoutException, IOException;
}
