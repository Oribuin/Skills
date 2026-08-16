package dev.oribuin.skills.skill.task;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record TaskLoader<T>(Function<String, T> supplier) {

    /**
     * Test a task loader against a string
     *
     * @param type The type to load
     * @return The loaded type if available, null otherwise
     */
    public @Nullable T from(@Nullable String type) {
        if (type == null || this.supplier == null) return null;

        return this.supplier.apply(type);
    }

}
