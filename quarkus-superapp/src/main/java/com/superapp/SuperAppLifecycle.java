package com.superapp;

import javax.inject.Inject;
import javax.enterprise.event.Observes;
import io.quarkus.runtime.StartupEvent;
import com.superapp.framework.SuperApp;

public class SuperAppLifecycle {
    @Inject
    SuperApp app;

    void onStart(@Observes StartupEvent ev) {
        app.StartNativeApp()
            .setHealthCheck()
            .setMaxMemory()
            .setMaxThreads()
            .setDefaultActionOnFail()
            .setDefaultActionOnThrottle();
    }
}
