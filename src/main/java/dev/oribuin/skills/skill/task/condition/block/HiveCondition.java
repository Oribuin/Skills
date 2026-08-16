package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.config.ConfigBoolean;
import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HiveCondition extends BlockCondition {

    private ConfigBoolean isSedated;
    private ConfigBoolean hasFlower;
    private ConfigBoolean maxBees;
    private ConfigBoolean maxHoney;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public HiveCondition(@NotNull ConfigurationSection section) {
        super(section);

        ConfigurationSection hiveSection = section.getConfigurationSection("bee-hive");
        this.isSedated = ConfigBoolean.check(hiveSection, "is-sedated");
        this.hasFlower = ConfigBoolean.check(hiveSection, "has-flower");
        this.maxBees = ConfigBoolean.check(hiveSection, "max-bees");
        this.maxHoney = ConfigBoolean.check(hiveSection, "max-honey");
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

        boolean result = false;
        // API is stupid and splits up both honey state and entity state into two seperate things
        if (target.getState() instanceof org.bukkit.block.Beehive hiveState) {
            if (this.isSedated.applicable()) result = this.isSedated.check(hiveState.isSedated());
            if (!result && this.hasFlower.applicable()) result = this.hasFlower.check(hiveState.getFlower() != null);
            if (!result && this.maxBees.applicable()) result = this.maxBees.check(hiveState.getEntityCount() == hiveState.getMaxEntities());
        }

        if (target.getBlockData() instanceof org.bukkit.block.data.type.Beehive hiveData) {
            if (!result && this.maxHoney.applicable()) result = this.maxHoney.check(hiveData.getHoneyLevel() == hiveData.getMaximumHoneyLevel());
        }
        return result;
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.isSedated.applicable()
                && this.hasFlower.applicable()
                && this.maxBees.applicable()
                && this.maxHoney.applicable();
    }

}
