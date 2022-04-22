package io.github.vampirestudios.raa_materials.blocks;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.api.*;
import io.github.vampirestudios.raa_materials.utils.Rands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomCrystalClusterBlock extends AmethystClusterBlock implements SimpleWaterloggedBlock, BlockModelProvider {
	public final Item drop;
	private boolean dropsSelf = false;

	public CustomCrystalClusterBlock(BlockBehaviour.Properties settings, Item drop) {
		super(7, 3, settings);
		if (drop == null) {
			this.drop = this.asItem();
			this.dropsSelf = true;
		} else {
			this.drop = drop;
		}
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));
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

	@Override
	@Environment(EnvType.CLIENT)
	public String getItemModel(ResourceLocation blockId) {
		return createBlockPattern(blockId).orElse("");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable String getBlockModel(ResourceLocation blockId, BlockState blockState) {
		return createBlockPattern(blockId).orElse("");
	}

	@Override
	public void registerVariants(ResourceLocation id) {
		this.stateDefinition.getPossibleStates().forEach((aa)->{
			ResourceLocation stateId = new ModelResourceLocation(id.getNamespace(), id.getPath(), aa.toString());
			InnerRegistryClient.registerBlockVarients(new ModelResourceLocation(id.getNamespace(),"blockstates/"+stateId.getPath(), aa.toString()), this.getVariantModel(stateId, aa));
		});
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getVariantModel(ResourceLocation stateId, BlockState blockState) {
		ModelResourceLocation shardsUp = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), this.defaultBlockState().toString());

		Direction facing = blockState.getValue(FACING);

		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState);

		Transformation transformation = new Transformation(null, facing.getRotation(), null, null);
		return ModelsHelper.createMultiVariant(shardsUp, transformation, false);
	}

	protected Optional<String> createBlockPattern(ResourceLocation blockId) {
		return PatternsHelper.createBlockCross(blockId);
	}

}
