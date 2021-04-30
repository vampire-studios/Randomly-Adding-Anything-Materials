package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.name_generation.Language;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import org.apache.commons.lang3.text.WordUtils;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
	private static final Map<String, String> NAMES = Maps.newHashMap();

	public static void init() {}

	public static void addTranslation(String raw, String translated) {
		NAMES.put(raw, translated);
	}

	public static boolean hasTranslation(String raw) {
		return NAMES.containsKey(raw);
	}

	public static String getTranslation(String raw) {
		return NAMES.get(raw);
	}

	public static String makeRaw(String type, String name) {
		return type + "." + RAAMaterials.MOD_ID + "." + name;
	}

	public static String makeRawItem(String name) {
		return makeRaw("item", name);
	}

	public static String makeRawBlock(String name) {
		return makeRaw("block", name);
	}
}