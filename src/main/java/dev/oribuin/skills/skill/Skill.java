package dev.oribuin.skills.skill;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.task.SkillTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Skill = The parent skill for something, e.g. mining, swords, farming, etc
 * Skill Task = The task type + associated value for it
 * Skill Task Type = The type of action that needs to be done to progress
 * Skill Ability = Passive Upgrades for the user e.g. explosives, more damage, faster bite time, etc
 * <p>
 * Formula = The levelling formula to decide how much xp is required to level up
 * Required Tools = The materials that a user must be holding to complete the action
 * Max Level = The maximum level a person can have for the skill/
 */
public class Skill {

    private final @NotNull String name;
    private final @NotNull Map<String, SkillTask<?>> tasks;
    private @NotNull Boolean enabled;
    private @NotNull String formula;
    private @Nullable Integer maxLevel;
    private @Nullable ConfigurationSection config;
    private @Nullable File file;

    public Skill(@NotNull String name) {
        this.name = name;
        this.tasks = new HashMap<>();
        this.enabled = true;
        this.formula = "%level% * 325";
        this.maxLevel = null;
    }

    /**
     * Load a skill from a configuration section
     *
     * @param config The section to load from
     * @return The skill if available
     */
    @Nullable
    public static Skill from(@NotNull File file, @NotNull FileConfiguration config) {
        String skillName = config.getString("name");
        if (skillName == null) {
            SkillsPlugin.getInstance().getLogger().severe("Failed to load a skill name from a config");
            return null;
        }

        Skill skill = new Skill(skillName);
        skill.setConfig(config); // load the config
        skill.setFile(file); // load the file
        skill.setEnabled(config.getBoolean("enabled", true));
        skill.setFormula(config.getString("formula", skill.getFormula())); // load the levelling formula
        skill.setMaxLevel(config.get("max-level") != null ? config.getInt("max-level") : null); // load the max level formula

        ConfigurationSection tasks = config.getConfigurationSection("tasks");
        if (tasks == null) {
            SkillsPlugin.getInstance().getLogger().severe("Missing tasks segment in skill[" + skillName + "]");
            return skill;
        }

        // Load all the tasks for the skill inside the plugin
        for (String taskAction : tasks.getKeys(false)) {
            ConfigurationSection taskActionSection = tasks.getConfigurationSection(taskAction);
            if (taskActionSection == null) continue;

            SkillTask<?> task = SkillTask.from(taskActionSection, taskAction);
            if (task != null) skill.getTasks().put(taskAction, task);
        }

        // TODO: Load skill abilities :3

        return skill;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(@NotNull Boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull Map<String, SkillTask<?>> getTasks() {
        return tasks;
    }

    public @NotNull String getFormula() {
        return formula;
    }

    public void setFormula(@NotNull String formula) {
        this.formula = formula;
    }

    public @Nullable Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(@Nullable Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public @Nullable ConfigurationSection getConfig() {
        return config;
    }

    public void setConfig(@Nullable ConfigurationSection config) {
        this.config = config;
    }

    public @Nullable File getFile() {
        return file;
    }

    public void setFile(@Nullable File file) {
        this.file = file;
    }
}
