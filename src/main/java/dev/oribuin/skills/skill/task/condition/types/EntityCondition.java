package dev.oribuin.skills.skill.task.condition.types;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class EntityCondition extends TypeCondition<Entity> {

    /**
     * Creates a new entity condition to register into the plugin
     *
     * @param section The section to load
     */
    public EntityCondition(@NotNull ConfigurationSection section) {
        super(section);
    }
    
}
