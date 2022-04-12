package io.github.vampirestudios.raa_materials.api.namegeneration;

import io.github.vampirestudios.raa_materials.utils.Rands;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class TestNameGenerator {
	private static final Map<String, String> generatedNames = new HashMap<>();
	public static final List<Pair<String, String>> specialLettersTesting = List.of(
			Pair.of("a", "\u00E0|\u00E4|\u00E6|\u00E5|\u0152|\u0153"),
			Pair.of("e", "\u00E9|\u00E8|\u00EB|\u00A3"),
			Pair.of("c", "\u00E7"),
			Pair.of("i", "\u00EF|\u026A"),
			Pair.of("o", "\u00F6|\uA66E|\u2D32|\u00F8|\u03A9|\u2609|\u232C|\u23E3|\u263A|\u263B|\u263C|\u0278"),
			Pair.of("u", "\u00F9|\u00FC"),
			Pair.of("w", "\u03C9|\u0270"),
			Pair.of("v", "\u0076|\u2304"),
			Pair.of("Y", "\u26A7"),
			Pair.of("f", "\u0283")
	);

	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			System.out.println(generateOreName(new Random()));
		}
	}

	public static Pair<String, String> generateOreName(Random random) {
		Rands.setRand(random);
		String name = generate(random, Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		while(generatedNames.containsKey(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", ""))) {
			name = generate(random, Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		}
		String registryName = name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", "");
		for (Pair<String, String> stringStringPair : specialLettersTesting) {
			String[] strings = stringStringPair.getValue().split("\\|");
			for (String string : strings) {
				if (registryName.contains(string)) registryName = registryName.replace(string, stringStringPair.getKey());
			}
		}
		generatedNames.put(name, registryName);
		return Pair.of(name, registryName);
	}

	public static Pair<String, String> generateStoneName(Random random) {
		Rands.setRand(random);
		String name = generate(random, 5, 15);
		while(generatedNames.containsKey(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", ""))) {
			name = generate(random, 5, 15);
		}
		String extraName = name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", "");
		for (Pair<String, String> stringStringPair : specialLettersTesting) {
			String[] strings = stringStringPair.getValue().split("\\|");
			for (String string : strings) {
				if (extraName.contains(string)) extraName = extraName.replace(string, stringStringPair.getKey());
			}
		}
		generatedNames.put(name, extraName);
		return Pair.of(name, extraName);
	}

	public static String generate(Random random, int min, int max) {
		Rands.setRand(random);
		String vowels = "aeiou", characters = "qwrtypsdfghjklzxcvbnm", mutations = "'`^ ´&¤%!?+-.,";
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
				int rng = Rands.randIntRange(1, 14);
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

		if (Rands.chance(20)) {
			String test = name.toString();
			int tries = 3;
			while (tries > 0) {
				int rng = Rands.randIntRange(1, specialLettersTesting.size());
				Pair<String, String> stringStringPair = specialLettersTesting.get(rng-1);
				String[] strings = stringStringPair.getRight().split("\\|");
				rng = Rands.randIntRange(1, strings.length);
				StringBuilder aaa = new StringBuilder(test);
				if(aaa.indexOf(stringStringPair.getLeft())!=-1) aaa.setCharAt(aaa.indexOf(stringStringPair.getLeft()), strings[rng - 1].charAt(0));
				test = aaa.toString();
				tries--;
			}
			finalName = test;
		}
		return finalName;
	}

}
