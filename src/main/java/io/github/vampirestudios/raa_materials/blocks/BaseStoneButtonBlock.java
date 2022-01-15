package io.github.vampirestudios.raa_materials.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Collections;
import java.util.List;

public class BaseStoneButtonBlock extends StoneButtonBlock {
	public BaseStoneButtonBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}