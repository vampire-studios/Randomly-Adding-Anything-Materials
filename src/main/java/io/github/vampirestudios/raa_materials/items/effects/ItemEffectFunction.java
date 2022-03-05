package io.github.vampirestudios.raa_materials.items.effects;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ItemEffectFunction {
    void apply(Level world, LivingEntity target, LivingEntity attacker, JsonElement config);
}