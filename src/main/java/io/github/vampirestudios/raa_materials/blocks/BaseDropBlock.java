package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.Collections;
import java.util.List;

public class BaseDropBlock extends BaseBlock {
	private final Item drop;

	public BaseDropBlock(Settings settings, Item drop) {
		super(settings);
		this.drop = drop == null ? this.asItem() : drop;
	}

	public BaseDropBlock(Settings settings) {
		this(settings, null);
	}

	public Item getDrop() {
		return drop;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop));
	}
}