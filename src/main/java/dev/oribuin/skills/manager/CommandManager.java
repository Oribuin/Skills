package dev.oribuin.skills.manager;

import dev.oribuin.skills.SkillsPlugin;
import dev.oribuin.skills.config.impl.PluginMessages;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.setting.Configurable;
import org.incendo.cloud.setting.ManagerSetting;

import javax.naming.NoPermissionException;
import java.util.function.Supplier;

public class CommandManager extends LegacyPaperCommandManager<CommandSender> implements Manager {

    private final SkillsPlugin plugin;
    private final AnnotationParser<CommandSender> parser;

    public CommandManager(@NonNull SkillsPlugin owningPlugin) {
        super(
                owningPlugin,
                ExecutionCoordinator.asyncCoordinator(),
                SenderMapper.identity()
        );

        this.plugin = owningPlugin;
        // Register the command manager
        Configurable<ManagerSetting> commandSettings = this.settings();
        commandSettings.set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true);
        commandSettings.set(ManagerSetting.OVERRIDE_EXISTING_COMMANDS, true);

        // Register argument parser
        this.parser = new AnnotationParser<>(this, CommandSender.class);

        // Register capabilities
        if (this.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            this.registerBrigadier();
        } else if (this.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        this.exceptionController()
                .registerHandler(NoPermissionException.class, x -> PluginMessages.get()
                        .getNoPermission().send(x.context().sender())
                )
                .registerHandler(InvalidSyntaxException.class, x -> PluginMessages.get()
                        .getInvalidSyntax().send(x.context().sender(), "syntax", x.exception().correctSyntax())
                )
                .registerHandler(InvalidCommandSenderException.class, x -> PluginMessages.get()
                        .getRequirePlayer().send(x.context().sender(), "sender", x.context().sender().getName())
                );

        // Register additional stuff down here :3
        
        // Register all the plugin commands
//        this.parser.parse();
    }

    /**
     * The task that runs when the plugin is loaded/reloaded
     *
     * @param plugin The plugin reloading
     */
    @Override
    public void reload(SkillsPlugin plugin) {
    }

    /**
     * The task that runs when the plugin is disabled, usually takes priority over {@link Manager#reload(FishingPlugin)}
     *
     * @param plugin The plugin being disabled
     */
    @Override
    public void disable(SkillsPlugin plugin) {
    }

    /**
     * Register a command parser into the plugin command manager
     *
     * @param type   The type class being parsed
     * @param parser The argument parser
     * @param <T>    The type being parsed
     */
    public <T> void registerParser(Class<T> type, Supplier<ArgumentParser<CommandSender, T>> parser) {
        ParserDescriptor<CommandSender, T> descriptor = ParserDescriptor.of(parser.get(), type);
        this.parserRegistry().registerParser(descriptor);
    }
    
    public AnnotationParser<CommandSender> getParser() {
        return parser;
    }
    
}
