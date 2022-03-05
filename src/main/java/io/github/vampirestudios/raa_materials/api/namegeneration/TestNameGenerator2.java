package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.vampirelib.utils.Rands;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TestNameGenerator2 {
	private static final List<String> generatedNames = new ArrayList<>();
	public static final List<Pair<String, String>> specialLettersTesting = List.of(
			Pair.of("a", "\u00E0|\u00E4|\u00E6|\u00E5|\u0152|\u0153|\u00E2"),
			Pair.of("e", "\u00E9|\u00E8|\u00EB|\u00A3|\u00EA"),
			Pair.of("c", "\u00E7"),
			Pair.of("i", "\u00EF|\u026A"),
			Pair.of("o", "\u00F6|\uA66E|\uA69A|\u2D32|\u00F8|\u03A9|\u2609|\u232C|\u23E3|\u263A|\u263B|\u263C|\u0278"),
			Pair.of("u", "\u00F9|\u00FC"),
			Pair.of("w", "\u03C9|\u0270"),
			Pair.of("v", "\u0076|\u2304"),
			Pair.of("y", "\u26A7"),
			Pair.of("f", "\u0283")
	);

	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			generateOreName();
		}
	}

	public static String generateOreName() {
		String name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		while(generatedNames.contains(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\+|-|\\.|,", ""))) {
			name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		}
		String finalName = name;
		new Thread(() -> System.out.println(finalName)).start();
		generatedNames.add(name);
		return name;
	}

	public static String generateStoneName() {
		String name = generate(5, 15);
		while(generatedNames.contains(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\+|-|\\.|,", ""))) {
			name = generate(5, 15);
		}
		generatedNames.add(name);
		return name;
	}

	public static String generate(int min, int max) {
		String vowels = "aeiou", characters = "qwrtypsdfghjklzxcvbnm", mutations = "'`^ ´&¤%!+-.,";
		StringBuilder name = new StringBuilder();
		for (int i = 1; i < Rands.randIntRange(min, max); i++) {
			if ((i == 1)) {
				if (Rands.randIntRange(1, 100) >= 50) {
					int rng = Rands.randIntRange(1, 4);
					name.append(vowels.substring(rng - 1, rng).toUpperCase(Locale.ROOT));
					i++;
				} else {
					int rng = Rands.randIntRange(1, 20);
					name.append(characters.substring(rng - 1, rng).toUpperCase(Locale.ROOT));
				}
			} else if (((i % 2) == 0)) {
				int rng = Rands.randIntRange(1, 4);
				name.append(vowels, rng - 1, rng);
			} else if (Rands.randIntRange(1, 100) >= 95) {
				int rng = Rands.randIntRange(1, 13);
				name.append(mutations, rng - 1, rng);
			} else if (Rands.randIntRange(1, 100) >= 75) {
				int rng = Rands.randIntRange(1, 4);
				name.append(vowels, rng - 1, rng);
			} else {
				int rng = Rands.randIntRange(1, 20);
				name.append(characters, rng - 1, rng);
			}
		}

		String finalName = name.toString();

		if (Rands.chance(40)) {
			String test = name.toString();
			Pair<String, String> stringStringPair = specialLettersTesting.get(Rands.randIntRange(1, specialLettersTesting.size() - 1));
			String[] strings = stringStringPair.getSecond().split("\\|");
			int rng = Rands.randIntRange(1, strings.length);
			test = changeString(test, strings[rng - 1]);
			finalName = test;
		}
		return finalName;
	}

	public static String changeString(String entry, String change) {
		int length = entry.length();
		char[] cArray = entry.toCharArray();
		cArray[randomNumber(new Random(), 0, length)] = change.charAt(0);
		return WordUtils.capitalizeFully(String.valueOf(cArray));
	}

	public static int randomNumber(Random random, int min, int max) {
		return min + random.nextInt(max - min);
	}
}
