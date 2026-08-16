package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.config.ConfigBoolean;
import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FurnaceCondition extends BlockCondition {

    private final ConfigBoolean isCooking;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public FurnaceCondition(@NotNull ConfigurationSection section) {
        super(section);
        this.isCooking = ConfigBoolean.check(section, "is-cooking");
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
        if (!(target.getState() instanceof Furnace furnace)) return false;

        return this.isCooking.check(furnace.getCookTime() <= furnace.getCookTimeTotal());
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.isCooking.applicable();
    }

}
