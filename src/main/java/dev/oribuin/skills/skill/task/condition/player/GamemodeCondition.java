package dev.oribuin.skills.skill.task.condition.player;

import dev.oribuin.skills.skill.task.condition.types.PlayerCondition;
import dev.oribuin.skills.util.SkillUtils;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class GamemodeCondition extends PlayerCondition {

    private List<GameMode> gameModes;
    
    /**
     * Creates a new player condition to register into the plugin
     *
     * @param section The section to load
     */
    public GamemodeCondition(@NotNull ConfigurationSection section) {
        super(section);
        this.gameModes = SkillUtils.getEnumList(GameMode.class, section.getStringList("game-modes"));
    }

    /**
     * Check to see if the condition can apply to the value provided
     *
     * @param executor The person running the task
     * @param target   The type to check for
     * @return true if the condition passes
     */
    @Override
    public boolean test(@NotNull Player executor, @NonNull Player target) {
        return this.gameModes.contains(target.getGameMode());
    }

    /**
     * Check whether the condition can apply to the skill
     *
     * @return Whether the condition should apply
     */
    @Override
    public boolean isApplicable() {
        return this.gameModes.isEmpty();
    }
}
