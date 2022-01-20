package io.github.vampirestudios.raa_materials.blocks;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import io.github.vampirestudios.raa_materials.api.BasePatterns;
import io.github.vampirestudios.raa_materials.api.BlockModelProvider;
import io.github.vampirestudios.raa_materials.api.ModelsHelper;
import io.github.vampirestudios.raa_materials.api.PatternsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomCrystalClusterBlock extends AmethystClusterBlock implements SimpleWaterloggedBlock, BlockModelProvider {
	private Item drop;

	public CustomCrystalClusterBlock(BlockBehaviour.Properties settings, Item drop) {
		super(7, 3, settings);
		this.drop = drop;
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));
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

	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		return ModelsHelper.fromPattern(createBlockPattern(blockId));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		return ModelsHelper.fromPattern(createBlockPattern(blockId));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ModelResourceLocation shardsUp = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), this.defaultBlockState().toString());

		if (!modelCache.containsKey(shardsUp)) {
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%modid%", stateId.getNamespace());
			textures.put("%texture%", stateId.getPath());
			Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
			BlockModel model = ModelsHelper.fromPattern(pattern);
			modelCache.put(shardsUp, model);
		}

		Direction facing = blockState.getValue(FACING);
		if (facing == Direction.UP) {
			return modelCache.get(shardsUp);
		}

		Transformation transformation = new Transformation(null, facing.getRotation(), null, null);
		return ModelsHelper.createMultiVariant(shardsUp, transformation, false);
	}

	protected Optional<String> createBlockPattern(ResourceLocation blockId) {
		return PatternsHelper.createBlockCross(blockId);
	}

}
