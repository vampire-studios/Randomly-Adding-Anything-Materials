package io.github.vampirestudios.raa_materials.items.effects;

import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ItemEffectHandler {
    public static void spawnLightning(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (world.getRandom().nextInt(config.getAsJsonObject().get("chance").getAsInt()) == 0) {
            if (!world.isClientSide()) {
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
                assert lightningBolt != null;
                lightningBolt.moveTo(target.blockPosition().getX(), target.blockPosition().getZ(), target.blockPosition().getZ());
                world.addFreshEntity(lightningBolt);
            }
        }
    }

    public static void statusEffectForTarget(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (!world.isClientSide()) {
            target.addEffect(new MobEffectInstance(
                    Objects.requireNonNull(Registry.MOB_EFFECT.get(new ResourceLocation(config.getAsJsonObject().get("type").getAsString()))),
                    config.getAsJsonObject().get("duration").getAsInt(),
                    config.getAsJsonObject().get("amplifier").getAsInt()));
        }
    }

    public static void spawnFireball(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (world.getRandom().nextInt(config.getAsJsonObject().get("chance").getAsInt()) == 0) {
            if (!world.isClientSide()) {
                Vec3 vec3d = attacker.getViewVector(1.0F);
                double f = target.getX() - (attacker.getX() + vec3d.x * 4.0D);
                double g = target.getY(0.5D) - (0.5D + attacker.getY(0.5D));
                double h = target.getZ() - (attacker.getZ() + vec3d.z * 4.0D);

                SmallFireball fireballEntity = new SmallFireball(world, f, g, h, 1D, 1D, 1D);
                world.addFreshEntity(fireballEntity);
            }
        }
    }

    public static void stopEntity(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (!world.isClientSide()) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, config.getAsJsonObject().get("duration").getAsInt(), 127, false, false, false));
        }
    }

    public static void burnEntity(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (!world.isClientSide()) {
            target.setSecondsOnFire(config.getAsJsonObject().get("seconds").getAsInt());
        }
    }

    public static void knockbackEntity(Level world, LivingEntity target, LivingEntity attacker, JsonElement config) {
        if (!world.isClientSide()) {
            target.knockback(config.getAsJsonObject().get("speed").getAsFloat(), config.getAsJsonObject().get("xMovement").getAsDouble(), config.getAsJsonObject().get("zMovement").getAsDouble());
        }
    }

}