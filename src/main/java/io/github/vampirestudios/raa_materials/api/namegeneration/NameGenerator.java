package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.name_generation.Language;
import io.github.vampirestudios.raa_materials.RAAMaterials;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class NameGenerator {
	private static final MarkovChain ORE_GEN = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("ores") : makeLanguageChain("ores", RAACore.CONFIG.getLanguage().getId());
	private static final MarkovChain ROCKS = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("rocks") : makeLanguageChain("rocks", RAACore.CONFIG.getLanguage().getId());
	private static final Map<String, String> NAMES = Maps.newHashMap();
	private static final Set<String> GENERATED = Sets.newHashSet();

	public static void init() {}

	private static MarkovChain makeChain(String name) {
		InputStream stream = NameGenerator.class.getResourceAsStream("/assets/" + RAAMaterials.MOD_ID + "/namegen/" + name + ".txt");
		return new MarkovChain(stream);
	}

	private static MarkovChain makeLanguageChain(String name, String language) {
		InputStream stream = NameGenerator.class.getResourceAsStream("/assets/" + RAAMaterials.MOD_ID + "/namegen/" + language + "/" + name + ".txt");
		return new MarkovChain(stream);
	}

	private static String makeName(MarkovChain chain, Random random, int min, int max) {
		String result = chain.makeWord(min, max, random);
		result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		for (int i = 0; i < 600 && GENERATED.contains(result); i++) {
			result = chain.makeWord(min, max, random);
			result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		}
		result = GENERATED.contains(result) ? result + "_" + Integer.toHexString(random.nextInt()) : result;
		GENERATED.add(result);
		return result;
	}

	private static String makeName(MarkovChain chain, Random random, int min, int max, Supplier<String> failFunction) {
		String result = chain.makeWord(min, max, random);
		result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		for (int i = 0; i < 600 && GENERATED.contains(result); i++) {
			result = chain.makeWord(min, max, random);
			result = result.isEmpty() ? Long.toHexString(random.nextLong()).toLowerCase() : result;
		}
		if (GENERATED.contains(result)) {
			result = failFunction.get();
		}
		GENERATED.add(result);
		return result;
	}

	public static void clearNames() {
		GENERATED.clear();
	}

	public static String makeOreName(Random random) {
		return makeName(ORE_GEN, random, 6, 12, () -> makeRockName(random));
	}

	public static String makeRockName(Random random) {
		return makeName(ROCKS, random, 6, 12);
	}

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