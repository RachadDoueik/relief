package com.app.relief.enums;
import java.util.Map;
import java.util.Set;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    REVIEW,
    DONE,
    CANCELLED;

    private static final Map<TaskStatus, Set<TaskStatus>> transitions = Map.of(
            TODO, Set.of(IN_PROGRESS, CANCELLED),
            IN_PROGRESS, Set.of(REVIEW, CANCELLED),
            REVIEW, Set.of(DONE, IN_PROGRESS),
            DONE, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean canTransitionTo(TaskStatus target) {
        return transitions.get(this).contains(target);
    }
}
