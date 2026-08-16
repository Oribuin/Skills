package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LightLevelCondition extends BlockCondition {

    private final int lightLevel;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public LightLevelCondition(@NotNull ConfigurationSection section) {
        super(section);
        this.lightLevel = section.getInt("light-level");
    }

    /**
     * Check to see if the condition can apply to the value provided
     *
     * @param executor The person running the task
     * @param target   The type to check for
     * @return true if the condition passes
     */
    @Override
    public boolean test(@NotNull Player executor, @NotNull Block target) {
        return target.getLightLevel() >= this.lightLevel;
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.section.contains("light-level");
    }
}
