package dev.oribuin.skills.manager;

import com.google.gson.Gson;
import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.config.impl.MySQLConfig;
import dev.oribuin.skills.database.connector.DatabaseConnector;
import dev.oribuin.skills.database.connector.MySQLConnector;
import dev.oribuin.skills.database.connector.SQLiteConnector;
import dev.oribuin.skills.scheduler.PluginScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DataManager implements Manager {
    
    private static final Gson GSON = new Gson();

    private final SkillsPlugin plugin;
    private DatabaseConnector connector;

    public DataManager(SkillsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * The task that runs when the plugin is loaded/reloaded
     *
     * @param plugin The plugin reloading
     */
    public void reload(SkillsPlugin plugin) {
        this.disable(plugin);

        MySQLConfig sqlConfig = MySQLConfig.get();
        if (sqlConfig.isEnabled()) {
            String hostname = sqlConfig.getHostname();
            int port = sqlConfig.getPort();
            String database = sqlConfig.getDatabaseName();
            String username = sqlConfig.getUsername();
            String password = sqlConfig.getPassword();
            boolean useSSL = sqlConfig.useSSL();
            int poolSize = sqlConfig.getConnectionPoolSize();

            this.connector = new MySQLConnector(this.plugin, hostname, port, database, username, password, useSSL, poolSize);
            this.plugin.getLogger().info("Data manager connected using MySQL.");
        } else {
            this.connector = new SQLiteConnector(this.plugin);
            this.connector.cleanup();
            this.plugin.getLogger().info("Data manager connected using SQLite.");
        }

        // Create the initial table for the plugin
        this.connector.connect(connection -> {
            try (Statement statement = connection.createStatement()) {
//                statement.addBatch(CREATE_TABLE_USERS);
//                statement.addBatch(CREATE_TABLE_TOTEMS);
                statement.executeBatch();
            }
        });

//        this.saveBatch(this.userData.values());
//        this.userData.clear(); // Clear the map

        // Load all the users who are currently online
//        Collection<UUID> uuids = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).toList();
//        this.loadBatch(uuids);

        // Load all the totems in the plugin 

    }

    /**
     * The task that runs when the plugin is disabled, usually takes priority over {@link Manager#reload(SkillsPlugin)}
     *
     * @param plugin The plugin being disabled
     */
    public void disable(SkillsPlugin plugin) {
        if (this.connector != null) {
            // Wait for all connections to finish
            long now = System.currentTimeMillis();
            long deadline = now + 5000;
            synchronized (this.connector.getLock()) {
                while (!this.connector.isFinished() && now < deadline) {
                    try {
                        this.connector.getLock().wait(deadline - now);
                        now = System.currentTimeMillis();
                    } catch (InterruptedException ex) {
                        this.plugin.getLogger().severe("Interrupted error occurred: " + ex.getMessage());
                    }
                }
            }

            this.connector.closeConnection();
        }
    }
    
    /**
     * Run a task asynchronously
     *
     * @param runnable The task to run
     */
    public void async(Runnable runnable) {
        PluginScheduler.get().runTaskAsync(runnable);
    }

    // region SQL Queries
    // endregion
}
