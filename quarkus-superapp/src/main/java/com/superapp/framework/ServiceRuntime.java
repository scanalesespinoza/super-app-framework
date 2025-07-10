package com.superapp.framework;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.superapp.trace.TraceContext;

@ApplicationScoped
public class ServiceRuntime {

    private volatile boolean serviceHealthy = true;
    private volatile boolean throttleActive = false;

    @Inject
    TraceContext trace;

    public ServiceRuntime initializeService() {
        // inicia un endpoint web con parametros recomendados
        return this;
    }

    public ServiceRuntime enableHealthCheck() {
        serviceHealthy = true;
        return this;
    }

    public ServiceRuntime limitMemoryUsage() {
        return this;
    }

    public ServiceRuntime limitThreadCount() {
        return this;
    }

    public ServiceRuntime defaultActionOnFail() {
        return this;
    }

    public ServiceRuntime defaultActionOnThrottle() {
        return this;
    }

    public boolean isHealthy() {
        return serviceHealthy;
    }

    public boolean isThrottled() {
        return throttleActive;
    }

    public void startTrace() {
        trace.clear();
        trace.record("start request");
    }

    public String getTrace() {
        return trace.dump();
    }

    public void handleIncomingRequest() {
        trace.record("handleIncomingRequest: healthy=" + serviceHealthy + ", throttled=" + throttleActive);
        // logic when web request arrives
    }

    public <T> T handleOutgoingCall(Callable<T> call) throws Exception {
        trace.record("handleOutgoingCall: start");
        try {
            T result = call.call();
            trace.record("handleOutgoingCall: success");
            if (throttleActive) {
                recoverService();
            }
            return result;
        } catch (TimeoutException | IOException e) {
            trace.record("handleOutgoingCall: " + e.getClass().getSimpleName());
            activateThrottle();
            throw e;
        }
    }

    public void activateThrottle() {
        throttleActive = true;
        serviceHealthy = false;
        trace.record("activateThrottle -> throttled");
    }

    public void recoverService() {
        throttleActive = false;
        serviceHealthy = true;
        trace.record("recoverService -> healthy");
    }
}
