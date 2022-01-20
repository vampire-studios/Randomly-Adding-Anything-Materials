package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collections;
import java.util.List;

public class BaseDropBlock extends BaseBlock {
	private Item drop;

	public BaseDropBlock(Properties settings, Item drop) {
		super(settings);
		this.drop = drop;
	}

	public Item getDrop() {
		if (this.drop == null) {
			this.drop = this.asItem();
		}
		return drop;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(getDrop()));
	}
}