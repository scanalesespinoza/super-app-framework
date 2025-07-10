package com.superapp;

import javax.inject.Inject;
import javax.enterprise.event.Observes;
import io.quarkus.runtime.StartupEvent;
import com.superapp.framework.ServiceRuntime;

public class ServiceRuntimeLifecycle {
    @Inject
    ServiceRuntime runtime;

    void onStart(@Observes StartupEvent ev) {
        runtime.initializeService()
            .enableHealthCheck()
            .limitMemoryUsage()
            .limitThreadCount()
            .defaultActionOnFail()
            .defaultActionOnThrottle();
    }
}
