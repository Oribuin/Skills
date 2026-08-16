package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.config.ConfigBoolean;
import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GrownCropCondition extends BlockCondition {

    private final ConfigBoolean grownCrop;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public GrownCropCondition(@NotNull ConfigurationSection section) {
        super(section);
        this.grownCrop = ConfigBoolean.check(section, "grown-crop");
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
        if (!(target.getBlockData() instanceof Ageable ageable)) return false;
        return this.grownCrop.check(ageable.getAge() >= ageable.getMaximumAge());
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.grownCrop.applicable();
    }

}
