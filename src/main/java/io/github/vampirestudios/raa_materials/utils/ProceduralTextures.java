package io.github.vampirestudios.raa_materials.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class ProceduralTextures {

	private static final float HUE_LOWER_BOUND = 0F;
	private static final float HUE_UPPER_BOUND = 1F;
	private static final float SAT_LOWER_BOUND = 0.07f;
	private static final float SAT_UPPER_BOUND = 0.4f;

	private static final int DEFAULT_SIZE = 16 * 4;
	private static final float DEFAULT_MIN_BRIGHTNESS = 0.1F;
	private static final float DEFAULT_MAX_BRIGHTNESS = 0.8F;

	private static CustomColor createCustomColor(final Random random, final float satLowerBound, final float satUpperBound) {
		CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
		float sat = Rands.randFloatRange(random, satLowerBound, satUpperBound);
		float val = Rands.randFloatRange(random, 0.3F, 0.7F);
		color.switchToHSV().setSaturation(sat).setBrightness(val);
		return color;
	}

	public static ColorGradient makeStonePalette(Random random) {
		Rands.setRand(random);
		CustomColor color = new CustomColor(true)
				.setHue(Rands.randFloatRange( 0F, 1f))
				.setSaturation(Rands.randFloatRange( 0.07f, 0.4f))
				.setBrightness(Rands.randFloatRange( 0.3F, 0.8F));
		return TextureHelper.makeDualDistPalette(color,
				Rands.randFloatRange(-0.15f, 0.15f),
				Rands.randFloatRange(0.2f, -0.3f),
				Rands.randFloatRange(0.4f, 0.6f),
				Rands.randFloatRange(-0.1f, 0.2f),
				Rands.randFloatRange(-0.1f, -0.3f),
				Rands.randFloatRange(0.2f, 0.4f)
		);
	}

	public static ColorGradient makeDirtPalette(Random random) {
		CustomColor color = createCustomColor(random, SAT_LOWER_BOUND, SAT_UPPER_BOUND);
		float hue = Rands.randFloatRange(random, 0, 0.3F);
		float sat = random.nextFloat() * 0.3F;
		float val = Rands.randFloatRange(random, 0.3F, 0.5F);
		return TextureHelper.makeDistortedPalette(color, hue, sat, val);
	}

	public static Pair<ColorGradient, ColorGradient> makeWoodPalette(Random random) {
		CustomColor color = new CustomColor(
				random.nextFloat(),
				random.nextFloat(),
				random.nextFloat()
		);
		float sat = Rands.randFloatRange(random, 0, 0.2F);
		float val = Rands.randFloatRange(random, 0.3F, 0.7F);
		color.switchToHSV().setSaturation(sat).setBrightness(val);
		float hue = Rands.randFloatRange(random, 0, 0.3F);
		sat = random.nextFloat() * 0.3F;
		val = Rands.randFloatRange(random, 0.3F, 0.5F);
		ColorGradient outerGradient = TextureHelper.makeDistortedPalette(color, hue, sat, val);
		color.setBrightness(Rands.randFloatRange(random, 0.5F, 0.9F));
		ColorGradient innerGradient = TextureHelper.makeDistortedPalette(color, hue, sat, val);

		return Pair.of(outerGradient, innerGradient);
	}

	public static ColorDualGradient makeMetalPalette(Random random) {
		Rands.setRand(random);
		int type = random.nextInt(3);
		if (type == 0) {
			CustomColor color = new CustomColor(true)
					.setHue(Rands.randFloatRange(0F, 1F))
					.setSaturation(Rands.randFloatRange(0.1F, 0.7F))
					.setBrightness(Rands.randFloatRange(0.3F, 0.8F));
			float cos = Mth.cos(color.getHue() * MHelper.PI2);
			float hue = (color.getHue() < 0.66 ? -1 : 1) * cos * 0.14F;
			float sat = -0.35F;
			return makeCorrodedPalette(TextureHelper.makeDistortedPalette(color, hue, sat, 0.6F));
		} else if (type == 1) {
			CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat())
					.switchToHSV();
			color.setSaturation(Math.min(color.getSaturation(), 0.6F)).setBrightness(Math.min(color.getBrightness(), 0.6F));
			return makeCorrodedPalette(TextureHelper.makeDualDistPalette(color, 0.08F, -0.3F, 0.5F, 0.08F, -0.5F, 0.6F));
		} else {
			float value = random.nextBoolean() ? Rands.randFloatRange(0.2F, 0.4F) : Rands.randFloatRange(0.6F, 0.75F);
			CustomColor color = new CustomColor(value, value + 0.01f, value).switchToHSV()
					.setHue(Rands.randFloatRange(0F, 1F))
					.setSaturation(Rands.randFloatRange(0.06F, 0.1F));
			return makeCorrodedPalette(TextureHelper.makeDistortedPalette(color, Rands.randFloatRange(0.02F, 0.2F), -0.3F, Rands.randFloatRange(0.25F, 0.6F)));
		}
	}

	public static ColorDualGradient makeCorrodedPalette(ColorGradient grad) {
		CustomColor start = new CustomColor().set(grad.getColor(0f)).switchToHSV();
		CustomColor intersect = new CustomColor().set(grad.getColor(0.5f)).switchToHSV();
		CustomColor end = new CustomColor().set(grad.getColor(1f)).switchToHSV();

		start.setSaturation(start.getSaturation() * 0.5f + 0.45f)
			.setBrightness((start.getBrightness() - intersect.getBrightness()) / 1.1f + intersect.getBrightness());
		float hue = (intersect.getHue() - start.getHue());
		hue = (0.0f - (hue / Math.abs(hue))) * Math.max(Math.abs(hue), 0.35f) * 2f + intersect.getHue();
		hue += 0.2f;
		start.setHue(hue);

		end.setSaturation(end.getSaturation() * 0.5f + 0.15f)
			.setBrightness((end.getBrightness() - intersect.getBrightness()) / 1.04f + intersect.getBrightness());
		hue = (end.getHue() - intersect.getHue());
		hue = (0.0f - (hue / Math.abs(hue))) * Math.max(Math.abs(hue), 0.35f) * 1.6f + intersect.getHue();
		hue -= 0.2f;
		end.setHue(hue);

		intersect.setHue(intersect.getHue()*2.4f).setSaturation(intersect.getSaturation() * 0.5f + 0.5f)
			.switchToRGB().mixWith(grad.getColor(0.5f).switchToRGB(),0.5f);

		ColorGradient corrosion = new ColorGradient(start, intersect, end).switchToRGB();

		return new ColorDualGradient(grad, corrosion);
	}

	public static ColorGradient makeGemPalette(Random random) {
		CustomColor color = new CustomColor(true)
				.setHue(Rands.randFloatRange(random, 0F, 1F))
				.setSaturation(Rands.randFloatRange(random, 0.25F, 0.8F))
				.setBrightness(Rands.randFloatRange(random, 0.4F, 0.85F));
		float cos = Mth.cos(color.getHue() * MHelper.PI2);
		float hue = (color.getHue()<0.66?-1:1) * cos * 0.16F;
		float sat = -Rands.randFloatRange(random, 0.35F, 0.8F);
		return TextureHelper.makeDistortedPalette(color, hue, sat, 1.2F);
	}

	public static ColorGradient makeCrystalPalette(Random random) {
		CustomColor color = new CustomColor(true)
				.setHue(Rands.randFloatRange(random, 0F, 1F))
				.setSaturation(Rands.randFloatRange(random, 0.5F, 0.8F))
				.setBrightness(Rands.randFloatRange(random, 0.65F, 0.7F));
		float cos = Mth.cos(color.getHue() * MHelper.PI2);
		float hue = (color.getHue()<0.66?-1:1) * cos * 0.14F;
		float sat = -Rands.randFloatRange(random, 0.5F, 0.8F);
		return TextureHelper.makeDistortedPalette(color, hue, sat, 0.7F);
	}


	public static BufferTexture makeStoneTexture(float[] values, Random random) {
		Rands.setRand(random);
		int size = DEFAULT_SIZE;
		BufferTexture highFreq = TextureHelper.simpleTileable(TextureHelper.makeNoiseTexture(random, size, Rands.randFloatRange(1F, 1.6F) / (size/16F)));
		BufferTexture midFreq = TextureHelper.edgeTileable(TextureHelper.downScaleCrop(
				TextureHelper.offset(highFreq, Rands.randInt(size), Rands.randInt(size)),
				4),4,0.96f, (size/16));
		BufferTexture lowFreq = TextureHelper.upScale(TextureHelper.edgeTileable(TextureHelper.downScaleCrop(
				TextureHelper.offset(highFreq, Rands.randInt(size), Rands.randInt(size)),
				8),4, 0.95f, (size/16)/2),2);

		midFreq = TextureHelper.blur(midFreq, 0.1f, Rands.randIntRange(2, 6), Rands.randIntRange(2, 6));

		BufferTexture pass = TextureHelper.heightPass(midFreq, -1, -1);
		TextureHelper.normalize(pass);
		TextureHelper.clampValue(midFreq, values);
		TextureHelper.normalize(midFreq);

		midFreq = TextureHelper.blend(midFreq, pass, 0.2F);
		midFreq = TextureHelper.add(midFreq, pass);
		BufferTexture result = TextureHelper.blend(
				TextureHelper.blur(lowFreq, 0.5f, 2),
				TextureHelper.blend(midFreq, TextureHelper.downScale(highFreq,4),
						0.3f),0.65f);
		BufferTexture hrm = TextureHelper.blur(pass, 0.5f, 2, 2);

		hrm = TextureHelper.add(result, hrm);
		result = TextureHelper.blend(result, hrm, 0.15F);
		TextureHelper.normalize(result, 0.1F, 0.8F);
		TextureHelper.clampValue(result, values);
		return result;
	}

	// existing overloaded methods
	public static BufferTexture makeBlurredTexture(BufferTexture texture) {
		return makeBlurredTexture(texture, 0.01F, 4);
	}

	public static BufferTexture makeBlurredTexture(BufferTexture texture, float strength, int steps) {
		BufferTexture result = texture.cloneTexture();
		for (int i = 0; i < steps; i++) {
			result = blendWithOffsets(result, strength, 1, 0, -1, 0, 0, 1, 0, -1);
		}
		return result;
	}

	private static BufferTexture blendWithOffsets(BufferTexture texture, float blendStrength, int... offsets) {
		BufferTexture result = texture.cloneTexture();
		for (int i = 0; i < offsets.length; i += 2) {
			result = TextureHelper.blend(result, TextureHelper.offset(result, offsets[i], offsets[i + 1]), blendStrength);
		}
		return result;
	}


	// existing overloaded methods
	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay) {
		return TextureHelper.cover(texture, overlay.cloneTexture());
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		return coverWithOverlay(texture, overlay[random.nextInt(overlay.length)].cloneTexture(), gradient);
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		return TextureHelper.cover(texture, TextureHelper.applyGradient(overlay.cloneTexture(), gradient));
	}

	// existing overloaded methods
	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, int levels) {
		return TextureHelper.clamp(TextureHelper.cover(texture, overlay), levels);
	}

	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, float... levels) {
		if (texture.width > overlay.width) texture = TextureHelper.downScale(texture, texture.width / overlay.width);
		if (texture.width < overlay.width) texture = TextureHelper.upScale(texture, overlay.width / texture.width);
		return TextureHelper.clampValue(TextureHelper.cover(texture, overlay.cloneTexture()), levels);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture overlay) {
		return TextureHelper.blend(texture, overlay.cloneTexture(), 0.5F);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay[random.nextInt(overlay.length)].cloneTexture(), gradient);
		return TextureHelper.blend(texture, over, 0.5F);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.cloneTexture(), gradient);
		return TextureHelper.blend(texture, over, 0.5F);
	}

	public static BufferTexture randomColored(BufferTexture[] textures, ColorGradient gradient, Random random) {
		return TextureHelper.applyGradient(textures[random.nextInt(textures.length)].cloneTexture(), gradient);
	}

	public static BufferTexture randomColored(ResourceLocation texture, ColorGradient gradient) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		TextureHelper.normalize(textures, 0.35F, 1F);
		return TextureHelper.applyGradient(textures.cloneTexture(), gradient);
	}

	public static BufferTexture randomColored(ResourceLocation texture, ColorGradient gradient, float min, float max) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		TextureHelper.normalize(textures, min, max);
		return TextureHelper.applyGradient(textures.cloneTexture(), gradient);
	}

	public static BufferTexture randomColoredNoNormalize(ResourceLocation texture, ColorGradient gradient) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		return TextureHelper.applyGradient(textures.cloneTexture(), gradient);
	}

	public static BufferTexture randomColoredNoNormalize(BufferTexture texture, ColorGradient gradient) {
		return TextureHelper.applyGradient(texture.cloneTexture(), gradient);
	}

	public static BufferTexture randomColored(BufferTexture textures, ColorGradient gradient) {
		TextureHelper.normalize(textures, 0.35F, 1F);
		return TextureHelper.applyGradient(textures.cloneTexture(), gradient);
	}

	public static BufferTexture nonColored(BufferTexture[] textures, Random random) {
		return textures[random.nextInt(textures.length)];
	}

	public static BufferTexture nonColored(BufferTexture texture) {
		return texture;
	}

	public static BufferTexture nonColored(ResourceLocation texture) {
//		BufferTexture textures = TextureHelper.loadTexture(texture);
//		TextureHelper.normalize(textures, 0.35F, 1F);
		return TextureHelper.loadTexture(texture);
	}
}