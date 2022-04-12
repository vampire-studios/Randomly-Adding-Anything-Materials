package io.github.vampirestudios.raa_materials.utils;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class ProceduralTextures {
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
		}else if (type == 1) {
			CustomColor color = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat())
					.switchToHSV();
			color.setSaturation(Math.min(color.getSaturation(), 0.6F)).setBrightness(Math.min(color.getBrightness(), 0.6F));
			return makeCorrodedPalette(TextureHelper.makeDualDistPalette(color, 0.08F, -0.3F, 0.5F, 0.08F, -0.5F, 0.6F));
		}else {
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

		start.setSaturation(start.getSaturation() * 0.5f + 0.4f)
				.setBrightness((start.getBrightness() - intersect.getBrightness()) / 1.1f + start.getBrightness());
		float hue = (start.getHue() - intersect.getHue());
		hue = (0.0f - (hue / Math.abs(hue))) * Math.max(Math.abs(hue), 0.3f) + start.getHue();
		start.setHue(hue);

		end.setSaturation(end.getSaturation() * 0.5f + 0.15f)
				.setBrightness((end.getBrightness() - intersect.getBrightness()) / 1.04f + intersect.getBrightness());
		hue = (end.getHue() - intersect.getHue());
		hue = (0.0f - (hue / Math.abs(hue))) * Math.max(Math.abs(hue), 0.35f) + intersect.getHue();
		end.setHue(hue);

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
		BufferTexture texture = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.6F, 1.2F) / 4F);
		BufferTexture distort = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.6F, 1.2F) / 4F);
		BufferTexture additions = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.5F, 1.4F) / 4F);
		BufferTexture result = TextureHelper.distort(texture, distort, Rands.randFloatRange(0F, 8F));
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

		if (RAAMaterials.CONFIG.textureSize < result.width)
			result = TextureHelper.downScale(result, result.width / RAAMaterials.CONFIG.textureSize);
		else result = TextureHelper.upScale(result, RAAMaterials.CONFIG.textureSize / result.width);

		result = TextureHelper.normalize(result, 0.1F, 0.7F);
		result = TextureHelper.clampValue(result, values);

		return result;
	}

	public static BufferTexture makeBlurredTexture(BufferTexture texture) {
		BufferTexture result = texture.cloneTexture();
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
		return TextureHelper.cover(texture, overlay.cloneTexture());
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture[] overlay, Random random, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay[random.nextInt(overlay.length)].cloneTexture(), gradient);
		return TextureHelper.cover(texture, over);
	}

	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, int levels) {
		BufferTexture over = TextureHelper.cover(texture, overlay);
		return TextureHelper.clamp(over, levels);
	}

	public static BufferTexture clampCoverWithOverlay(BufferTexture texture, BufferTexture overlay, float... levels) {
		if (texture.width > overlay.width) texture = TextureHelper.downScale(texture, texture.width / overlay.width);
		if (texture.width < overlay.width) texture = TextureHelper.upScale(texture, overlay.width / texture.width);
		BufferTexture over = TextureHelper.cover(texture, overlay.cloneTexture());
		return TextureHelper.clampValue(over, levels);
	}

	public static BufferTexture coverWithOverlay(BufferTexture texture, BufferTexture overlay, ColorGradient gradient) {
		BufferTexture over = TextureHelper.applyGradient(overlay.cloneTexture(), gradient);
		return TextureHelper.cover(texture, over);
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