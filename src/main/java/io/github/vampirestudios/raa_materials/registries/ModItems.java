package io.github.vampirestudios.raa_materials.registries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

	public static Item testItem;

	public static void init() {
		testItem = registerItem("test_item", new Item(new Item.Settings().group(ItemGroup.MISC)));
	}

	public static Item registerItem(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier("modid", name), item);
	}

}
