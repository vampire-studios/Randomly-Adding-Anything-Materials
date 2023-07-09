package io.github.vampirestudios.raa_materials.utils;

import com.mojang.blaze3d.platform.NativeImage;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.testing.CloverNoise;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.util.TriConsumer;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;

public class TextureHelper {
	private static final CustomColor COLOR = new CustomColor();
	private static final CustomColor COLOR2 = new CustomColor();
	private static final int ALPHA = 255 << 24;
	
	public static NativeImage makeTexture(int width, int height) {
		return new NativeImage(width, height, false);
	}

	public static int color(int r, int g, int b) {
		return ALPHA | (b << 16) | (g << 8) | r;
	}
	
	public static int color(int r, int g, int b, int a) {
		return (a << 24) | (b << 16) | (g << 8) | r;
	}
	
	public static ResourceLocation makeBlockTextureID(String name) {
		return RAAMaterials.id("block/" + name);
	}
	
	public static ResourceLocation makeItemTextureID(String name) {
		return RAAMaterials.id("item/" + name);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public static CustomColor getFromTexture(BufferTexture img, float x, float y) {
		int x1 = Mth.floor(MHelper.wrap(x, img.getWidth()));
		int y1 = Mth.floor(MHelper.wrap(y, img.getHeight()));
		int x2 = (x1 + 1) % img.getWidth();
		int y2 = (y1 + 1) % img.getHeight();
		float deltaX = x - Mth.floor(x);
		float deltaY = y - Mth.floor(y);

		float[] color1 = getColorComponents(img.getPixel(x1, y1));
		float[] color2 = getColorComponents(img.getPixel(x2, y1));
		float[] color3 = getColorComponents(img.getPixel(x1, y2));
		float[] color4 = getColorComponents(img.getPixel(x2, y2));

		float r = Mth.lerp(deltaY, Mth.lerp(deltaX, color1[0], color2[0]), Mth.lerp(deltaX, color3[0], color4[0]));
		float g = Mth.lerp(deltaY, Mth.lerp(deltaX, color1[1], color2[1]), Mth.lerp(deltaX, color3[1], color4[1]));
		float b = Mth.lerp(deltaY, Mth.lerp(deltaX, color1[2], color2[2]), Mth.lerp(deltaX, color3[2], color4[2]));
		float a = Mth.lerp(deltaY, Mth.lerp(deltaX, color1[3], color2[3]), Mth.lerp(deltaX, color3[3], color4[3]));

		return COLOR.set(r, g, b, a);
	}

	private static float[] getColorComponents(int pixel) {
		COLOR.set(pixel);
		return new float[] { COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), COLOR.getAlpha() };
	}

	public static NativeImage loadImage(ResourceLocation name) {
		try(InputStream input = Minecraft.getInstance().getResourceManager().getResourceOrThrow(name).open()) {
			return NativeImage.read(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String name) {
		ResourceLocation id = RAAMaterials.id(name);
		try(InputStream input = Minecraft.getInstance().getResourceManager().getResourceOrThrow(id).open()) {
			return NativeImage.read(input);
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static AnimationMetadataSection loadAnimation(ResourceLocation name) {
		try {
			Resource input = Minecraft.getInstance().getResourceManager().getResourceOrThrow(name);
			Optional<AnimationMetadataSection> animation = input.metadata().getSection(AnimationMetadataSection.SERIALIZER);
			return animation.orElse(AnimationMetadataSection.EMPTY);
		}
		catch (IOException e) {
			return AnimationMetadataSection.EMPTY;
		}
	}

	public static AnimationMetadataSection loadAnimation(String name) {
		return loadAnimation(RAAMaterials.id(name));
	}

	public static BufferTexture loadTexture(String name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(name)), loadAnimation(name));
	}

	public static BufferTexture loadTexture(ResourceLocation name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(name)), loadAnimation(name));
	}

