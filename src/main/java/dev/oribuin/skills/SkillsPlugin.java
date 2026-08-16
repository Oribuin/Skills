package dev.oribuin.skills;

import dev.oribuin.skills.config.ConfigLoader;
import dev.oribuin.skills.config.impl.Config;
import dev.oribuin.skills.config.impl.MySQLConfig;
import dev.oribuin.skills.config.impl.PluginMessages;
import dev.oribuin.skills.listener.TaskListener;
import dev.oribuin.skills.manager.CommandManager;
import dev.oribuin.skills.manager.DataManager;
import dev.oribuin.skills.manager.SkillManager;
import dev.oribuin.skills.util.model.Placeholder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillsPlugin extends JavaPlugin {

    private static SkillsPlugin instance;
    private ConfigLoader configLoader;
    private CommandManager commandManager;
    private DataManager dataManager;
    private SkillManager skillManager;

    @Override
    public void onEnable() {
        instance = this;

        // Load this plugin configs
        this.configLoader = new ConfigLoader();
        this.configLoader.loadConfig(Config.class, "settings");
        this.configLoader.loadConfig(PluginMessages.class, "messages");
        this.configLoader.loadConfig(MySQLConfig.class, "database-settings");

        // Load the plugin managers
        this.commandManager = new CommandManager(this);
        this.dataManager = new DataManager(this);
        this.skillManager = new SkillManager();

        this.reload();

        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new TaskListener(this), this);

        // register plugin hooks
//        if (HeadDbProvider.isEnabled()) manager.registerEvents(new HeadDbProvider(), this);
//        if (PAPIProvider.isEnabled()) new PAPIProvider(this).register();
    }

    public void reload() {
        this.configLoader.reload();
        this.dataManager.reload(this);
        this.commandManager.reload(this);
        this.skillManager.reload(this);
    }

    public static SkillsPlugin getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

}
