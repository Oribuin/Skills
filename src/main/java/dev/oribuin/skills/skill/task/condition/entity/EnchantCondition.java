package dev.oribuin.skills.skill.task.condition.entity;

import dev.oribuin.skills.config.ConfigBoolean;
import dev.oribuin.skills.skill.task.condition.types.BlockCondition;
import dev.oribuin.skills.skill.task.condition.types.EntityCondition;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class EnchantCondition extends EntityCondition {

    private final ConfigBoolean silkTouch;

    /**
     * Creates a new block condition to register into the plugin
     *
     * @param section The section to load
     */
    public EnchantCondition(@NotNull ConfigurationSection section) {
        super(section);
        this.silkTouch = ConfigBoolean.check(section, "silk-touch");
    }

    /**
     * Check to see if the condition can apply to the value provided
     *
     * @param executor The person running the task
     * @param target   The type to check for
     * @return true if the condition passes
     */
    @Override
    public boolean test(@NotNull Player executor, @NonNull Entity target) {
        executor.getEquipment();
    }

    /**
     * Check to see if the condition can apply to the value provided
     *
     * @param executor The person running the task
     * @param target   The type to check for
     * @return true if the condition passes
     */
    @Override
    public boolean test(@NotNull Player executor, @NonNull Block target) {
        ItemStack itemStack = executor.getInventory().getItemInMainHand();
        return this.silkTouch.check(itemStack.containsEnchantment(Enchantment.SILK_TOUCH));
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.silkTouch.applicable();
    }

}
