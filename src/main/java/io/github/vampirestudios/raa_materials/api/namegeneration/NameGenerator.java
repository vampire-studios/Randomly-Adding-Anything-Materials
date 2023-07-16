package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.google.common.collect.Maps;
import com.ibm.icu.text.MessageFormat;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class NameGenerator {
	private static final Map<String, String> NAMES = Maps.newHashMap();
	private static final Map<String, String> BASEKEYS = Maps.newHashMap();
	private static final Map<String, Object[]> BASENAMES = Maps.newHashMap();

	public static void init() {}

	public static void addTranslation(String raw, String translated) {
		NAMES.put(raw.replaceAll("['`^ ´]", ""), translated);
	}

	public static void addTranslation(String raw, String rawBase, String base) {
		Object[] data = {base, base.toLowerCase(), base.toLowerCase().charAt(0), base.toLowerCase().charAt(base.length() - 1)};
		String prepedKey = raw.replaceAll("['`^ ´]", "");
		String baseKey = "text.raa_materials."+rawBase;
		BASEKEYS.put(prepedKey, baseKey);
		BASENAMES.put(prepedKey, data);
		NAMES.put(prepedKey, generate(baseKey, data));
	}

	public static void regenerateTranslations() {
		for (Map.Entry<String,String> name: NAMES.entrySet()) {
			if(retranslatable(name.getKey())){
				String transKey = name.getKey();
				NAMES.replace(name.getKey(), generate(BASEKEYS.get(transKey), BASENAMES.get(transKey)));
			}
		}
	}

	public static boolean hasTranslation(String raw) {
		return NAMES.containsKey(raw);
	}

	public static String getTranslation(String raw) {
		return NAMES.get(raw);
	}

	public static boolean retranslatable(String raw) {
		return BASEKEYS.containsKey(raw);
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

	public static String generate(String rawBase, Object[] args) {
		Component translatableText = Component.translatable(rawBase);
		MessageFormat messageFormat = new MessageFormat(translatableText.getString());
		return messageFormat.format(args);
	}


}