package io.github.vampirestudios.raa_materials.blocks;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

public class BaseDropBlock extends BaseBlock {
	private final Item drop;

	public BaseDropBlock(Properties settings, Item drop) {
		super(settings);
		this.drop = drop == null ? this.asItem() : drop;
	}

	public BaseDropBlock(Properties settings) {
		this(settings, null);
	}

	public Item getDrop() {
		return drop;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop));
	}
}