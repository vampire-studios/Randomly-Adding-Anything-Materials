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
	private static final MarkovChain ORE_GEN = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("ores") : makeLanguageChain("ores", RAACore.CONFIG.getLanguage().getId());
	private static final MarkovChain GEM_GEN = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("gems") : makeLanguageChain("gems", RAACore.CONFIG.getLanguage().getId());
	private static final MarkovChain METAL_GEN = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("metals") : makeLanguageChain("metals", RAACore.CONFIG.getLanguage().getId());
	private static final MarkovChain CRYSTAL_GEN = RAACore.CONFIG.getLanguage() == Language.ENGLISH ? makeChain("crystals") : makeLanguageChain("crystals", RAACore.CONFIG.getLanguage().getId());
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

	private static String generateRockName(Random random) {
		String result = WordUtils.capitalizeFully(NameGenerator.ROCKS.makeWord(6, 12, random).toLowerCase(Locale.ROOT));
		result = result.isEmpty() ?WordUtils.capitalizeFully( NameGenerator.ROCKS.makeWord(6, 12, random).toLowerCase(Locale.ROOT)): result;
		for (int i = 0; i < 600 && GENERATED.contains(result); i++) {
			result = WordUtils.capitalizeFully(NameGenerator.ROCKS.makeWord(6, 12, random).toLowerCase(Locale.ROOT));
			result = result.isEmpty() ? WordUtils.capitalizeFully(NameGenerator.ROCKS.makeWord(6, 12, random).toLowerCase(Locale.ROOT)) : result;
		}
//		if (GENERATED.contains(result)) {
//			result = generateRockName(random);
//		}
		GENERATED.add(result);
		return result;
	}

	private static String generateOreName(MarkovChain type, Random random) {
		String result = WordUtils.capitalizeFully(type.makeWord(6, 12, random).toLowerCase(Locale.ROOT));
		result = result.isEmpty() ? WordUtils.capitalizeFully(type.makeWord(6, 12, random).toLowerCase(Locale.ROOT)) : result;
		for (int i = 0; i < 600 && GENERATED.contains(result); i++) {
			result = WordUtils.capitalizeFully(type.makeWord(6, 12, random).toLowerCase(Locale.ROOT));
			result = result.isEmpty() ? WordUtils.capitalizeFully(type.makeWord(6, 12, random).toLowerCase(Locale.ROOT)) : result;
		}
//		if (GENERATED.contains(result)) {
//			result = generateOreName(type, random);
//		}
		GENERATED.add(result);
		return result;
	}

	public static void clearNames() {
		GENERATED.clear();
	}

	public static String makeOreName(String oreType, Random random) {
		switch (oreType) {
			case "crystal":
				return generateOreName(CRYSTAL_GEN, random);
			case "metal":
				return generateOreName(METAL_GEN, random);
			case "gem":
				return generateOreName(GEM_GEN, random);
		}
		return generateOreName(ORE_GEN, random);
	}

	public static String makeRockName(Random random) {
		return generateRockName(random);
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