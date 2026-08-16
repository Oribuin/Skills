package dev.oribuin.skills.skill.task;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.skill.task.type.TaskType;
import dev.oribuin.skills.skill.task.type.TaskTypes;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SkillTask<T> {

    private final @NotNull String action; // place, break, etc. Use TaskTypes#from(String) 
    private @NotNull List<Material> requiredTools; // TODO: Use custom items
    private @NotNull Map<Object, TaskTarget<T>> targets;
    private TaskType<T, ?> type;
    private boolean recursive;

    @SuppressWarnings("unchecked")
    public SkillTask(@NotNull String action) {
        this.action = action;
        this.requiredTools = new ArrayList<>();
        this.targets = new HashMap<>();
        this.type = (TaskType<T, ?>) TaskTypes.from(action); // load the task type from the action
        this.recursive = false; // really only applies to farming stuff where they're breaking chain blocks
    }

    /**
     * Load the tasks to progress the skill in level and money
     *
     * @param section The section to gain
     * @param key     The
     * @return Skill task if available
     */
    @SuppressWarnings("unchecked")
    public static <T> @Nullable SkillTask<T> from(@NotNull ConfigurationSection section, @NotNull String key) {
        SkillTask<T> task = new SkillTask<>(key.toLowerCase());
        if (task.getType() == null) {
            SkillsPlugin.getInstance().getLogger().severe("Failed to load taskType[" + key + "]");
            return null;
        }

        // load all the targets
        ConfigurationSection actionSection = section.getConfigurationSection("actions");
        if (actionSection == null) {
            SkillsPlugin.getInstance().getLogger().severe("Failed to load any actions for task[ " + key + "]");
            return null;
        }

        for (String targetKey : actionSection.getKeys(false)) {
            ConfigurationSection keySection = actionSection.getConfigurationSection(targetKey);
            if (keySection == null) continue;

            TaskTarget<T> target = TaskTarget.from(keySection, task, targetKey);
            if (target == null) continue;

            task.getTargets().put(target.getValue(), target);
        }

        List<Material> requiredTools = section.getStringList("required-tools")
                .stream()
                .map(Material::getMaterial)
                .filter(Objects::nonNull)
                .toList();

        task.setRecursive(section.getBoolean("recursive", false));
        task.setRequiredTools(requiredTools);
        return task;
    }

    /**
     * Check if an event is the same as the skill task
     *
     * @param event The event to provide
     * @return true if they match
     */
    public boolean checkEvent(Event event) {
        return this.type.getEventClass().equals(event.getClass());
    }

    @Override
    public String toString() {
        return "SkillTask{" +
                "action='" + action + '\'' +
                ", requiredTools=" + requiredTools +
                ", targets=" + targets +
                ", type=" + type +
                ", recursive=" + recursive +
                '}';
    }

    public @NotNull String getAction() {
        return action;
    }

    public @NotNull List<Material> getRequiredTools() {
        return requiredTools;
    }

    public void setRequiredTools(@NotNull List<Material> requiredTools) {
        this.requiredTools = requiredTools;
    }

    public @NotNull Map<Object, TaskTarget<T>> getTargets() {
        return targets;
    }

    public void setTargets(@NotNull Map<Object, TaskTarget<T>> targets) {
        this.targets = targets;
    }

    public TaskType<?, ?> getType() {
        return type;
    }

    public void setType(TaskType<T, ?> type) {
        this.type = type;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

}
