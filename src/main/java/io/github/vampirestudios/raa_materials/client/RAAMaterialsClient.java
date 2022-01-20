package io.github.vampirestudios.raa_materials.client;

import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;
import net.fabricmc.fabric.api.client.model.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import static io.github.vampirestudios.raa_materials.RAAMaterials.MOD_ID;

public class RAAMaterialsClient implements RAAAddonClient, ModelResourceProvider, ModelVariantProvider {
	public static CustomModelBakery modelBakery;

	@Override
	public void onClientInitialize() {
		modelBakery = new CustomModelBakery();
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> this);
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> this);
	}

	@Override
	public String getId() {
		return MOD_ID;
	}

	@Override
	public String[] shouldLoadAfter() {
		return new String[]{};
	}

	@Override
	public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) {
		return modelBakery.getBlockModel(resourceId);
	}

	@Override
	public @Nullable UnbakedModel loadModelVariant(ModelResourceLocation modelId, ModelProviderContext context) {
		return modelId.getVariant().equals("inventory") ? modelBakery.getItemModel( modelId) : modelBakery.getBlockModel(modelId);
	}

}