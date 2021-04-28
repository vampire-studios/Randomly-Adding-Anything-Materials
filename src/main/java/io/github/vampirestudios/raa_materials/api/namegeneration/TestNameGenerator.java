package io.github.vampirestudios.raa_materials.api.namegeneration;

import io.github.vampirestudios.vampirelib.utils.Rands;

import java.util.Locale;

public class TestNameGenerator {

	public static String generateOreName() {
		return generate(Rands.chance(50) ? 2 : 3, Rands.chance(30) ? 20 : 12);
	}

	public static String generateStoneName() {
		return generate(5, 15);
	}

	public static String generate(int min, int max) {
		String vowels = "aeiou", characters = "qwrtypsdfghjklzxcvbnm";
		StringBuilder name = new StringBuilder();
		for (int i = 1; i < Rands.randIntRange(min, max); i++) {
			if ((i == 1)) {
				if (Rands.randIntRange(1, 100) >= 50) {
					int rng = Rands.randIntRange(1, 4);
					name.append(vowels.substring(rng-1, rng).toUpperCase(Locale.ROOT));
					i++;
				} else {
					int rng = Rands.randIntRange(1, 20);
					name.append(characters.substring(rng-1, rng).toUpperCase(Locale.ROOT));
				}
			}
			else if (((i % 2) == 0)) {
				int rng = Rands.randIntRange(1, 4);
				name.append(vowels, rng-1, rng);
			}
			else if (Rands.randIntRange(1, 100) >= 75) {
				int rng = Rands.randIntRange(1, 4);
				name.append(vowels, rng-1, rng);
			}
			else {
				int rng = Rands.randIntRange(1, 20);
				name.append(characters, rng-1, rng);
			}
		}
		return name.toString();
	}

}
