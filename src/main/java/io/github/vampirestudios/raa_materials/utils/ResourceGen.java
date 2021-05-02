package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.util.Identifier;

@SuppressWarnings ("VariableUseSideOnly")
public class ResourceGen {

	public static Identifier prefixPath(Identifier identifier, String prefix) {
		return new Identifier(identifier.getNamespace(), prefix + '/' + identifier.getPath());
	}

}