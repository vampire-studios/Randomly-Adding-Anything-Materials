package io.github.vampirestudios.raa_materials.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;

public class CustomArmorItem extends DyeableArmorItem {

	private final int color;

	public CustomArmorItem(int color, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Settings settings) {
		super(armorMaterial, equipmentSlot, settings);
		this.color = color;
	}

	@Override
	public int getColor(ItemStack stack) {
		return color;
	}

}
