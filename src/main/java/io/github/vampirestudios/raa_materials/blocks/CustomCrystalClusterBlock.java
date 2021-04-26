package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CustomCrystalClusterBlock extends AmethystClusterBlock {

	private final Supplier<Item> drop;

	public CustomCrystalClusterBlock(int height, int xzOffset, Settings settings, Supplier<Item> drop) {
		super(height, xzOffset, settings);
		this.drop = drop;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop.get()));
	}

}
