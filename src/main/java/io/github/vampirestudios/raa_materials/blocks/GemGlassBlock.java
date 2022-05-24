/*
package io.github.vampirestudios.raa_materials.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GemGlassBlock extends GlassBlock {

	private int gemstoneColor;

	public GemGlassBlock(Properties settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if(stateFrom.is(this)) {
			return true;
		}
		if(state.getBlock() instanceof GemGlassBlock && state.getBlock() instanceof PlayerOnlyGlassBlock &&
				state.getBlock() instanceof de.dafuqs.spectrum.blocks.glass.GemstoneGlassBlock &&
				state.getBlock() instanceof de.dafuqs.spectrum.blocks.glass.GemstonePlayerOnlyGlassBlock &&
				state.getBlock() instanceof TintedGlassBlock) {
			return true;
		}
		return super.skipRendering(state, stateFrom, direction);
	}

	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

}
*/
