package io.github.vampirestudios.raa_materials.api.namegeneration;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.vampirelib.utils.Rands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class TestNameGenerator {
	private static final List<String> generatedNames = new ArrayList<>();

	public static void main(String[] args) {
		for (int i = 0; i < 500; i++) {
			String name = generateOreName();
			System.out.println(name);
		}
	}

	public static String generateOreName() {
		String name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		while(generatedNames.contains(name.replaceAll("'|`|\\^| |´|£|&|\\(|\\)|¤|%|!|\\?|\\+|-|.|;|:|,", ""))) {
			name = generate(Rands.chance(50) ? (Rands.chance(100) ? 2 : 3) : (Rands.chance(100) ? 3 : 4), Rands.chance(30) ? 20 : 12);
		}
		generatedNames.add(name);
		return name;
	}

	public static String generateStoneName() {
		String name = generate(5, 15);
		while(generatedNames.contains(name)) {
			name = generate(5, 15);
		}
		generatedNames.add(name);
		return name;
	}

	public static String generate(int min, int max) {
		String vowels = "aeiou", characters = "qwrtypsdfghjklzxcvbnm", mutations = "'`^ ´£&()¤%!?+-.;:,";
		List<Pair<String, String>> specialLettersTesting = List.of(
				Pair.of("a", "àä"),
				Pair.of("e", "éèë"),
				Pair.of("c", "ç"),
				Pair.of("i", "ï"),
				Pair.of("o", "ö"),
				Pair.of("u", "ùü")
		);
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
				int rng = Rands.randIntRange(1, 19);
				name.append(mutations, rng - 1, rng);
			} else if (Rands.randIntRange(1, 100) >= 75) {
				int rng = Rands.randIntRange(1, 4);
				name.append(vowels, rng - 1, rng);
			} else {
				int rng = Rands.randIntRange(1, 20);
				name.append(characters, rng - 1, rng);
			}
		}

		String finalName;

		if (Rands.chance(40)) {
			AtomicReference<String> finalFinalName = new AtomicReference<>("");
			specialLettersTesting.forEach(stringStringPair -> {
				int rng = Rands.randIntRange(1, stringStringPair.getSecond().length());
				char[] character = stringStringPair.getSecond().toCharArray();
				finalFinalName.set(name.toString().replace(stringStringPair.getFirst(), Character.toString(character[rng - 1])));
			});
			finalName = finalFinalName.get();
		} else {
			finalName = name.toString();
		}
		return finalName;
	}

}
