package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BaseDropBlock extends BaseBlock {
	private final Supplier<Item> drop;

	public BaseDropBlock(Settings settings, Supplier<Item> drop) {
		super(settings);
		this.drop = drop;
	}

	public BaseDropBlock(Settings settings) {
		super(settings);
		this.drop = this::asItem;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop.get()));
	}
}