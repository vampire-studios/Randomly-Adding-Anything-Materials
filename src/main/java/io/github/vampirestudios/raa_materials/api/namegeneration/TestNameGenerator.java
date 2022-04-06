package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.vampirelib.utils.Rands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestNameGenerator {
	private static final List<String> generatedNames = new ArrayList<>();
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
			generateOreName();
		}
	}

	public static String generateOreName() {
		String name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		while(generatedNames.contains(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", ""))) {
			name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		}
		generatedNames.add(name);
		return name;
	}

	public static String generateStoneName() {
		String name = generate(5, 15);
		while(generatedNames.contains(name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", ""))) {
			name = generate(5, 15);
		}
		generatedNames.add(name);
		return name;
	}

	public static String generate(int min, int max) {
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

		if (Rands.chance(1)) {
			String test = name.toString();
			int tries = 3;
			while (tries > 0){
				int rng = Rands.randIntRange(1, specialLettersTesting.size());
				Pair<String, String> stringStringPair = specialLettersTesting.get(rng-1);
				String[] strings = stringStringPair.getSecond().split("\\|");
				rng = Rands.randIntRange(1, strings.length);
				StringBuilder aaa = new StringBuilder(test);
				if(aaa.indexOf(stringStringPair.getFirst())!=-1) aaa.setCharAt(aaa.indexOf(stringStringPair.getFirst()), strings[rng - 1].charAt(0));
				test = aaa.toString();
				tries--;
			}
			finalName = test;
		}
		return finalName;
	}

}
