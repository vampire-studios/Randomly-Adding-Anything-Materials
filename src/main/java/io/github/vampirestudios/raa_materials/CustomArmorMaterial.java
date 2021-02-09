package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomArmorMaterial implements ArmorMaterial {

    private final Identifier materialId;

    public CustomArmorMaterial(Identifier materialId) {
        this.materialId = materialId;
    }

    public int getDurability(EquipmentSlot equipmentSlot_1) {
        return ArmorMaterials.IRON.getDurability(equipmentSlot_1);
    }

    public int getProtectionAmount(EquipmentSlot equipmentSlot_1) {
        return ArmorMaterials.IRON.getProtectionAmount(equipmentSlot_1);
    }

    public int getEnchantability() {
        return ArmorMaterials.IRON.getEnchantability();
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Environment(EnvType.CLIENT)
    public String getName() {
        return "testing";
    }

    public float getToughness() {
        return ArmorMaterials.IRON.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.IRON.getKnockbackResistance();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Registry.ITEM.get(Utils.appendToPath(materialId, "_ingot")));
    }

}