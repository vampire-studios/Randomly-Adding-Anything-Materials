package io.github.vampirestudios.raa_materials.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class ProceduralTextures {
	public static ColorGradient makeStonePalette(Random random) {
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
		return TextureHelper.makeDistortedPalette(color, hue, sat, val);
	}

	public static ColorGradient makeDirtPalette(Random random) {
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

	public static ColorGradient makeMetalPalette(Random random) {
		int type = random.nextInt(3);
		if (type == 0) {
			CustomColor color = new CustomColor(true)
					.setHue(Rands.randFloatRange(random, 0F, 1F))
					.setSaturation(Rands.randFloatRange(random, 0.1F, 0.7F))
					.setBrightness(Rands.randFloatRange(random, 0.3F, 0.8F));
			float cos = Mth.cos(color.getHue() * MHelper.PI2);
			float hue = (color.getHue()<0.66?-1:1) * cos * 0.14F;
			float sat = -0.35F;
			return TextureHelper.makeDistortedPalette(color, hue, sat, 0.6F);
		} else if (type == 1) {
			CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
			color.switchToHSV();
			color.setSaturation(Math.min(color.getSaturation(), 0.7F));
			return TextureHelper.makeDistortedPalette(color, 0.07F, -0.3F, 0.5F);
		} else {
			float value = random.nextBoolean() ? Rands.randFloatRange(random, 0.1F, 0.3F) : Rands.randFloatRange(random, 0.6F, 0.85F);
			CustomColor color = new CustomColor(value, value+0.01f, value).switchToHSV()
					.setHue(Rands.randFloatRange(random, 0F, 1F))
					.setSaturation(Rands.randFloatRange(random, 0F, 0.06F));
			return TextureHelper.makeDistortedPalette(color, Rands.randFloatRange(random, 0.02F, 0.2F), -0.06F, Rands.randFloatRange(random, 0.25F, 0.6F));
		}
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

	public static BufferTexture makeStoneTexture(Random random) {
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(random, 0.6F, 1.2F) / 4F);
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(random, 0.6F, 1.2F) / 4F);
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(random, 0.5F, 1.4F) / 4F);
		BufferTexture result = TextureHelper.distort(texture, distort, Rands.randFloatRange(random, 0F, 5F));
		BufferTexture pass = TextureHelper.heightPass(result, -1, -1);

		pass = TextureHelper.normalize(pass);
		result = TextureHelper.clamp(result, 9);
		result = TextureHelper.normalize(result);
		result = TextureHelper.blend(result, pass, 0.3F);
		result = TextureHelper.add(result, pass);
		result = TextureHelper.blend(result, additions, 0.3F);
		result = TextureHelper.normalize(result);
		result = TextureHelper.clamp(result, 8);

//		BufferTexture offseted1 = TextureHelper.offset(texture, -1, 0);
//		BufferTexture offseted2 = TextureHelper.offset(texture, 0, -1);
//		result = TextureHelper.blend(result, offseted1, 0.2F);
//		result = TextureHelper.blend(result, offseted2, 0.2F);

		result = TextureHelper.downScale(result, 4);
		result = TextureHelper.normalize(result, 0.05F, 0.85F);
		result = TextureHelper.clamp(result, 9);

//		result = TextureHelper.downScale(result, 4);
//		result = TextureHelper.normalize(result, 0.15F, 0.85F);
//		result = TextureHelper.clamp(result, 9);

		return result;
	}

	public static BufferTexture makeBlurredTexture(BufferTexture texture) {
		BufferTexture result = texture.clone();
		BufferTexture b1 = TextureHelper.offset(texture,  1,  0);
		BufferTexture b2 = TextureHelper.offset(texture, -1,  0);
		BufferTexture b3 = TextureHelper.offset(texture,  0,  1);
		BufferTexture b4 = TextureHelper.offset(texture,  0, -1);
		result = TextureHelper.blend(result, b1, 0.01F);
		result = TextureHelper.blend(result, b2, 0.01F);
		result = TextureHelper.blend(result, b3, 0.01F);
		result = TextureHelper.blend(result, b4, 0.01F);
		return result;
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay) {
		return TextureHelper.cover(texture, overlay.clone());
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay[random.nextInt(overlay.length)].clone(), gradient);
		return TextureHelper.cover(texture, over);
	}

	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, int levels) {
		BufferTexture over = TextureHelper.cover(texture, overlay);
		return TextureHelper.clamp(over, levels);
	}

	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, float... levels) {
		BufferTexture over = TextureHelper.cover(texture, overlay.clone());
		return TextureHelper.clampValue(over, levels);
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.clone(), gradient);
		return TextureHelper.cover(texture, over);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture overlay) {
		return TextureHelper.blend(texture, overlay.clone(), 0.5F);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay[random.nextInt(overlay.length)].clone(), gradient);
		return TextureHelper.blend(texture, over, 0.5F);
	}

	public static BufferTexture coverWithOverlayAndBlend(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.clone(), gradient);
		return TextureHelper.blend(texture, over, 0.5F);
	}

	public static BufferTexture randomColored(BufferTexture[] textures, ColorGradient gradient, Random random) {
		return TextureHelper.applyGradient(textures[random.nextInt(textures.length)].clone(), gradient);
	}

	public static BufferTexture randomColored(ResourceLocation texture, ColorGradient gradient) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		TextureHelper.normalize(textures, 0.35F, 1F);
		return TextureHelper.applyGradient(textures.clone(), gradient);
	}

	public static BufferTexture randomColored(ResourceLocation texture, ColorGradient gradient, float min, float max) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		TextureHelper.normalize(textures, min, max);
		return TextureHelper.applyGradient(textures.clone(), gradient);
	}

	public static BufferTexture randomColoredNoNormalize(ResourceLocation texture, ColorGradient gradient) {
		BufferTexture textures = TextureHelper.loadTexture(texture);
		return TextureHelper.applyGradient(textures.clone(), gradient);
	}

	public static BufferTexture randomColoredNoNormalize(BufferTexture texture, ColorGradient gradient) {
		return TextureHelper.applyGradient(texture.clone(), gradient);
	}

	public static BufferTexture randomColored(BufferTexture textures, ColorGradient gradient) {
		TextureHelper.normalize(textures, 0.35F, 1F);
		return TextureHelper.applyGradient(textures.clone(), gradient);
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