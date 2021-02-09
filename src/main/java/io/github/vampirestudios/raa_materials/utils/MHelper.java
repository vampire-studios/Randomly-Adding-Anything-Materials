package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class MHelper {
	public static final float PI2 = (float) (Math.PI * 2);

	public static float wrap(float x, float y) {
	    return x - MathHelper.floor(x / y) * y;
	}

	public static int wrap(int x, int y) {
		return x < 0 ? (x - MathHelper.floor(x / y) + y) % y : x % y;
	}

	public static float lengthSqr(float x, float y, float z) {
		return x * x + y * y + z * z;
	}

	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}

	public static float linearToAngle(float x) {
		return x * PI2;
	}

	public static float max(float a, float b) {
		return Math.max(a, b);
	}

	public static float min(float a, float b) {
		return Math.min(a, b);
	}

}