package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CustomCrystalBlock extends AmethystBlock {

	private final Supplier<Item> drop;

	public CustomCrystalBlock(Settings settings) {
		super(settings);
		this.drop = this::asItem;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop.get()));
	}
}
