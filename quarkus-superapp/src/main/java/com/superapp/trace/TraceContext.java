package com.superapp.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

/**
 * Simple per-thread trace collector for demo purposes.
 */
@ApplicationScoped
public class TraceContext {
    private ThreadLocal<List<String>> traces = ThreadLocal.withInitial(ArrayList::new);
    private volatile String lastDump = "";

    public void clear() {
        traces.get().clear();
    }

    public void record(String step) {
        traces.get().add(step);
    }

    public String dump() {
        lastDump = traces.get().stream()
                .map(s -> "-> " + s)
                .collect(Collectors.joining("\n"));
        return lastDump;
    }

    public String getLastDump() {
        return lastDump;
    }
}
