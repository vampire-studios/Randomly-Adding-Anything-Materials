package io.github.vampirestudios.raa_materials.items;

import com.google.gson.JsonElement;
import io.github.vampirestudios.raa_materials.items.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.materials.OreMaterial;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Objects;

public class CustomSwordItem extends SwordItem {

    private final OreMaterial oreMaterial;

    public CustomSwordItem(OreMaterial oreMaterial, Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
        this.oreMaterial = oreMaterial;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level world = Objects.requireNonNull(target).level;
        if (!world.isClientSide()) {
            for (Map.Entry<MaterialEffects, JsonElement> effect : oreMaterial.getSpecialEffects().entrySet()) {
                effect.getKey().apply(world, target, attacker, effect.getValue());
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
