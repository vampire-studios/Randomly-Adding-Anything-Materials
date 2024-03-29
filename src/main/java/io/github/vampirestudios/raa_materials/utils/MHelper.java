package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.Random;

public class MHelper {
	public static final float PI2 = (float) (Math.PI * 2);

	public static float wrap(float x, float y) {
	    return x - Mth.floor(x / y) * y;
	}

	public static int wrap(int x, int y) {
		return x < 0 ? (x - Mth.floor(x / y) + y) % y : x % y;
	}

	public static float lengthSqr(float x, float y, float z) {
		return x * x + y * y + z * z;
	}

	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}

	public static int randRange(int min, int max, Random random) {
		return min + random.nextInt() * (max - min);
	}

	public static int floor(float x) {
		return x < 0 ? (int) (x - 1) : (int) x;
	}

	public static int floor(double x) {
		return x < 0 ? (int) (x - 1) : (int) x;
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

	public static double lengthSqr(double x, double y, double z) {
		return x * x + y * y + z * z;
	}

	public static float length(float x, float y, float z) {
		return (float) Math.sqrt(lengthSqr(x, y, z));
	}

	public static double length(double x, double y, double z) {
		return Math.sqrt(lengthSqr(x, y, z));
	}

	public static float lengthSqr(float x, float y) {
		return x * x + y * y;
	}

	public static double lengthSqr(double x, double y) {
		return x * x + y * y;
	}

	public static float length(float x, float y) {
		return (float) Math.sqrt(lengthSqr(x, y));
	}

	public static double length(double x, double y) {
		return Math.sqrt(lengthSqr(x, y));
	}

	public static Vector3f cross(Vector3f vec1, Vector3f vec2) {
		float cx = vec1.y() * vec2.z() - vec1.z() * vec2.y();
		float cy = vec1.z() * vec2.x() - vec1.x() * vec2.z();
		float cz = vec1.x() * vec2.y() - vec1.y() * vec2.x();
		return new Vector3f(cx, cy, cz);
	}

}