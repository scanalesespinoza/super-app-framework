package com.superapp.trace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.lang.StackWalker;
import java.lang.StackWalker.StackFrame;

import javax.enterprise.context.ApplicationScoped;

/**
 * Simple per-thread trace collector for demo purposes.
 * <p>
 * The context now stores the last ten completed request flows so that the
 * /trace endpoint can display recent activity.
 */
@ApplicationScoped
public class TraceContext {
    private ThreadLocal<List<String>> traces = ThreadLocal.withInitial(ArrayList::new);
    private Deque<List<String>> history = new ArrayDeque<>();
    private AtomicInteger counter = new AtomicInteger();

    public void clear() {
        traces.get().clear();
    }

    public void record(String step) {
        traces.get().add(step);
    }

    public void record(String step, Class<?> clazz, String method) {
        traces.get().add(step + " [" + clazz.getSimpleName() + ", " + method + "]");
    }

    /**
     * Record a step automatically tagging the calling class and method.
     */
    public void recordAuto(String step) {
        StackFrame frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(2).findFirst()).orElse(null);
        if (frame != null) {
            traces.get().add(step + " [" + frame.getDeclaringClass().getSimpleName() + ", " + frame.getMethodName() + "]");
        } else {
            traces.get().add(step);
        }
    }

    /**
     * Dump the current trace and add it to the history of recent traces.
     */
    public synchronized String dump() {
        List<String> copy = new ArrayList<>(traces.get());
        history.addLast(copy);
        if (history.size() > 10) {
            history.removeFirst();
        }
        counter.incrementAndGet();
        String dump = copy.stream()
                .map(s -> "##-> " + s)
                .collect(Collectors.joining("\n"));
        traces.get().clear();
        return dump;
    }

    /**
     * Return a dump of the last ten recorded traces.
     */
    public synchronized String getLastDump() {
        int startId = counter.get() - history.size() + 1;
        int id = startId;
        StringBuilder sb = new StringBuilder();
        for (List<String> trace : history) {
            sb.append("# Request: ").append(id).append('\n');
            sb.append("## Request count: ").append(id).append('\n');
            sb.append(trace.stream()
                    .map(s -> "##-> " + s)
                    .collect(Collectors.joining("\n")));
            sb.append("\n\n");
            id++;
        }
        return sb.toString().trim();
    }
}
