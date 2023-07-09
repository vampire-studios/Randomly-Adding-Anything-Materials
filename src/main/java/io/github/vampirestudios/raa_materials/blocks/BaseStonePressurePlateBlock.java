package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.api.BasePatterns;
import io.github.vampirestudios.raa_materials.api.LegacyBlockModelProvider;
import io.github.vampirestudios.raa_materials.api.ModelsHelper;
import io.github.vampirestudios.raa_materials.api.PatternsHelper;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseStonePressurePlateBlock extends PressurePlateBlock implements LegacyBlockModelProvider {
	private final Block source;

	public BaseStonePressurePlateBlock(Block source) {
		super(Sensitivity.MOBS, FabricBlockSettings.copyOf(source).noOcclusion(), BlockSetType.STONE);
		this.source = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getItemModel(ResourceLocation resourceLocation) {
		return ModelHelper.simpleParentItem(resourceLocation);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation parentId = BuiltInRegistries.BLOCK.getKey(source);
		Optional<String> pattern;
		if (blockState.getValue(POWERED)) {
			pattern = PatternsHelper.createJson(BasePatterns.BLOCK_PLATE_DOWN, parentId);
		}
		else {
			pattern = PatternsHelper.createJson(BasePatterns.BLOCK_PLATE_UP, parentId);
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String state = blockState.getValue(POWERED) ? "_down" : "_up";
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + state);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createBlockSimple(modelId);
	}
}