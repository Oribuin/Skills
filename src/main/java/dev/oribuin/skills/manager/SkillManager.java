package dev.oribuin.skills.manager;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.Skill;
import dev.oribuin.skills.skill.task.condition.TaskConditions;
import dev.oribuin.skills.util.SkillUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SkillManager implements Manager {

    private final Map<String, Skill> skills = new HashMap<>();

    /**
     * The task that runs when the plugin is loaded/reloaded
     *
     * @param plugin The plugin reloading
     */
    @Override
    public void reload(SkillsPlugin plugin) {
        this.skills.clear();
        TaskConditions.init();

        File skillsFolder = new File(plugin.getDataFolder(), "skills");
        File[] content = skillsFolder.listFiles();

        // Create the default files in the plugin
        if (content == null) {
            SkillUtils.createFile(plugin, "skills", "farming.yml");
            content = skillsFolder.listFiles(); // whatever
        }

        if (content == null) return; // still empty :(

        for (File file : content) {
            if (file.isDirectory()) return; // don't search subdirs
            if (!file.getName().endsWith(".yml")) return; // not a yml file

            plugin.getLogger().info("Loading skill from file: " + file.getName());
            FileConfiguration skillConfig = YamlConfiguration.loadConfiguration(file);
            Skill skill = Skill.from(file, skillConfig);
            if (skill == null) {
                plugin.getLogger().warning("Failed to load config from file: " + file.getName());
                continue;
            }

            this.skills.put(skill.getName().toLowerCase(), skill);
        }
    }

    /**
     * The task that runs when the plugin is disabled, usually takes priority over {@link Manager#reload(SkillsPlugin)}
     *
     * @param plugin The plugin being disabled
     */
    @Override
    public void disable(SkillsPlugin plugin) {

    }

    public Map<String, Skill> getSkills() {
        return skills;
    }
    
}
