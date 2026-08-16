package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.config.ConfigBoolean;
import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HydratedCondition extends BlockCondition {

    private final ConfigBoolean isHydrated;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public HydratedCondition(@NotNull ConfigurationSection section) {
        super(section);

        this.isHydrated = ConfigBoolean.check(section, "is-hydrated");
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
        if (!(target.getBlockData() instanceof Farmland farmland)) return false;
        return this.isHydrated.check(farmland.getMoisture() >= farmland.getMaximumMoisture());
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.isHydrated.applicable();
    }
}
