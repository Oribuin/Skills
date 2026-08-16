package dev.oribuin.skills.config.impl;

import dev.oribuin.skills.SkillsPlugin;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
public class Config {

    public static Config get() {
        return SkillsPlugin.getInstance().getConfigLoader().get(Config.class);
    }

}
