package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class ProceduralTextures {
	public static ColorGradient makeStonePalette(CustomColor color, Random random) {
		float sat = MHelper.randRange(0, 0.2F, random);
		float val = MHelper.randRange(0.3F, 0.7F, random);
		color.switchToHSV().setSaturation(sat).setBrightness(val);
		float hue = MHelper.randRange(0, 0.3F, random);
		sat = random.nextFloat() * 0.3F;
		val = MHelper.randRange(0.3F, 0.5F, random);
		return TextureHelper.makeDistortedPalette(color, hue, sat, val);
	}

	public static ColorGradient makeMetalPalette(Random random) {
		int type = random.nextInt(3);
		if (type == 0) {
			CustomColor color = new CustomColor(true)
					.setHue(Rands.randFloatRange(0F, 1F))
					.setSaturation(MHelper.randRange(0.1F, 0.7F, random))
					.setBrightness(MHelper.randRange(0.3F, 0.8F, random));
			float cos = Mth.cos(color.getHue() * MHelper.PI2);
			float hue = (color.getHue()<0.66?-1:1) * cos * 0.14F;
			float sat = -0.35F;
			return TextureHelper.makeDistortedPalette(color, hue, sat, 0.6F);
		}
		else if (type == 1) {
			CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
			color.switchToHSV();
			color.setSaturation(Math.min(color.getSaturation(), 0.7F));
			return TextureHelper.makeDistortedPalette(color, 0.07F, -0.3F, 0.5F);
		}
		else {
			float value = random.nextBoolean() ? MHelper.randRange(0.1F, 0.3F, random) : MHelper.randRange(0.6F, 0.85F, random);
			CustomColor color = new CustomColor(value, value+0.01f, value).switchToHSV()
					.setHue(Rands.randFloatRange(0F, 1F))
					.setSaturation(MHelper.randRange(0F, 0.06F, random));
			return TextureHelper.makeDistortedPalette(color, MHelper.randRange(0.02F, 0.2F, random), -0.06F, MHelper.randRange(0.25F, 0.6F, random));
		}
	}

	public static ColorGradient makeGemPalette(Random random) {
//		CustomColor color = new CustomColor(true)
//				.setHue(Rands.randFloatRange(0F, 1F))
//				.setSaturation(MHelper.randRange(0.6F, 1, random))
//				.setBrightness(MHelper.randRange(0.3F, 1, random));
		CustomColor color = new CustomColor(true)
				.setHue(Rands.randFloatRange(0F, 1F))
				.setSaturation(Rands.randFloatRange(0.25F, 0.8F))
				.setBrightness(Rands.randFloatRange(0.4F, 0.85F));
		float cos = Mth.cos(color.getHue() * MHelper.PI2);
		float hue = (color.getHue()<0.66?-1:1) * cos * 0.16F;
		float sat = -MHelper.randRange(0.35F, 0.8F, random);
		return TextureHelper.makeDistortedPalette(color, hue, sat, 1.2F);
	}

	public static ColorGradient makeCrystalPalette(Random random) {
//		CustomColor color = new CustomColor(true)
//				.setHue(Rands.randFloatRange(0F, 1F))
//				.setSaturation(MHelper.randRange(0.4F, 1F, random))
//				.setBrightness(MHelper.randRange(0.3F, 0.85F, random));
		CustomColor color = new CustomColor(true)
				.setHue(Rands.randFloatRange(0F, 1F))
				.setSaturation(Rands.randFloatRange(0.5F, 0.8F))
				.setBrightness(Rands.randFloatRange(0.65F, 0.7F));
		float cos = Mth.cos(color.getHue() * MHelper.PI2);
		float hue = (color.getHue()<0.66?-1:1) * cos * 0.14F;
		float sat = -MHelper.randRange(0.5F, 0.8F, random);
		return TextureHelper.makeDistortedPalette(color, hue, sat, 0.7F);
	}

	public static BufferTexture makeStoneTexture(ColorGradient gradient, Random random) {
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.4F, 0.6F, random) / 4F);
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.4F, 0.8F, random) / 4F);
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, 64, MHelper.randRange(0.5F, 1.0F, random) / 4F);
		BufferTexture result = TextureHelper.distort(texture, distort, MHelper.randRange(0F, 5F, random));
		BufferTexture pass = TextureHelper.heightPass(result, -1, -1);

		pass = TextureHelper.normalize(pass);
		result = TextureHelper.clamp(result, 8);
		result = TextureHelper.normalize(result);
		result = TextureHelper.blend(result, pass, 0.3F);
		result = TextureHelper.add(result, pass);
		result = TextureHelper.blend(result, additions, 0.3F);
		result = TextureHelper.normalize(result);
		result = TextureHelper.clamp(result, 7);

		BufferTexture offseted1 = TextureHelper.offset(texture, -1, 0);
		BufferTexture offseted2 = TextureHelper.offset(texture, 0, -1);
		result = TextureHelper.blend(result, offseted1, 0.2F);
		result = TextureHelper.blend(result, offseted2, 0.2F);

		result = TextureHelper.downScale(result, 4);
		result = TextureHelper.normalize(result, 0.15F, 0.85F);
		result = TextureHelper.clamp(result, 8);

		result = TextureHelper.applyGradient(result, gradient);

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

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.clone(), gradient);
		return TextureHelper.cover(texture, over);
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay) {
		return TextureHelper.blend(texture, overlay.clone(), 0.5F);
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay[random.nextInt(overlay.length)].clone(), gradient);
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