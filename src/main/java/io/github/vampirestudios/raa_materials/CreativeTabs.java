package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import java.util.Locale;

public class CreativeTabs {
	public static final CreativeModeTab BLOCKS = makeGroup("Random Blocks", Blocks.STONE);

	private static CreativeModeTab makeGroup(String name, ItemLike icon) {
		String idString = name.toLowerCase(Locale.ROOT).replace(" ", "_");
		if (RAAMaterials.isClient()) {
			NameGenerator.addTranslation("itemGroup." + RAAMaterials.MOD_ID + "." + idString, name);
		}
		return FabricItemGroupBuilder.create(RAAMaterials.id(idString)).icon(() -> new ItemStack(icon)).build();
	}
}