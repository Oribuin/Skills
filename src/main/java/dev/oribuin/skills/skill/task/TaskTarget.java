package dev.oribuin.skills.skill.task;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.task.condition.TaskConditions;
import dev.oribuin.skills.skill.task.condition.types.TypeCondition;
import dev.oribuin.skills.skill.task.type.TaskType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTarget<T> {

    private static final Logger log = LoggerFactory.getLogger(TaskTarget.class);
    private final T value;
    private final double experience;
    private final double money;
    private List<TypeCondition<T>> conditions;

    /**
     * Create a new task target, this is basically the container for all the materials/entities etc that the skill will use
     * to level up as well as how much exp/money it provides
     */
    public TaskTarget(T value, double experience, double money) {
        this.value = value;
        this.experience = experience;
        this.money = money;
        this.conditions = new ArrayList<>();
    }

    /**
     * Get the value for the target
     *
     * @return The value
     */
    public T getValue() {
        return value;
    }

    /**
     * Get the experience given for the target
     *
     * @return The amount of experience or null
     */
    public Double getExperience() {
        return this.experience;
    }

    /**
     * Get the money given for the target
     *
     * @return The amount of money or null
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * Get the conditions listed for the target
     *
     * @return The condition to load
     */
    public List<TypeCondition<T>> getConditions() {
        return conditions;
    }

    /**
     * Set the predefined conditions for this target
     *
     * @param conditions the conditions to apply
     */
    public void setConditions(List<TypeCondition<T>> conditions) {
        this.conditions = conditions;
    }

    /**
     * Load a task action from a target
     *
     * @param section The section to load it from
     * @param task    The task to get
     * @param <T>     The task type associated value
     * @return The task targets if available (material, entity, etc)
     */
    @SuppressWarnings("unchecked")
    public static <T> @Nullable TaskTarget<T> from(
            @NotNull ConfigurationSection section,
            @NotNull SkillTask<T> task,
            @NotNull String key
    ) {
        // e.g. T = Block, key = Block Material Id
        TaskType<T, ?> taskType = (TaskType<T, ?>) task.getType();
        T value = (T) taskType.getLoader().from(key);
        if (value == null) {
            SkillsPlugin.getInstance().getLogger().severe("taskTarget value[" + key + "] does not exist, are you sure it is correct?");
            return null;
        }

        double experience = section.getDouble("experience", 0);
        double money = section.getDouble("money", 0);
        TaskTarget<T> taskTarget = new TaskTarget<>(value, experience, money);

        List<TypeCondition<T>> typeConditions = new ArrayList<>();

        // Load conditions for the tag
        ConfigurationSection conditionSection = section.getConfigurationSection("conditions");
        if (conditionSection != null) {
            List<TypeCondition<?>> loaded = TaskConditions.from(conditionSection);
            for (TypeCondition<?> loadedCondition : loaded) {
                typeConditions.add((TypeCondition<T>) loadedCondition);
            }

            taskTarget.setConditions(typeConditions);
            String availableConditions = typeConditions.stream()
                    .map(x -> x.getClass().getSimpleName())
                    .collect(Collectors.joining(", "));

            SkillsPlugin.getInstance().getLogger().warning("taskTarget[" + key + "] conditions are [" + availableConditions + "]");
        }

        if (experience <= 0 && money <= 0) {
            SkillsPlugin.getInstance().getLogger().warning("taskTarget[" + key + "] provide no rewards for the user");
        }

        return taskTarget;
    }

    @Override
    public String toString() {
        return "TaskTarget{" +
                "value=" + value +
                ", experience=" + experience +
                ", money=" + money +
                ", conditions=" + conditions +
                '}';
    }
}
