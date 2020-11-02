package io.github.vampirestudios.raa_materials.client;

import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

/**
 * Encapsulates parts of baked models that can't be pre-baked and
 * must be generated at render time. Allows packed models to include
 * dynamic elements via composition instead of sub-typing.
 */
@FunctionalInterface
public interface DynamicRenderer {
    void render(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context);
}