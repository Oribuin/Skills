package dev.oribuin.skills.manager;

import dev.oribuin.skills.SkillsPlugin;

public interface Manager {

    /**
     * The task that runs when the plugin is loaded/reloaded
     *
     * @param plugin The plugin reloading
     */
    void reload(SkillsPlugin plugin);

    /**
     * The task that runs when the plugin is disabled, usually takes priority over {@link Manager#reload(SkillsPlugin)}
     *
     * @param plugin The plugin being disabled
     */
    void disable(SkillsPlugin plugin);

}
