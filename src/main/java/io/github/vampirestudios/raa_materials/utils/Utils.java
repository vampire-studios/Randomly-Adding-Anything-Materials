package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.items.effects.MaterialEffects;
import net.minecraft.world.entity.ai.behavior.ShufflingList;

public class Utils {

	public static final ShufflingList<MaterialEffects> EFFECT_LIST = new ShufflingList<>();

	static {
		EFFECT_LIST.add(MaterialEffects.LIGHTNING, 2);
		EFFECT_LIST.add(MaterialEffects.EFFECT, 4);
		EFFECT_LIST.add(MaterialEffects.FIREBALL, 2);
		EFFECT_LIST.add(MaterialEffects.FREEZE, 1);
	}

}
