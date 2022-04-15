package io.github.vampirestudios.raa_materials.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlayerOnlyGlassBlock extends AbstractGlassBlock {

	// used for tinted glass to make light not shine through
	private final boolean tinted;

	public PlayerOnlyGlassBlock(Properties settings, boolean tinted) {
		super(settings);
		this.tinted = tinted;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if(context instanceof EntityCollisionContext) {
			EntityCollisionContext entityShapeContext = (EntityCollisionContext) context;

			Entity entity = entityShapeContext.getEntity();
			if(entity instanceof Player) {
				return Shapes.empty();
			}
		}
		return state.getShape(world, pos);
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
		return !tinted;
	}

	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		if(tinted) {
			return world.getMaxLightLevel();
		} else {
			return super.getLightBlock(state, world, pos);
		}
	}

}
