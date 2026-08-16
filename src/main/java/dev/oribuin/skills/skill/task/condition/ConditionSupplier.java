package dev.oribuin.skills.skill.task.condition;

import dev.oribuin.skills.skill.task.condition.types.TypeCondition;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public record ConditionSupplier<T extends TypeCondition<?>>(Function<ConfigurationSection, T> function) {

    /**
     * Create a new condition type
     *
     * @param section The section to load the condition from
     * @return The condition type
     */
    public T create(ConfigurationSection section) {
        return this.function.apply(section);
    }
}
