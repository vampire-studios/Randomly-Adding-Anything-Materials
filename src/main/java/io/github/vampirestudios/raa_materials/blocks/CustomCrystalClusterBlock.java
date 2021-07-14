package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.Collections;
import java.util.List;

public class CustomCrystalClusterBlock extends AmethystClusterBlock {

	private final Item drop;

	public CustomCrystalClusterBlock(int height, int xzOffset, Settings settings, Item drop) {
		super(height, xzOffset, settings);
		this.drop = drop == null ? this.asItem() : drop;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop));
	}

}
