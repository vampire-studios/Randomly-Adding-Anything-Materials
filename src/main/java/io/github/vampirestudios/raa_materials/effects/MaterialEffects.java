package io.github.vampirestudios.raa_materials.effects;

import com.google.gson.JsonElement;
import io.github.vampirestudios.raa_materials.config.GeneralConfig;
import io.github.vampirestudios.raa_materials.utils.Utils;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.function.Consumer;

public enum MaterialEffects {
    LIGHTNING(ItemEffectHandler::spawnLightning, (element -> {
        element.getAsJsonObject().addProperty("chance", Rands.randIntRange(2, 8));
    })),
    EFFECT(ItemEffectHandler::statusEffectForTarget, (element -> {
        String effectID = "";
        if (GeneralConfig.useOnlyVanillaPotionEffects.getValue()) {
            while (!new Identifier(effectID).getNamespace().equals("minecraft")) {
                effectID = Rands.list(new ArrayList<>(Registry.STATUS_EFFECT.getIds())).toString();
            }
        } else {
            while (isInBlackList(effectID)) {
                effectID = Rands.list(new ArrayList<>(Registry.STATUS_EFFECT.getIds())).toString();
            }
        }
        if (effectID.equals("")) {
            if (GeneralConfig.useOnlyVanillaPotionEffects.getValue()) {
                while (!new Identifier(effectID).getNamespace().equals("minecraft")) {
                    effectID = Rands.list(new ArrayList<>(Registry.STATUS_EFFECT.getIds())).toString();
                }
            } else {
                while (isInBlackList(effectID)) {
                    effectID = Rands.list(new ArrayList<>(Registry.STATUS_EFFECT.getIds())).toString();
                }
            }
        }
        element.getAsJsonObject().addProperty("type", effectID);
        element.getAsJsonObject().addProperty("duration", Rands.randIntRange(5, 15));
        element.getAsJsonObject().addProperty("amplifier", Rands.randIntRange(0, 2));
    })),
    FIREBALL(ItemEffectHandler::spawnFireball, (element -> {
        element.getAsJsonObject().addProperty("chance", Rands.randIntRange(2, 8));
    })),
    FREEZE(ItemEffectHandler::stopEntity, (element -> {
        element.getAsJsonObject().addProperty("duration", Rands.randIntRange(5, 20));
    })),
    BURN(ItemEffectHandler::burnEntity, (element -> {
        element.getAsJsonObject().addProperty("seconds", Rands.randIntRange(5, 20));
    })),
    KNOCKBACK(ItemEffectHandler::knockbackEntity, (element -> {
        element.getAsJsonObject().addProperty("speed", Rands.randFloatRange(0.1F, 0.7F));
        element.getAsJsonObject().addProperty("xMovement", Utils.randDoubleRange(1.0D, 1.6D));
        element.getAsJsonObject().addProperty("zMovement", Utils.randDoubleRange(1.0D, 1.6D));
    }));

    public ItemEffectFunction function;
    public Consumer<JsonElement> jsonConsumer;

    MaterialEffects(ItemEffectFunction function, Consumer<JsonElement> jsonConsumer) {
        this.function = function;
        this.jsonConsumer = jsonConsumer;
    }

    public void apply(ServerWorld world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        this.function.apply(world, target, attacker, config);
    }

    private static boolean isInBlackList(String id) {
        for (String string : GeneralConfig.blacklistedPotionEffects.getValue())
            if (string.equals(id)) return true;
        return false;
    }
}
