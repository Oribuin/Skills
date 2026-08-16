package dev.oribuin.skills.config.impl;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.config.TextMessage;
import net.kyori.adventure.sound.Sound;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
public class PluginMessages {

    public static final String PREFIX = "<#94bc80><b>Skills</b> <gray>| <white>";

    public static PluginMessages get() {
        return SkillsPlugin.getInstance().getConfigLoader().get(PluginMessages.class);
    }

    @Comment("The message sent when a user reloads the plugin")
    private TextMessage reload = new TextMessage(PREFIX + "You have reloaded the plugin in <#93bc80><time><white>ms");

    @Comment("The message sent when a player does not have permission to do something.")
    private TextMessage noPermission = new TextMessage(PREFIX + "You do not have permission to do this");

    @Comment("The message sent when a player does not have permission to do something.")
    private TextMessage requirePlayer = new TextMessage(PREFIX + "You need to be sender type of <#94bc80><sender><white> to run this command");

    @Comment("The message sent when a player gets the syntax for a message wrong")
    private TextMessage invalidSyntax = new TextMessage(PREFIX + "You have provided invalid syntax. The correct usage is: <#94bc80><syntax>");

    @Comment("The target has a full inventory")
    private TextMessage fullInventory = new TextMessage(PREFIX + "The target you have provided has a full inventory.");
    
    public TextMessage getReload() {
        return reload;
    }

    public TextMessage getNoPermission() {
        return noPermission;
    }

    public TextMessage getRequirePlayer() {
        return requirePlayer;
    }

    public TextMessage getInvalidSyntax() {
        return invalidSyntax;
    }

    public TextMessage getFullInventory() {
        return fullInventory;
    }

}
