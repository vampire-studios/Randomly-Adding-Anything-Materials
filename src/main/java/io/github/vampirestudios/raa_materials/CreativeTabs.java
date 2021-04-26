package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Locale;

public class CreativeTabs {
	public static final ItemGroup BLOCKS = makeGroup("Random Blocks", Blocks.STONE);
	public static final ItemGroup ITEMS = makeGroup("Random Items", Items.GOLD_INGOT);

	private static ItemGroup makeGroup(String name, ItemConvertible icon) {
		String idString = name.toLowerCase(Locale.ROOT).replace(" ", "_");
		if (RAAMaterials.isClient()) {
			NameGenerator.addTranslation("itemGroup." + RAAMaterials.MOD_ID + "." + idString, name);
		}
		return FabricItemGroupBuilder.create(RAAMaterials.id(idString)).icon(() -> new ItemStack(icon)).build();
	}
}