package com.superapp.framework;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.superapp.UserRequest;

/**
 * Simple implementation of the central application flow.
 * <p>
 * Each step returns a boolean indicating success. If a step fails the
 * execution stops and the reason is recorded via {@link ServiceRuntime}.
 */
@ApplicationScoped
public class CentralFlow {

    private static final long MIN_FREE_MEMORY = 50L * 1024 * 1024; // 50 MB
    private static final int MAX_ACTIVE_THREADS = 100;

    @Inject
    ServiceRuntime runtime;

    private Object processingData;

    /** Result of running the flow. */
    public static class FlowResult<T> {
        public final boolean success;
        public final String reason;
        public final T payload;

        FlowResult(boolean success, String reason, T payload) {
            this.success = success;
            this.reason = reason;
            this.payload = payload;
        }
    }

    /**
     * Execute the central flow for the /user endpoint.
     *
     * @param request the incoming request
     * @param fetch   callable that retrieves the user data
     */
    public FlowResult<Optional<String>> executeUser(UserRequest request, Callable<Optional<String>> fetch) {
        runtime.startTrace();
        try {
            if (!checkHealth()) {
                return new FlowResult<>(false, "health check failed", null);
            }
            initialize();
            if (!canHandleRequest()) {
                return new FlowResult<>(false, "cannot handle request", null);
            }
            if (!isRequestValid(request)) {
                return new FlowResult<>(false, "invalid request", null);
            }
            if (!externalServicesAvailable()) {
                return new FlowResult<>(false, "external services unavailable", null);
            }
            Optional<String> data;
            try {
                data = runtime.handleOutgoingCall(fetch);
            } catch (Exception e) {
                runtime.recordError(e.getClass().getSimpleName());
                return new FlowResult<>(false, "external call error", null);
            }
            if (!externalCallsSucceeded(data)) {
                return new FlowResult<>(false, "external call error", null);
            }
            processingData = data;
            if (!processTransaction()) {
                return new FlowResult<>(false, "transaction failed", null);
            }
            runtime.recordResult("success");
            return new FlowResult<>(true, "ok", data);
        } finally {
            cleanup();
            runtime.getTrace();
        }
    }

    private boolean checkHealth() {
        long free = Runtime.getRuntime().freeMemory();
        int threads = Thread.activeCount();
        boolean ok = free > MIN_FREE_MEMORY && threads < MAX_ACTIVE_THREADS && runtime.isHealthy();
        if (!ok) {
            runtime.recordError("health");
        }
        return ok;
    }

    private void initialize() {
        processingData = new Object();
    }

    private boolean canHandleRequest() {
        // placeholder for dependency validation
        boolean ok = runtime.isHealthy();
        if (!ok) {
            runtime.recordError("dependencies");
        }
        return ok;
    }

    private boolean isRequestValid(UserRequest req) {
        return req != null && req.user != null && !req.user.isBlank();
    }

    private boolean externalServicesAvailable() {
        // placeholder check for external service availability
        return true;
    }

    private boolean externalCallsSucceeded(Optional<String> data) {
        return data != null;
    }

    private boolean processTransaction() {
        return processingData != null;
    }

    private void cleanup() {
        processingData = null;
    }
}
