package dev.oribuin.skills.skill.task.condition.types;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class BlockCondition extends TypeCondition<Block> {

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public BlockCondition(@NotNull ConfigurationSection section) {
        super(section);
    }

}
