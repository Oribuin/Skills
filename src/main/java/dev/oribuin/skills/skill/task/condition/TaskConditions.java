package dev.oribuin.skills.skill.task.condition;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.config.impl.Config;
import dev.oribuin.skills.skill.task.condition.block.BiomeCondition;
import dev.oribuin.skills.skill.task.condition.block.FurnaceCondition;
import dev.oribuin.skills.skill.task.condition.block.GrownCropCondition;
import dev.oribuin.skills.skill.task.condition.block.HiveCondition;
import dev.oribuin.skills.skill.task.condition.block.HydratedCondition;
import dev.oribuin.skills.skill.task.condition.block.LightLevelCondition;
import dev.oribuin.skills.skill.task.condition.block.OreCondition;
import dev.oribuin.skills.skill.task.condition.block.PoweredCondition;
import dev.oribuin.skills.skill.task.condition.block.SpawnerCondition;
import dev.oribuin.skills.skill.task.condition.block.WaterloggedCondition;
import dev.oribuin.skills.skill.task.condition.types.TypeCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static dev.oribuin.skills.skill.task.condition.ConditionType.BLOCK;

public class TaskConditions {

    private final static Map<ConditionType, List<ConditionSupplier<?>>> CONDITIONS = new HashMap<>();
    
    public static void init() {
        CONDITIONS.clear();
        
        // region Block Condition
        register(BLOCK, BiomeCondition::new);
        register(BLOCK, FurnaceCondition::new);
        register(BLOCK, GrownCropCondition::new);
        register(BLOCK, HiveCondition::new);
        register(BLOCK, HydratedCondition::new);
        register(BLOCK, LightLevelCondition::new);
        register(BLOCK, OreCondition::new);
        register(BLOCK, PoweredCondition::new);
        register(BLOCK, SpawnerCondition::new);
        register(BLOCK, WaterloggedCondition::new);
        // endregion
    }

    /**
     * Register a type condition into the plugin
     *
     * @param supplier The condition supplier
     * @param <T>      The type condition
     */
    public static <T extends TypeCondition<?>> void register(
            @NotNull ConditionType type,
            @NotNull Function<ConfigurationSection, T> supplier
    ) {
        List<ConditionSupplier<?>> suppliers = CONDITIONS.getOrDefault(type, new ArrayList<>());
        suppliers.add(new ConditionSupplier<>(supplier));
        CONDITIONS.put(type, suppliers);
    }

    /**
     * Load all the conditions for the specified task type
     *
     * @param section The section to load from
     * @return All conditions available
     */
    @NotNull
    public static List<TypeCondition<?>> from(@Nullable ConfigurationSection section) {
        List<TypeCondition<?>> applicable = new ArrayList<>();
        if (section == null || section.getKeys(false).isEmpty()) return applicable; // empty 
        System.out.println("Checking each condition values");
        System.out.println("Total Conditions to check from: " + CONDITIONS.size());

        CONDITIONS.values().forEach(x -> x.forEach(supplier -> {
            TypeCondition<?> condition = supplier.create(section);
            try {
                System.out.println("Checking if condition[" + condition.getClass().getSimpleName() + "] is applicable: " + condition.isApplicable());
                if (condition.isApplicable()) applicable.add(condition);
            } catch (NullPointerException ex) {
                SkillsPlugin.getInstance().getLogger().warning("Condition[" + condition.getClass().getSimpleName() + "] has encountered an error while checking if applicable: " + ex.getMessage());
            }
        }));

        return applicable;
    }

}
