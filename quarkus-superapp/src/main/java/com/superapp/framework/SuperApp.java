package com.superapp.framework;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuperApp {

    private volatile boolean healthy = true;
    private volatile boolean throttled = false;

    public SuperApp StartNativeApp() {
        // inicia un endpoint web con parametros recomendados
        return this;
    }

    public SuperApp setHealthCheck() {
        healthy = true;
        return this;
    }

    public SuperApp setMaxMemory() {
        return this;
    }

    public SuperApp setMaxThreads() {
        return this;
    }

    public SuperApp setDefaultActionOnFail() {
        return this;
    }

    public SuperApp setDefaultActionOnThrottle() {
        return this;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public boolean isThrottled() {
        return throttled;
    }

    public void OnInCommingCall() {
        // logic when web request arrives
    }

    public <T> T OnOutGoingCall(Callable<T> call) throws Exception {
        try {
            T result = call.call();
            if (throttled) {
                onRecovery();
            }
            return result;
        } catch (TimeoutException | IOException e) {
            OnManyFails();
            throw e;
        }
    }

    public void OnManyFails() {
        throttled = true;
        healthy = false;
    }

    public void onRecovery() {
        throttled = false;
        healthy = true;
    }
}
