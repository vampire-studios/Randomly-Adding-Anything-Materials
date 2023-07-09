package io.github.vampirestudios.raa_materials.items.effects;

import com.google.gson.JsonElement;
import io.github.vampirestudios.raa_materials.utils.Rands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.function.Consumer;

public enum MaterialEffects {
    LIGHTNING(ItemEffectHandler::spawnLightning, (element -> element.getAsJsonObject().addProperty("chance", Rands.randIntRange(2, 8)))),
    EFFECT(ItemEffectHandler::statusEffectForTarget, (element -> {
        element.getAsJsonObject().addProperty("type", Rands.list(new ArrayList<>(BuiltInRegistries.MOB_EFFECT.keySet())).toString());
        element.getAsJsonObject().addProperty("duration", Rands.randIntRange(5, 15));
        element.getAsJsonObject().addProperty("amplifier", Rands.randIntRange(0, 2));
    })),
    FIREBALL(ItemEffectHandler::spawnFireball, (element -> element.getAsJsonObject().addProperty("chance", Rands.randIntRange(2, 8)))),
    FREEZE(ItemEffectHandler::stopEntity, (element -> element.getAsJsonObject().addProperty("duration", Rands.randIntRange(5, 20)))),
    BURN(ItemEffectHandler::burnEntity, (element -> element.getAsJsonObject().addProperty("seconds", Rands.randIntRange(5, 20)))),
    KNOCKBACK(ItemEffectHandler::knockbackEntity, (element -> {
        element.getAsJsonObject().addProperty("speed", Rands.randFloatRange(0.1F, 0.7F));
        element.getAsJsonObject().addProperty("xMovement", Rands.randDoubleRange(1.0D, 1.6D));
        element.getAsJsonObject().addProperty("zMovement", Rands.randDoubleRange(1.0D, 1.6D));
    }));

    public final ItemEffectFunction function;
    public final Consumer<JsonElement> jsonConsumer;

    MaterialEffects(ItemEffectFunction function, Consumer<JsonElement> jsonConsumer) {
        this.function = function;
        this.jsonConsumer = jsonConsumer;
    }

    public void apply(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        this.function.apply(world, target, attacker, config);
    }
}