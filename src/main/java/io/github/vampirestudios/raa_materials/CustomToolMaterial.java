package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomToolMaterial implements ToolMaterial {

    private final transient Identifier materialId;

    public CustomToolMaterial(Identifier materialId) {
        this.materialId = materialId;
    }

    @Override
    public int getDurability() {
        return ToolMaterials.IRON.getDurability();
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return ToolMaterials.IRON.getMiningSpeedMultiplier();
    }

    @Override
    public float getAttackDamage() {
        return ToolMaterials.IRON.getAttackDamage();
    }

    @Override
    public int getMiningLevel() {
        return ToolMaterials.IRON.getMiningLevel();
    }

    @Override
    public int getEnchantability() {
        return ToolMaterials.IRON.getEnchantability();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Registry.ITEM.get(Utils.appendToPath(materialId, "_ingot")));
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
