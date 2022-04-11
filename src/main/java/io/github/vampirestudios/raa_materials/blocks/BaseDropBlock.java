package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.utils.Rands;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

public class BaseDropBlock extends BaseBlock {
	public final Item drop;
	private boolean dropsSelf = false;

	public BaseDropBlock(Properties settings, Item drop) {
		super(settings);
		if (drop == null) {
			this.drop = this.asItem();
			this.dropsSelf = true;
		} else {
			this.drop = drop;
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		int amount = 1;
		if (!this.dropsSelf) {
			ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
			if (stack != null) {
				int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
				amount = Rands.randIntRange(1, 1 + fortuneLevel);
			}
		}
		return Collections.singletonList(new ItemStack(this.drop, amount));
	}
}