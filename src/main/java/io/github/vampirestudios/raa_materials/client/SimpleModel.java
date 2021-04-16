package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Simple baked model supporting the Fabric Render API features.<p>
 */
public class SimpleModel extends AbstractModel {
	protected final Mesh mesh;
	protected final Supplier<MeshTransformer> transformerFactory;
	protected WeakReference<List<BakedQuad>[]> quadLists = null;
	protected final ItemProxy itemProxy = new ItemProxy();

	public SimpleModel(
	Mesh mesh,
	Supplier<MeshTransformer> transformerFactory,
	Sprite sprite,
	ModelTransformation transformation,
	DynamicRenderer dynamicRender) {
		super(sprite, transformation, dynamicRender);
		this.mesh = mesh;
		this.transformerFactory = transformerFactory;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random rand) {
		List<BakedQuad>[] lists = quadLists == null ? null : quadLists.get();
		if(lists == null) {
			lists = ModelHelper.toQuadLists(mesh);
			quadLists = new WeakReference<>(lists);
		}
		final List<BakedQuad> result = lists[face == null ? 6 : face.getId()];
		return result == null ? ImmutableList.of() : result;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		final MeshTransformer transform = transformerFactory == null ? null : transformerFactory.get().prepare(blockView, state, pos, randomSupplier);
		if(transform != null) {
			context.pushTransform(transform);
		}
		if(mesh != null) {
			context.meshConsumer().accept(mesh);
		}
		if(dynamicRender != null) {
			dynamicRender.render(blockView, state, pos, randomSupplier, context);
		}
		if(transform != null) {
			context.popTransform();
		}
	}

	@Override
	public ModelOverrideList getOverrides() {
		return itemProxy;
	}

	protected class ItemProxy extends ModelOverrideList {
		public ItemProxy() {
			super(null, null, null, Collections.emptyList());
		}

		@Nullable
		@Override
		public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
			return SimpleModel.this;
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		final MeshTransformer transform = transformerFactory == null ? null : transformerFactory.get().prepare(stack, randomSupplier);
		if(transform != null) {
			context.pushTransform(transform);
		}
		if(mesh != null) {
			context.meshConsumer().accept(mesh);
		}
		if(dynamicRender != null) {
			dynamicRender.render(null, null, null, randomSupplier, context);
		}
		if(transform != null) {
			context.popTransform();
		}
	}
}