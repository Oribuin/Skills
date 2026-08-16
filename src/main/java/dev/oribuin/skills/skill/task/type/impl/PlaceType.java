package dev.oribuin.skills.skill.task.type.impl;

import dev.oribuin.skills.skill.task.TaskLoader;
import dev.oribuin.skills.skill.task.type.TaskType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class PlaceType extends TaskType<Block, BlockBreakEvent> {

    /**
     * Create a new task type for the plugin
     */
    public PlaceType() {
        super("place", Block.class, BlockBreakEvent.class);
        this.loader = new TaskLoader<>(Material::getMaterial); // TODO: Material Tags
    }

    /**
     * Check if the value from the event matches the task type
     *
     * @param event The event to check
     * @param value The value to check
     * @return True if the two values match each other
     */
    @Override
    public boolean attempt(BlockBreakEvent event, Block value) {
        return event.getBlock().getType().equals(value.getType());
    }
    
    /**
     * Convert the value into the value stored (block -> material, etc)
     *
     * @param object@return The resulting value
     */
    @Override
    public Object asGeneric(Object object) {
        return object instanceof Block block ? block.getType() : object;
    }
    
}
