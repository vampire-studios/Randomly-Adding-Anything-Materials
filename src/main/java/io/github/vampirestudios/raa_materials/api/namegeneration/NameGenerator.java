package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.RAAMaterials;

import java.util.Map;

public class NameGenerator {
	private static final Map<String, String> NAMES = Maps.newHashMap();

	public static void init() {}

	public static void addTranslation(String raw, String translated) {
		NAMES.put(raw.replaceAll("'|`|\\^| |´", ""), translated);
		System.out.println(translated);
	}

	public static boolean hasTranslation(String raw) {
		return NAMES.containsKey(raw);
	}

	public static String getTranslation(String raw) {
		return NAMES.get(raw);
	}

	public static String makeRaw(String type, String name) {
		System.out.println(type + "." + RAAMaterials.MOD_ID + "." + name.replaceAll("'|`|\\^| |´", ""));
		return type + "." + RAAMaterials.MOD_ID + "." + name;
	}

	public static String makeRawItem(String name) {
		return makeRaw("item", name);
	}

	public static String makeRawBlock(String name) {
		return makeRaw("block", name);
	}
}