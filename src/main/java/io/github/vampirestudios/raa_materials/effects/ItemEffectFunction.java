package io.github.vampirestudios.raa_materials.effects;

import com.google.gson.JsonElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemEffectFunction {
    void apply(ServerWorld world, LivingEntity target, LivingEntity attacker, JsonElement config);
}
