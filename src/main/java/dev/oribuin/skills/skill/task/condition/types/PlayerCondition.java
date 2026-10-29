package dev.oribuin.skills.skill.task.condition.types;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public abstract class PlayerCondition extends TypeCondition<Player> {

    /**
     * Creates a new player condition to register into the plugin
     *
     * @param section The section to load
     */
    public PlayerCondition(@NotNull ConfigurationSection section) {
        super(section);
    }
    
}
