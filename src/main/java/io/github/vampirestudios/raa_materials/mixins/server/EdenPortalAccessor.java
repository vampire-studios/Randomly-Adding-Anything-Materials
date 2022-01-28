package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulevs.edenring.world.EdenPortal;

import java.util.Map;

@Mixin(EdenPortal.class)
public interface EdenPortalAccessor {
	@Accessor
	static Map<BlockPos, BlockState> getPRE_PORTAL() {
		throw new UnsupportedOperationException();
	}

	@Accessor
	static Map<BlockPos, BlockState> getPORTAL() {
		throw new UnsupportedOperationException();
	}
}