	// add this helper method for DRY principle
	public static BufferTexture ensureSameSize(BufferTexture a, BufferTexture b) {
		if (a.width > b.width) return TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) return TextureHelper.upScale(a, b.width / a.width);
		return a;
	}

	private static void forEachPixel(BufferTexture texture, TriConsumer<BufferTexture, Integer, Integer> function) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				function.accept(texture, x, y);
			}
		}
	}

	public static BufferTexture makeNoiseTexture(Random random, int side, float scale) {
		BufferTexture texture = new BufferTexture(side, side);

		long seed = random.nextLong();
		CloverNoise.Noise2D noise2D = new CloverNoise.Noise2D(seed); // create instance once outside loop
		double f = Math.PI * 2 / side;
		double r = side * scale * (1.0 / (Math.PI * 2));

		double[] sin = new double[side];
		double[] cos = new double[side];

		for (int t = 0; t < side; t++) { sin[t] = Math.sin(t * f) * r; }
		for (int t = 0; t < side; t++) { cos[t] = Math.cos(t * f) * r; }

		COLOR.forceRGB().setAlpha(1F);
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				float value = (float) noise2D.fractalNoise(sin[x], cos[y], Rands.randIntRange(2, 4)); // use instance
				COLOR.set(value, value, value);
				texture.setPixel(x, y, COLOR);
			}
		}

		return texture;
	}

	public static BufferTexture blend(BufferTexture a, BufferTexture b, float mix) {
		BufferTexture result = a.cloneTexture();
		a = ensureSameSize(a, b);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		forEachPixel(a, (texture, x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR2.set(b.getPixel(x, y));
			if (COLOR2.getAlpha() > 0 && COLOR.getAlpha() > 0) {
				COLOR.mixWith(COLOR2, mix,false,true);
			}
			if (COLOR2.getAlpha() > 0 && (COLOR.getAlpha() <= 0)) {
				COLOR.set(COLOR2);
			}
			result.setPixel(x, y, COLOR);
		});
		return result;
	}

	public static BufferTexture cover(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.cloneTexture();
		a = ensureSameSize(a, b);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		forEachPixel(a, (texture, x, y) -> {
			int pixelA = texture.getPixel(x, y);
			int pixelB = b.getPixel(x, y);
			COLOR.set(pixelA);
			COLOR2.set(pixelB);

			if (COLOR.getAlpha() < 0.01F) result.setPixel(x, y, COLOR);
			else COLOR.set(pixelA).mixWith(COLOR2.set(pixelB), COLOR2.getAlpha());

			result.setPixel(x, y, COLOR);
		});
		return result;
	}

	public static BufferTexture combine(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.cloneTexture();
		a = ensureSameSize(a, b);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		forEachPixel(a, (texture, x, y) -> {
			int pixelA = texture.getPixel(x, y);
			int pixelB = b.getPixel(x, y);
			COLOR.set(pixelA);
			COLOR2.set(pixelB);
			COLOR.set(pixelA).alphaBlend(COLOR2).copyMaxAlpha(COLOR2);
			result.setPixel(x, y, COLOR);
		});
		return result;
	}

	public static BufferTexture outline(BufferTexture texture, CustomColor dark, CustomColor bright, int offsetX, int offsetY) {
		BufferTexture result = texture.cloneTexture();
		BufferTexture darkOffset = offset(texture, offsetX, offsetY);
		BufferTexture lightOffset = offset(texture, -offsetX, -offsetY);
		COLOR.forceRGB();
		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(lightOffset.getPixel(x, y));
			if (COLOR.getAlpha() > 0) {
				COLOR.set(texture1.getPixel(x, y));
				if (COLOR.getAlpha() == 0) {
					result.setPixel(x, y, bright);
					return;
				}
			}
			COLOR.set(darkOffset.getPixel(x, y));
			if (COLOR.getAlpha() > 0) {
				COLOR.set(texture1.getPixel(x, y));
				if (COLOR.getAlpha() == 0) {
					result.setPixel(x, y, dark);
				}
			}
		});
		return result;
	}

	public static BufferTexture clamp(BufferTexture texture, int levels) {
		COLOR.forceRGB();
		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(texture1.getPixel(x, y));
			float r = (float) Mth.floor(COLOR.getRed() * levels) / levels;
			float g = (float) Mth.floor(COLOR.getGreen() * levels) / levels;
			float b = (float) Mth.floor(COLOR.getBlue() * levels) / levels;
			float a = (float) Mth.floor(COLOR.getAlpha() * levels) / levels;
			COLOR.set(r, g, b, a);
			texture1.setPixel(x, y, COLOR);
		});
		return texture;
	}

	public static BufferTexture clampValue(BufferTexture texture, float[] levels) {
		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(texture1.getPixel(x, y));
			COLOR.switchToHSV();
			float h = 0;
			float s = 0;
			float v = COLOR.getBrightness();
			float clamped = 4f;
			for (int i = 1; i < levels.length; i++) {
				float temp = Mth.abs(v - levels[i-1]) < Mth.abs(v - levels[i]) ? levels[i-1] : levels[i];
				clamped = Mth.abs(v-clamped) <= Mth.abs(v - temp)? clamped : temp;
			}
			v = clamped;
			float a = COLOR.getAlpha();
			COLOR.set(h, s, v, a);
			COLOR.switchToRGB();
			texture1.setPixel(x, y, COLOR);
		});
		return texture;
	}

	public static float[] getValues(BufferTexture texture) {
		ArrayList<Float> list = new ArrayList<>();
		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(texture1.getPixel(x, y));
			COLOR.switchToHSV();
			float v = COLOR.getBrightness();
			if (!list.contains(v)) list.add(v);
			COLOR.forceRGB();
		});
		float[] out = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			out[i] = list.get(i); // manually fill the array
		}
		return out;
	}

	public static BufferTexture distort(BufferTexture texture, BufferTexture distortion, float amount) {
		BufferTexture result = texture.cloneTexture();
		if (texture.width > distortion.width) texture = TextureHelper.downScale(texture, texture.width / distortion.width);
		if (texture.width < distortion.width) texture = TextureHelper.upScale(texture, distortion.width / texture.width);
		Vector3f dirX = new Vector3f();
		Vector3f dirY = new Vector3f();
		COLOR.forceRGB();

		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(distortion.getPixel(x, y));
			float h1 = COLOR.getRed();
			COLOR.set(distortion.getPixel((x + 1) % distortion.getWidth(), y));
			float h2 = COLOR.getGreen();
			COLOR.set(distortion.getPixel(x, (y + 1) % distortion.getHeight()));
			float h3 = COLOR.getBlue();
			dirX.set(1, h2 - h1, 1);
			dirY.set(1, h3 - h1, 1);
			dirX.normalize();
			dirY.normalize();
			dirX.cross(dirY);
			dirX.normalize();

			float dx = dirX.x() * amount;
			float dy = dirX.y() * amount;
			result.setPixel(x, y, getFromTexture(texture1, x + dx, y + dy));
		});
		return result;
	}

	public static BufferTexture applyGradient(BufferTexture texture, ColorGradient gradient) {
		forEachPixel(texture, (texture1, x, y) -> {
			COLOR.set(texture1.getPixel(x, y));
			texture1.setPixel(x, y, COLOR2.set(gradient.getColor(COLOR.getRed())).setAlpha(COLOR.getAlpha()));
		});
		return texture;
	}

	public static BufferTexture heightPass(BufferTexture texture, int offsetX, int offsetY) {
		BufferTexture result = texture.cloneTexture();
		COLOR.forceRGB();
		COLOR2.forceRGB();
		forEachPixel(texture, (texture, x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR2.set(texture.getPixel(MHelper.wrap(x + offsetX, texture.getWidth()), MHelper.wrap(y + offsetY, texture.getHeight())));
			float r = Mth.abs(COLOR.getRed() - COLOR2.getRed());
			float g = Mth.abs(COLOR.getGreen() - COLOR2.getGreen());
			float b = Mth.abs(COLOR.getBlue() - COLOR2.getBlue());
			result.setPixel(x, y, COLOR.set(r, g, b));
		});
		return result;
	}

	public static BufferTexture simpleTileable(BufferTexture texture) {
		BufferTexture tex = texture.cloneTexture();
		BufferTexture result = offset(tex, tex.getWidth() / 2, tex.getHeight() / 2);
		result = blend(tex, result, 0.5f);
		return result;
	}

	public static BufferTexture edgeTileable(BufferTexture texture, int edgeSize) {
		return edgeTileable(texture, edgeSize, 0f, 0);
	}

	public static BufferTexture edgeTileable(BufferTexture texture, int edgeSize, float blur, int blurRadius) { // todo use some sorta blur on the edges
		BufferTexture tex = offset(texture, texture.getWidth()/2, texture.getHeight()/2);
		BufferTexture alpha = new BufferTexture(texture.getWidth(), texture.getHeight());
		BufferTexture alpha2 = new BufferTexture(texture.getWidth(), texture.getHeight());
		BufferTexture result = texture.cloneTexture();

		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				if(x < edgeSize || x > texture.getWidth() - edgeSize-1 || y < edgeSize || y > texture.getHeight() - edgeSize - 1){
					alpha.setPixel(x, y, COLOR.set(tex.getPixel(x,y)));
				}else alpha.setPixel(x, y, COLOR.set(tex.getPixel(x,y)).setAlpha(0f));
			}
		}
		for (int x = 0; x < tex.getWidth(); x++) {
			for (int y = 0; y < tex.getHeight(); y++) {
				COLOR.set(alpha.getPixel(x, y)).mixWith(getAverageAlpha(alpha, x, y, blurRadius), blur, false, false);
				alpha2.setPixel(x, y, COLOR);
			}
		}
		normalizeAlpha(alpha2);
		for (int x = 0; x < tex.getWidth(); x++) {
			for (int y = 0; y < tex.getHeight(); y++) {
				result.setPixel(x, y, COLOR.set(result.getPixel(x, y)).alphaBlend(COLOR2.set(alpha2.getPixel(x, y))));
			}
		}

		result = offset(result, tex.getWidth()/2, tex.getHeight()/2);
		return result;
	}

	/*
	 * Very slightly faster way of blurring textures than ProceduralTextures.MakeBlurredTexture(BufferTexture texture, float strength, int steps)
	 * Effectively the same when steps are low, exponentially faster for each blur step;
	 */
	private static void addColor(BufferTexture tex, int x, int y, float[] colorAccumulator) {
		COLOR.set(tex.getPixel(MHelper.wrap(x, tex.getWidth()), MHelper.wrap(y, tex.getHeight())));
		colorAccumulator[0] += COLOR.getRed();
		colorAccumulator[1] += COLOR.getGreen();
		colorAccumulator[2] += COLOR.getBlue();
		colorAccumulator[3] += COLOR.getAlpha();
	}

	public static BufferTexture blur(BufferTexture texture, float strength, int steps) {
		BufferTexture tex;
		BufferTexture result = texture.cloneTexture();

		float[] colorAccumulator = new float[4]; // cr, cg, cb, ca

		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int i = 0; i < steps; i++) {
			tex = result.cloneTexture();
			for (int x = 0; x < tex.getWidth(); x++) {
				for (int y = 0; y < tex.getHeight(); y++) {
					Arrays.fill(colorAccumulator, 0f);
					addColor(tex, x - 1, y, colorAccumulator);
					addColor(tex, x, y - 1, colorAccumulator);
					addColor(tex, x + 1, y, colorAccumulator);
					addColor(tex, x, y + 1, colorAccumulator);

					result.setPixel(x, y, COLOR.set(tex.getPixel(x, y))
							.mixWith(COLOR2.set(colorAccumulator[0] / 4, colorAccumulator[1] / 4,
									colorAccumulator[2] / 4, colorAccumulator[3] / 4), strength));
				}
			}
		}
		return result;
	}

	/*
	 * Faster that the "step"/loop based blur methods
	*/
	public static BufferTexture blur(BufferTexture tex, float strength, int width, int height) {
		BufferTexture result = tex.cloneTexture();

		COLOR2.forceRGB();
		forEachPixel(tex, (texture, x, y) -> result.setPixel(
				x, y,
				COLOR2.set(tex.getPixel(x, y)).mixWith(
						getAverageColor(texture, x-(width/2), y-(height/2), width, height),
						strength
				)
		));

		return result;
	}

	public static BufferTexture normalizeAlpha(BufferTexture texture) {
		final float[] minA = {1};
		final float[] normA = {0};
		COLOR.forceRGB();

		// Calculate minA and normA
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			normA[0] = MHelper.max(normA[0], COLOR.getAlpha());
			minA[0] = MHelper.min(minA[0], COLOR.getAlpha());
		});

		normA[0] = (normA[0] == 0 ? 1 : normA[0]) - minA[0];

		// Normalize alpha values
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR.setAlpha((COLOR.getAlpha() - minA[0]) / normA[0]);
			texture.setPixel(x, y, COLOR);
		});

		return texture;
	}
	public static BufferTexture normalize(BufferTexture texture) {
		final float[] minR = {1};
		final float[] minG = {1};
		final float[] minB = {1};
		final float[] normR = {0};
		final float[] normG = {0};
		final float[] normB = {0};
		COLOR.forceRGB();
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			normR[0] = MHelper.max(normR[0], COLOR.getRed());
			normG[0] = MHelper.max(normG[0], COLOR.getGreen());
			normB[0] = MHelper.max(normB[0], COLOR.getBlue());
			minR[0] = MHelper.min(minR[0], COLOR.getRed());
			minG[0] = MHelper.min(minG[0], COLOR.getGreen());
			minB[0] = MHelper.min(minB[0], COLOR.getBlue());
		});
		normR[0] = (normR[0] == 0 ? 1 : normR[0]) - minR[0];
		normG[0] = (normG[0] == 0 ? 1 : normG[0]) - minG[0];
		normB[0] = (normB[0] == 0 ? 1 : normB[0]) - minB[0];
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR.setRed((COLOR.getRed() - minR[0]) / normR[0]);
			COLOR.setGreen((COLOR.getGreen() - minG[0]) / normG[0]);
			COLOR.setBlue((COLOR.getBlue() - minB[0]) / normB[0]);
			texture.setPixel(x, y, COLOR);
		});
		return texture;
	}

	public static BufferTexture normalize(BufferTexture texture, float min, float max) {
		float delta = max - min;
		final float[] minR = {1};
		final float[] minG = {1};
		final float[] minB = {1};
		final float[] normR = {0};
		final float[] normG = {0};
		final float[] normB = {0};
		COLOR.forceRGB();
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			normR[0] = MHelper.max(normR[0], COLOR.getRed());
			normG[0] = MHelper.max(normG[0], COLOR.getGreen());
			normB[0] = MHelper.max(normB[0], COLOR.getBlue());
			minR[0] = MHelper.min(minR[0], COLOR.getRed());
			minG[0] = MHelper.min(minG[0], COLOR.getGreen());
			minB[0] = MHelper.min(minB[0], COLOR.getBlue());
		});
		normR[0] = (normR[0] == 0 ? 1 : normR[0]) - minR[0];
		normG[0] = (normG[0] == 0 ? 1 : normG[0]) - minG[0];
		normB[0] = (normB[0] == 0 ? 1 : normB[0]) - minB[0];
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR.setRed((COLOR.getRed() - minR[0]) / normR[0] * delta + min);
			COLOR.setGreen((COLOR.getGreen() - minG[0]) / normG[0] * delta + min);
			COLOR.setBlue((COLOR.getBlue() - minB[0]) / normB[0] * delta + min);
			texture.setPixel(x, y, COLOR);
		});
		return texture;
	}

	public static BufferTexture add(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.cloneTexture();
		a = ensureSameSize(a, b);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		BufferTexture finalA = a;
		forEachPixel(a, (x, y) -> {
			COLOR.set(finalA.getPixel(x, y));
			COLOR2.set(b.getPixel(x, y));
			float cr = COLOR.getRed() + COLOR2.getRed();
			float cg = COLOR.getGreen() + COLOR2.getGreen();
			float cb = COLOR.getBlue() + COLOR2.getBlue();
			float ca = COLOR.getAlpha() + COLOR2.getAlpha();
			result.setPixel(x, y, COLOR.set(cr, cg, cb, ca));
		});
		return result;
	}

	public static BufferTexture sub(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.cloneTexture();
		a = ensureSameSize(a, b);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		forEachPixel(a, (x, y) -> {
			COLOR.set(a.getPixel(x, y));
			COLOR2.set(b.getPixel(x, y));
			float cr = COLOR.getRed() - COLOR2.getRed();
			float cg = COLOR.getGreen() - COLOR2.getGreen();
			float cb = COLOR.getBlue() - COLOR2.getBlue();
			result.setPixel(x, y, COLOR.set(cr, cg, cb));
		});
		return result;
	}

	public static BufferTexture offset(BufferTexture texture, int offsetX, int offsetY) {
		BufferTexture result = texture.cloneTexture();
		COLOR.forceRGB();
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(MHelper.wrap(x + offsetX, texture.getWidth()), MHelper.wrap(y + offsetY, texture.getHeight())));
			result.setPixel(x, y, COLOR);
		});
		return result;
	}

	public static BufferTexture invert(BufferTexture texture) {
		COLOR.forceRGB();
		forEachPixel(texture, (x, y) -> {
			COLOR.set(texture.getPixel(x, y));
			COLOR.setRed(1 - COLOR.getRed());
			COLOR.setGreen(1 - COLOR.getGreen());
			COLOR.setBlue(1 - COLOR.getBlue());
			texture.setPixel(x, y, COLOR);
		});
		return texture;
	}

	public static BufferTexture upScale(BufferTexture texture, int scale) {
		BufferTexture result = texture.cloneTexture();
		result.upscale(scale);

		for (int x = 0; x < result.getWidth(); x++) {
			int px = x / scale;
			for (int y = 0; y < result.getHeight(); y++) {
				int py = y / scale;
				result.setPixel(x, y, getAverageColor(texture, px, py, scale, scale));
			}
		}
		return result;
	}

	public static BufferTexture downScale(BufferTexture texture, int scale) {
		BufferTexture result = texture.cloneTexture();
		result.downscale(scale);

		for (int x = 0; x < result.getWidth(); x++) {
			int px = x * scale;
			for (int y = 0; y < result.getHeight(); y++) {
				int py = y * scale;
				result.setPixel(x, y, getAverageColor(texture, px, py, scale, scale));
			}
		}
		return result;
	}

	public static BufferTexture downScaleCrop(BufferTexture texture, int scale) {
		BufferTexture result = texture.cloneTexture();
		result.downscale(scale);

		for (int x = 0; x < result.getWidth(); x++) {
			for (int y = 0; y < result.getHeight(); y++) {
				result.setPixel(x, y, new CustomColor(texture.getPixel(x, y)));
			}
		}
		return result;
	}

	public static ColorGradient makeDistortedPalette(CustomColor color, float hueDist, float satDist, float valDist) {
		CustomColor colorStart = new CustomColor().set(color).switchToHSV();
		colorStart
				.setHue(colorStart.getHue() - hueDist)
				.setSaturation(Mth.clamp(colorStart.getSaturation() - satDist, 0.01F, 1F))
				.setBrightness(Mth.clamp(colorStart.getBrightness() - valDist, 0.07F, 0.55F));
		CustomColor colorMid = new CustomColor().set(color).switchToHSV();
		colorMid
				.setHue(colorMid.getHue() )
				.setSaturation(Mth.clamp(colorMid.getSaturation() , 0.03F, 1F))
				.setBrightness(Mth.clamp(colorMid.getBrightness() , 0.1F, 0.6F));
		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
				.setHue(colorEnd.getHue() + hueDist)
				.setSaturation(Mth.clamp(colorEnd.getSaturation() + satDist, 0.01F, 0.5F))
				.setBrightness(Mth.clamp(colorEnd.getBrightness() + valDist, 0.5F, 1F));
		return new ColorGradient(colorStart, colorMid, colorEnd);
	}

	public static ColorGradient makeDualDistPalette(CustomColor color, float backHue, float backSat, float backVal, float forHue, float forSat, float forVal) {
		CustomColor colorStart = new CustomColor().set(color).switchToHSV();
		colorStart
				.setHue(colorStart.getHue() - backHue)
				.setSaturation(Mth.clamp(colorStart.getSaturation() - backSat, 0.01F, 1F))
				.setBrightness(Mth.clamp(colorStart.getBrightness() - backVal, 0.07F, 0.55F));
		CustomColor colorMid = new CustomColor().set(color).switchToHSV();
		colorMid
				.setHue(colorMid.getHue() )
				.setSaturation(Mth.clamp(colorMid.getSaturation() , 0.0F, 1F))
				.setBrightness(Mth.clamp(colorMid.getBrightness() , 0.0F, 1F));
		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
				.setHue(colorEnd.getHue() + forHue)
				.setSaturation(Mth.clamp(colorEnd.getSaturation() + forSat, 0.01F, 1F))
				.setBrightness(Mth.clamp(colorEnd.getBrightness() + forVal, 0.5F, 1F));
		return new ColorGradient(colorStart, colorMid, colorEnd);
	}

	public static CustomColor getAverageAlpha(BufferTexture texture, int x, int y, int r) {
		float ca = 0;

		COLOR.forceRGB();

		for (int px = -r; px <= r; px ++) {
			int posX = MHelper.wrap(x + px, texture.getWidth());
			for (int py = -r; py <= r; py ++) {
				int posY = MHelper.wrap(y + py, texture.getHeight());
				COLOR.set(texture.getPixel(posX, posY));
				ca += COLOR.getAlpha();
			}
		}

		int count = r * 2 + 1;
		count *= count;
		return COLOR.set(texture.getPixel(x,y)).setAlpha( ca / count);
	}

	public static CustomColor getAverageColor(BufferTexture texture, int x, int y, int r) {
		float cr = 0;
		float cg = 0;
		float cb = 0;
		float ca = 0;

		COLOR.forceRGB();
		for (int px = -r; px <= r; px ++) {
			int posX = MHelper.wrap(x + px, texture.getWidth());
			for (int py = -r; py <= r; py ++) {
				int posY = MHelper.wrap(y + py, texture.getHeight());
				COLOR.set(texture.getPixel(posX, posY));
				cr += COLOR.getRed();
				cg += COLOR.getGreen();
				cb += COLOR.getBlue();
				ca += COLOR.getAlpha();
			}
		}

		int count = r * 2 + 1;
		count *= count;
		return COLOR.set(cr / count, cg / count, cb / count, ca / count);
	}

	public static CustomColor getAverageColor(BufferTexture texture, int x, int y, int width, int height) {
		float cr = 0;
		float cg = 0;
		float cb = 0;

		COLOR.forceRGB();
		for (int px = 0; px < width; px ++) {
			int posX = MHelper.wrap(x + px, texture.getWidth());
			for (int py = 0; py < height; py ++) {
				int posY = MHelper.wrap(y + py, texture.getHeight());
				COLOR.set(texture.getPixel(posX, posY));
				cr += COLOR.getRed();
				cg += COLOR.getGreen();
				cb += COLOR.getBlue();
			}
		}

		int count = width * height;
		return COLOR.set(cr / count, cg / count, cb / count);
	}

}