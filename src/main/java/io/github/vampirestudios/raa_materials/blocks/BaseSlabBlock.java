package io.github.vampirestudios.raa_materials.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Collections;
import java.util.List;

public class BaseSlabBlock extends SlabBlock {
	private final Block source;

	public BaseSlabBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
		this.source = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

}