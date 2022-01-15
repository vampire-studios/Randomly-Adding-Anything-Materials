package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.resources.ResourceLocation;

@SuppressWarnings ("VariableUseSideOnly")
public class ResourceGen {

	public static ResourceLocation prefixPath(ResourceLocation identifier, String prefix) {
		return new ResourceLocation(identifier.getNamespace(), prefix + '/' + identifier.getPath());
	}

}