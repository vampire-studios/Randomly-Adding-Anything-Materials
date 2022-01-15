package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

public class CustomArmorMaterial implements ArmorMaterial {

    private final ResourceLocation materialId;

    public CustomArmorMaterial(ResourceLocation materialId) {
        this.materialId = materialId;
    }

    public int getDurabilityForSlot(EquipmentSlot equipmentSlot_1) {
        return ArmorMaterials.IRON.getDurabilityForSlot(equipmentSlot_1);
    }

    public int getDefenseForSlot(EquipmentSlot equipmentSlot_1) {
        return ArmorMaterials.IRON.getDefenseForSlot(equipmentSlot_1);
    }

    public int getEnchantmentValue() {
        return ArmorMaterials.IRON.getEnchantmentValue();
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
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
        return Ingredient.of(Registry.ITEM.get(Utils.appendToPath(materialId, "_ingot")));
    }

}