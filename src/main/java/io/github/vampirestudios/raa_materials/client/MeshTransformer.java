package io.github.vampirestudios.raa_materials.client;

import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext.QuadTransform;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public interface MeshTransformer extends QuadTransform {
    default MeshTransformer prepare(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier) { return this;}
    
    default MeshTransformer prepare(ItemStack stack, Supplier<Random> randomSupplier) { return this;};
}