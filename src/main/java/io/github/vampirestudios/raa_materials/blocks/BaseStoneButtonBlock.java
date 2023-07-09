package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.api.BasePatterns;
import io.github.vampirestudios.raa_materials.api.LegacyBlockModelProvider;
import io.github.vampirestudios.raa_materials.api.ModelsHelper;
import io.github.vampirestudios.raa_materials.api.PatternsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseStoneButtonBlock extends ButtonBlock implements LegacyBlockModelProvider {
	private final Block source;
	private final ResourceLocation registryName;

	public BaseStoneButtonBlock(ResourceLocation registryName, Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion(), BlockSetType.STONE, 10, false);
		this.registryName = registryName;
		this.source = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getItemModel(ResourceLocation blockId) {
		ResourceLocation parentId = BuiltInRegistries.BLOCK.getKey(source);
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.ITEM_BUTTON, parentId);
		return pattern.orElse("");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation parentId = BuiltInRegistries.BLOCK.getKey(source);
		Optional<String> pattern = blockState.getValue(POWERED) ? PatternsHelper.createJson(
				BasePatterns.BLOCK_BUTTON_PRESSED,
				parentId
		) : PatternsHelper.createJson(BasePatterns.BLOCK_BUTTON, parentId);
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String powered = blockState.getValue(POWERED) ? "_powered" : "";
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + powered);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		AttachFace face = blockState.getValue(FACE);
		boolean isCeiling = face == AttachFace.CEILING;
		int x = 0, y = 0;
		switch (face) {
			case CEILING -> x = 180;
			case WALL -> x = 90;
			default -> {
			}
		}
		switch (blockState.getValue(FACING)) {
			case NORTH:
				if (isCeiling) {
					y = 180;
				}
				break;
			case EAST:
				y = isCeiling ? 270 : 90;
				break;
			case SOUTH:
				if (!isCeiling) {
					y = 180;
				}
				break;
			case WEST:
				y = isCeiling ? 90 : 270;
				break;
			default:
				break;
		}
		BlockModelRotation rotation = BlockModelRotation.by(x, y);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), face == AttachFace.WALL);
	}
}