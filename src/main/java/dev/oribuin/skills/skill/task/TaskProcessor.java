package dev.oribuin.skills.skill.task;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.Skill;
import dev.oribuin.skills.skill.task.condition.types.TypeCondition;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * maybe i dont use this
 */
public class TaskProcessor<T> {

    private @NotNull Event event; // The event being utilised
    private @NotNull T value; // Material, Entity Type, ItemStack, whatever 
    private @Nullable Player player; // the player who did the action
    private @Nullable ItemStack heldItem;
    private int amount; // the amount of times it was done

    /**
     * The task to process for the player
     *
     * @param event  The associated event
     * @param value  The value that was modified
     * @param amount The amount of times it was done
     */
    private TaskProcessor(@NotNull Event event, @NotNull T value, int amount) {
        this.event = event;
        this.value = value;
        this.amount = amount;
        this.player = event instanceof PlayerEvent playerEvent ? playerEvent.getPlayer() : null;
        this.heldItem = player != null ? player.getInventory().getItemInMainHand() : null;
    }

    /**
     * Create a new task processor for the plugin
     *
     * @param event  The event task
     * @param value  The value that was provided
     * @param amount The amount of times it was done
     * @return The associated task processor
     */
    public static <T> TaskProcessor<T> create(@NotNull Event event, @NotNull T value, int amount) {
        return new TaskProcessor<>(event, value, amount);
    }

    /**
     * Execute the task processor for all the available tasks
     *
     * @param plugin The plugin to run the tasks
     */
    public void execute(SkillsPlugin plugin) {
        plugin.getSkillManager().getSkills().values()
                .stream()
                .filter(Skill::isEnabled)
//                .filter(skill -> {
//                    // Check if player has levelled the skill
//                    return true;
//                })
                .forEach(skill -> skill.getTasks().values().forEach(this::executeTask));
    }

    /**
     * Execute the task for the skill task(s)
     *
     * @param task the task to run
     */
    @SuppressWarnings("unchecked")
    public void executeTask(SkillTask<?> task) {
        if (this.player == null) return; // no player or value provided
        if (this.amount <= 0) return; // invalid amount
        if (!task.checkEvent(this.event)) return; // incompatible event
        if (!task.getRequiredTools().isEmpty() && this.heldItem == null) return; // no tool when one is required

        Object asGeneric = task.getType().asGeneric(this.value);
        TaskTarget<?> target = task.getTargets().get(asGeneric);
        if (target == null) return;

        if (!target.getConditions().isEmpty()) {
            for (TypeCondition<?> condition : target.getConditions()) {
                TypeCondition<T> typeCondition = (TypeCondition<T>) condition;
                boolean result = typeCondition.test(this.player, this.value);
                this.player.sendMessage("Condition[" + typeCondition.getClass().getSimpleName() + "] being tested: " + result);
                if (!result) return;
            }
        }

        this.player.sendMessage("Action[" + task.getType().getName() + "] with value[" + asGeneric + "] gave you [" + target.getExperience() + "] xp and [" + target.getMoney() + "] money!");
    }

    @Override
    public String toString() {
        return "TaskProcessor{" +
                "event=" + event +
                ", value=" + value +
                ", player=" + player +
                ", heldItem=" + heldItem +
                ", amount=" + amount +
                '}';
    }

    public @NotNull Object value() {
        return value;
    }

    public TaskProcessor<T> value(@NotNull T value) {
        this.value = value;
        return this;
    }

    public @NotNull Event event() {
        return event;
    }

    public TaskProcessor<T> event(@NotNull Event event) {
        this.event = event;
        return this;
    }

    public @Nullable Player getPlayer() {
        return player;
    }

    public TaskProcessor<T> player(@Nullable Player player) {
        this.player = player;
        return this;
    }

    public @Nullable ItemStack heldItem() {
        return heldItem;
    }

    public TaskProcessor<T> heldItem(@Nullable ItemStack heldItem) {
        this.heldItem = heldItem;
        return this;
    }

    public int amount() {
        return amount;
    }

    public TaskProcessor<T> amount(int amount) {
        this.amount = amount;
        return this;
    }
}
