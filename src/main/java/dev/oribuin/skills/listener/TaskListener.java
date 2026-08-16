package dev.oribuin.skills.listener;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.task.TaskProcessor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;

public class TaskListener implements Listener {

    private final SkillsPlugin plugin;

    public TaskListener(SkillsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        TaskProcessor.create(event, block, 1)
                .player(player)
                .heldItem(player.getInventory().getItemInMainHand())
                .execute(this.plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        TaskProcessor.create(event, block, 1)
                .player(player)
                .heldItem(player.getInventory().getItemInMainHand())
                .execute(this.plugin);
    }

}
