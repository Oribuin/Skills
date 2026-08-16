package dev.oribuin.skills.skill.task.type;

import dev.oribuin.skills.skill.task.TaskLoader;
import org.bukkit.event.Event;

import java.util.function.Function;

/**
 * Create a new task type for the plugin
 *
 * @param <T> The important content enum
 */
public abstract class TaskType<T, Z extends Event> {

    private final String name;
    private final Class<T> contentClass;
    private final Class<Z> eventClass;
    protected TaskLoader<?> loader;

    /**
     * Create a new task type for the plugin
     *
     * @param name The name of the task type
     */
    public TaskType(String name, Class<T> contentClass, Class<Z> eventClass) {
        this.name = name;
        this.contentClass = contentClass;
        this.eventClass = eventClass;
    }

    /**
     * Check if the value from the event matches the task type
     *
     * @param event The event to check
     * @param value The value to check
     * @return True if the two values match each other
     */
    public abstract boolean attempt(Z event, T value);

    /**
     * Convert the value into the value stored (block -> material, etc)
     *
     * @param value The value to migrate
     * @param <Y>   The type it's migrating to
     * @return The resulting value
     */
    public abstract Object asGeneric(Object object);

    public String getName() {
        return name;
    }

    public Class<T> getContentClass() {
        return contentClass;
    }

    public Class<Z> getEventClass() {
        return eventClass;
    }

    public TaskLoader<?> getLoader() {
        return loader;
    }
}
