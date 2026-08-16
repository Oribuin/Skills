package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import dev.oribuin.skills.util.SkillUtils;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.spawner.Spawner;
import org.jetbrains.annotations.NotNull;

public class SpawnerCondition extends BlockCondition {

    private EntityType entityType;
    private int spawnCount;
    private int spawnRange;
    private boolean inverted;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public SpawnerCondition(@NotNull ConfigurationSection section) {
        super(section);

        ConfigurationSection spawnerSection = section.getConfigurationSection("spawners");
        if (spawnerSection == null) return;

        String entityType = spawnerSection.getString("entity-type");
        this.entityType = SkillUtils.getEnum(EntityType.class, entityType);
        this.spawnCount = spawnerSection.getInt("spawn-count", 0);
        this.spawnRange = spawnerSection.getInt("spawn-range", 0);
        this.inverted = entityType != null && entityType.startsWith("!");
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
        if (!(target.getState() instanceof Spawner spawner)) return false;

        boolean result = false;
        if (this.entityType != null) result = this.inverted != (this.entityType == spawner.getSpawnedType());
        if (this.spawnCount > 0) result = spawner.getSpawnCount() >= this.spawnCount;
        if (this.spawnRange > 0) result = spawner.getSpawnRange() >= this.spawnRange;
        return result;
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.section.isConfigurationSection("spawners");
    }

}
