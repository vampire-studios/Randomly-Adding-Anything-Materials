package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.items.effects.MaterialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.ShufflingList;

import java.util.Locale;
import java.util.Map;

public class Utils {

	public static String toTitleCase(String lowerCase) {
		return Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
	}

	public static String nameToId(String name, Map<String, String> specialCharMap) {
		// strip name of special chars
		for (Map.Entry<String, String> specialChar : specialCharMap.entrySet()) {
			name = name.replace(specialChar.getKey(), specialChar.getValue());
		}
		return name.toLowerCase(Locale.ENGLISH);
	}

	public static ResourceLocation appendToPath(ResourceLocation identifier, String suffix) {
		return new ResourceLocation(identifier.getNamespace(), identifier.getPath() + suffix);
	}

	public static ResourceLocation prependToPath(ResourceLocation identifier, String prefix) {
		return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath());
	}

	public static ResourceLocation appendAndPrependToPath(ResourceLocation identifier, String prefix, String suffix) {
		return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath() + suffix);
	}

	public static final ShufflingList<MaterialEffects> EFFECT_LIST = new ShufflingList<>();

	static {
		EFFECT_LIST.add(MaterialEffects.LIGHTNING, 2);
		EFFECT_LIST.add(MaterialEffects.EFFECT, 4);
		EFFECT_LIST.add(MaterialEffects.FIREBALL, 2);
		EFFECT_LIST.add(MaterialEffects.FREEZE, 1);
	}

}
