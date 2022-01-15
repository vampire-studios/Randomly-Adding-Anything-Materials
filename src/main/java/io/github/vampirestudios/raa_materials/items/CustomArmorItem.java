package io.github.vampirestudios.raa_materials.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;

public class CustomArmorItem extends DyeableArmorItem {

	private final int color;

	public CustomArmorItem(int color, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties settings) {
		super(armorMaterial, equipmentSlot, settings);
		this.color = color;
	}

	@Override
	public int getColor(ItemStack stack) {
		return color;
	}

}
