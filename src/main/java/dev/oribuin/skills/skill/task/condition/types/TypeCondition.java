package dev.oribuin.skills.skill.task.condition.types;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class TypeCondition<T> {

    protected final ConfigurationSection section;

    public TypeCondition(@NotNull ConfigurationSection section) {
        this.section = section;
    }

    /**
     * Check to see if the condition can apply to the value provided
     *
     * @param executor The person running the task
     * @param target   The type to check for
     * @return true if the condition passes
     */
    public abstract boolean test(@NotNull Player executor, @NotNull T target);

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    public abstract boolean isApplicable();
    
}
