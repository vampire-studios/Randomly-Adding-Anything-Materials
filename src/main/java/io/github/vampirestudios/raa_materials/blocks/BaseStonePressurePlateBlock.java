package io.github.vampirestudios.raa_materials.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Collections;
import java.util.List;

public class BaseStonePressurePlateBlock extends PressurePlateBlock {
	public BaseStonePressurePlateBlock(Block source) {
		super(Sensitivity.MOBS, FabricBlockSettings.copyOf(source).noOcclusion());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}