package io.github.vampirestudios.raa_materials.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;

public abstract class AbstractModel implements BakedModel, FabricBakedModel {
	protected static final Renderer RENDERER = RendererAccess.INSTANCE.getRenderer();

	protected final Sprite modelSprite;
	protected final ModelTransformation transformation;
	protected final DynamicRenderer dynamicRender;

	protected AbstractModel(
			Sprite sprite,
			ModelTransformation transformation,
			DynamicRenderer dynamicRender) {
		modelSprite = sprite;
		this.transformation = transformation;
		this.dynamicRender = dynamicRender;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return true;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return modelSprite;
	}

	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}
}