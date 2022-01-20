package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomToolMaterial implements Tier {

    private final ResourceLocation materialId;

    public CustomToolMaterial(ResourceLocation materialId) {
        this.materialId = materialId;
    }

    @Override
    public int getUses() {
        return Tiers.IRON.getUses();
    }

    @Override
    public float getSpeed() {
        return Tiers.IRON.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
        return Tiers.IRON.getAttackDamageBonus();
    }

    @Override
    public int getLevel() {
        return Tiers.IRON.getLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return Tiers.IRON.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Registry.ITEM.get(Utils.appendToPath(materialId, "_ingot")));
    }

    public int getSwordAttackDamage() {
        return 3;
    }

    public float getSwordAttackSpeed() {
        return -2.4F;
    }

    public float getShovelAttackDamage() {
        return 1.5F;
    }

    public float getShovelAttackSpeed() {
        return -3.0F;
    }

    public int getPickaxeAttackDamage() {
        return 1;
    }

    public float getPickaxeAttackSpeed() {
        return -2.8F;
    }

    public float getAxeAttackDamage() {
        return 6.0F;
    }

    public float getAxeAttackSpeed() {
        return -3.1F;
    }

    public int getHoeAttackDamage() {
        return -2;
    }

    public float getHoeAttackSpeed() {
        return -1.0F;
    }

}
