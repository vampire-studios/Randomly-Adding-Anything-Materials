package io.github.vampirestudios.raa_materials.items;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;

public class CustomArmorItem extends DyeableArmorItem {

	private final int color;

	public CustomArmorItem(int color, ArmorMaterial armorMaterial, ArmorItem.Type type, Properties settings) {
		super(armorMaterial, type, settings);
		this.color = color;
	}

	@Override
	public int getColor(ItemStack stack) {
		return color;
	}

}
