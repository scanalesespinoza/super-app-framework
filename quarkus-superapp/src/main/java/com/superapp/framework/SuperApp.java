package com.superapp.framework;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.superapp.trace.TraceContext;

@ApplicationScoped
public class SuperApp {

    private volatile boolean healthy = true;
    private volatile boolean throttled = false;

    @Inject
    TraceContext trace;

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

    public void startTrace() {
        trace.clear();
        trace.record("start request");
    }

    public String getTrace() {
        return trace.dump();
    }

    public void OnInCommingCall() {
        trace.record("OnInCommingCall: healthy=" + healthy + ", throttled=" + throttled);
        // logic when web request arrives
    }

    public <T> T OnOutGoingCall(Callable<T> call) throws Exception {
        trace.record("OnOutGoingCall: start");
        try {
            T result = call.call();
            trace.record("OnOutGoingCall: success");
            if (throttled) {
                onRecovery();
            }
            return result;
        } catch (TimeoutException | IOException e) {
            trace.record("OnOutGoingCall: " + e.getClass().getSimpleName());
            OnManyFails();
            throw e;
        }
    }

    public void OnManyFails() {
        throttled = true;
        healthy = false;
        trace.record("OnManyFails -> throttled");
    }

    public void onRecovery() {
        throttled = false;
        healthy = true;
        trace.record("onRecovery -> healthy");
    }
}
