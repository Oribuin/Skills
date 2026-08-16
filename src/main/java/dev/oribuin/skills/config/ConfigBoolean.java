package dev.oribuin.skills.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Create a new config boolean that will check whether something should be checked and if it's inverted
 *
 * @param applicable   Whether the option should be checked
 * @param inverted Whether the config check should be inverted
 */
public record ConfigBoolean(boolean applicable, boolean inverted) {

    /**
     * Get a config boolean from a config section
     *
     * @param section The section to get the boolean from
     * @param path    The path to the boolean
     * @return The resulting config and whether it's inverted
     */
    @NotNull
    public static ConfigBoolean check(@Nullable ConfigurationSection section, @NotNull String path) {
        ConfigBoolean empty = new ConfigBoolean(false, false);
        if (section == null) return empty;

        String line = section.getString(path);
        if (line == null || line.isEmpty()) return empty;

        boolean inverted = line.startsWith("!");
        boolean result = Boolean.parseBoolean(inverted ? line.substring(1) : line);
        return new ConfigBoolean(result, inverted);
    }

    /**
     * Check against an option for the plugin and whether it should be inverted
     *
     * @param existing The existing config
     * @return The applicable of the check
     */
    public boolean check(boolean existing) {
        if (!this.applicable) return false;
        return this.inverted != existing;
    }

}
