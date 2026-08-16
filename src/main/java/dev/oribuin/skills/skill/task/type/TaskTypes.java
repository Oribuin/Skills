package dev.oribuin.skills.skill.task.type;

import dev.oribuin.skills.skill.task.type.impl.BreakType;
import dev.oribuin.skills.skill.task.type.impl.DamageDealtType;
import dev.oribuin.skills.skill.task.type.impl.PlaceType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TaskTypes {

    private static final Map<String, TaskType<?, ?>> types = new HashMap<>();

    public static final BreakType BREAK = register(BreakType::new);
    public static final PlaceType PLACE = register(PlaceType::new);
    public static final DamageDealtType DAMAGE_DEALT_ = register(DamageDealtType::new);

    //     BREAK,
    //    PLACE,
    //    FISH,
    //    DAMAGE_DEALT,
    //    RECIEVE_DAMAGE,
    //    SMELT,
    //    CRAFT,
    //    BREW

    /**
     * Register the task type into the plugin
     *
     * @param supplier The task to register
     * @return The task type
     */
    public static <T extends TaskType<?, ?>> T register(Supplier<T> supplier) {
        T result = supplier.get();
        types.put(result.getName().toLowerCase(), result);
        return result;
    }

    /**
     * Get the task type from the type
     *
     * @param type The type to get it from
     * @return The associated task type
     */
    public static @Nullable TaskType<?, ?> from(@NotNull String type) {
        return types.get(type.toLowerCase());
    }

    /**
     * Get the task types for the associated event
     *
     * @param event The event associated to the task type
     * @param <T>   The event to use
     * @return The task type list
     */
    public static @NotNull <T extends Event> List<TaskType<?, ?>> from(@NotNull T event) {
        return types.values()
                .stream()
                .filter(x -> x.getEventClass().equals(event.getClass()))
                .toList();
    }
}
