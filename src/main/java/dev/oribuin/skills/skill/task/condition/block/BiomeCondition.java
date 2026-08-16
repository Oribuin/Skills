package dev.oribuin.skills.skill.task.condition.block;

import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BiomeCondition extends BlockCondition {

    private final List<String> biomes;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public BiomeCondition(@NotNull ConfigurationSection section) {
        super(section);

        this.biomes = section.getStringList("biomes");
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
        if (this.biomes == null || this.biomes.isEmpty()) return false;

        Key biomeKey = target.getBiome().key();
        return this.biomes.stream().anyMatch(s -> {
            Key key = Key.key(s);
            boolean matches = key.asString().equalsIgnoreCase(biomeKey.asString());
            boolean inverted = s.startsWith("!");
            return inverted != matches;
        });
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.biomes != null && !this.biomes.isEmpty();
    }

}
