package dev.oribuin.skills.config.serializer;

import dev.oribuin.skills.util.SkillUtils;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class ComponentSerializer extends ScalarSerializer<Component> {

    private static final ComponentSerializer INSTANCE = new ComponentSerializer();

    private ComponentSerializer() {
        super(Component.class);
    }

    @Override
    public Component deserialize(Type type, Object obj) throws SerializationException {
        return SkillUtils.LEGACY_SERIALIZER.deserialize(obj.toString());
    }

    @Override
    protected Object serialize(Component item, Predicate<Class<?>> typeSupported) {
        return SkillUtils.MINIMESSAGE.serialize(item);
    }

    public static ComponentSerializer getInstance() {
        return INSTANCE;
    }


}
