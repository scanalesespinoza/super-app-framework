package com.superapp.framework;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuperApp {
    public SuperApp StartNativeApp() {
        // inicia un endpoint web con parametros recomendados
        return this;
    }

    public SuperApp setHealthCheck() {
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

    public void OnInCommingCall() {
        // logic when web request arrives
    }

    public void OnOutGoingCall() {
        // logic when try to call another service
    }

    public void OnManyFails() {
        // logic to wait, block incomming calls and recover service when outgoing is recovered
    }
}
