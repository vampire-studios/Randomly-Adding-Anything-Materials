package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_materials.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomToolMaterial implements Tier {

    private final ResourceLocation materialId;
    private final boolean metal;
    private final int tier;
    private final Tiers finalTier;
    private final int bonus;

    public CustomToolMaterial(ResourceLocation materialId, boolean metal, int tier, int bonus) {
        this.materialId = materialId;
        this.metal = metal;
        this.tier = tier;
        finalTier = getTier();
        this.bonus = bonus;
    }

    private Tiers getTier() {
        return switch (tier) {
            case 0 -> Rands.chance(50) ? Tiers.GOLD : Tiers.WOOD;
            case 1 -> Tiers.STONE;
            case 2 -> Tiers.IRON;
            case 3 -> Tiers.DIAMOND;
            case 4 -> Tiers.NETHERITE;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }

    @Override
    public int getUses() {
        return finalTier.getUses() + bonus * 2;
    }

    @Override
    public float getSpeed() {
        return finalTier.getSpeed() + bonus;
    }

    @Override
    public float getAttackDamageBonus() {
        return (finalTier.getAttackDamageBonus() + bonus) / 2;
    }

    @Override
    public int getLevel() {
        return finalTier.getLevel();
    }

    @Override
    public int getEnchantmentValue() {
        return finalTier.getEnchantmentValue() + bonus * 2;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Registry.ITEM.get(Utils.appendToPath(materialId, metal ? "_ingot" : "_gem")));
    }

    public float getSwordAttackSpeed() {
        return -2.4F;
    }

    public float getShovelAttackSpeed() {
        return -3.0F;
    }

    public float getPickaxeAttackSpeed() {
        return -2.8F;
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
