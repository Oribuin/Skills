package dev.oribuin.skills.skill.task.type.impl;

import dev.oribuin.skills.skill.task.TaskLoader;
import dev.oribuin.skills.skill.task.type.TaskType;
import dev.oribuin.skills.util.SkillUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageDealtType extends TaskType<Entity, EntityDamageByEntityEvent> {

    /**
     * Create a new task type for the plugin
     */
    public DamageDealtType() {
        super("damage_dealt", Entity.class, EntityDamageByEntityEvent.class);
        this.loader = new TaskLoader<>(s -> SkillUtils.getEnum(EntityType.class, s));
    }

    /**
     * Check if the value from the event matches the task type
     *
     * @param event The event to check
     * @param value The value to check
     * @return True if the two values match each other
     */
    @Override
    public boolean attempt(EntityDamageByEntityEvent event, Entity value) {
        return event.getEntityType().equals(value.getType());
    }

    /**
     * Convert the value into the value stored (block -> material, etc)
     *
     * @param object@return The resulting value
     */
    @Override
    public Object asGeneric(Object object) {
        return object instanceof Entity entity ? entity.getType() : object;
    }

}
