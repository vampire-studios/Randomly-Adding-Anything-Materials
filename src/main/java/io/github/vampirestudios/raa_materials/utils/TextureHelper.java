package io.github.vampirestudios.raa_materials.utils;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.math.Vector3f;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TextureHelper {
	private static final CustomColor COLOR = new CustomColor();
	private static final CustomColor COLOR2 = new CustomColor();
	private static final int ALPHA = 255 << 24;
	
	public static NativeImage makeTexture(int width, int height) {
		return new NativeImage(width, height, false);
	}
	
	public static NativeImage makeTexture(int side) {
		return makeTexture(side, side);
	}
	
	public static NativeImage makeTexture() {
		return makeTexture(16);
	}
	
	public static void setPixel(NativeImage img, int x, int y, int r, int g, int b) {
		img.setPixelRGBA(x, y, color(r, g, b));
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

	public static ResourceLocation makeArmorTextureID(String name) {
		return new ResourceLocation("models/armor/" + name);
	}
	
	public static CustomColor getFromTexture(NativeImage img, int x, int y) {
		return COLOR.forceRGB().set(img.getPixelRGBA(x, y));
	}

	public static CustomColor getFromTexture(BufferTexture img, float x, float y) {
		int x1 = Mth.floor(MHelper.wrap(x, img.getWidth()));
		int y1 = Mth.floor(MHelper.wrap(y, img.getHeight()));
		int x2 = (x1 + 1) % img.getWidth();
		int y2 = (y1 + 1) % img.getHeight();
		float deltaX = x - Mth.floor(x);
		float deltaY = y - Mth.floor(y);

		COLOR.forceRGB().set(img.getPixel(x1, y1));
		float r1 = COLOR.getRed();
		float g1 = COLOR.getGreen();
		float b1 = COLOR.getBlue();
		float a1 = COLOR.getAlpha();

		COLOR.set(img.getPixel(x2, y1));
		float r2 = COLOR.getRed();
		float g2 = COLOR.getGreen();
		float b2 = COLOR.getBlue();
		float a2 = COLOR.getAlpha();

		COLOR.set(img.getPixel(x1, y2));
		float r3 = COLOR.getRed();
		float g3 = COLOR.getGreen();
		float b3 = COLOR.getBlue();
		float a3 = COLOR.getAlpha();

		COLOR.set(img.getPixel(x2, y2));
		float r4 = COLOR.getRed();
		float g4 = COLOR.getGreen();
		float b4 = COLOR.getBlue();
		float a4 = COLOR.getAlpha();

		r1 = Mth.lerp(deltaX, r1, r2);
		g1 = Mth.lerp(deltaX, g1, g2);
		b1 = Mth.lerp(deltaX, b1, b2);
		a1 = Mth.lerp(deltaX, a1, a2);

		r2 = Mth.lerp(deltaX, r3, r4);
		g2 = Mth.lerp(deltaX, g3, g4);
		b2 = Mth.lerp(deltaX, b3, b4);
		a2 = Mth.lerp(deltaX, a3, a4);

		r1 = Mth.lerp(deltaY, r1, r2);
		g1 = Mth.lerp(deltaY, g1, g2);
		b1 = Mth.lerp(deltaY, b1, b2);
		a1 = Mth.lerp(deltaY, a1, a2);

		return COLOR.set(r1, g1, b1, a1);
	}

	public static NativeImage loadImage(ResourceLocation name) {
		try {
			Resource input = Minecraft.getInstance().getResourceManager().getResource(name);
			return NativeImage.read(input.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String name) {
		try {
			ResourceLocation id = RAAMaterials.id(name);
			Resource input = Minecraft.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String namespace, String name) {
		try {
			ResourceLocation id = new ResourceLocation(namespace, name);
			Resource input = Minecraft.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static AnimationMetadataSection loadAnimation(ResourceLocation name) {
		try {
			Resource input = Minecraft.getInstance().getResourceManager().getResource(name);
			AnimationMetadataSection animation = input.getMetadata(AnimationMetadataSection.SERIALIZER);
			return animation != null ? animation : AnimationMetadataSection.EMPTY ;
		}
		catch (IOException e) {
			return AnimationMetadataSection.EMPTY;
		}
	}

	public static AnimationMetadataSection loadAnimation(String name) {
		return loadAnimation(RAAMaterials.id(name));
	}

	public static AnimationMetadataSection loadAnimation(String namespace, String name) {
		return loadAnimation(new ResourceLocation(namespace, name));
	}

	public static BufferTexture loadTexture(String name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(name)), loadAnimation(name));
	}

	public static BufferTexture loadTexture(ResourceLocation name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(name)), loadAnimation(name));
	}

	public static BufferTexture makeNoiseTexture(Random random, int side, float scale) {
		BufferTexture texture = new BufferTexture(side, side);

		long seed = random.nextLong();
		double f = Math.PI * 2 / side;
		double r = side * scale * (1.0 / (Math.PI * 2));

		double[] sin = new double[side];
		double[] cos = new double[side];

		for (int t = 0; t < side; t++) { sin[t] = Math.sin(t * f) * r; }
		for (int t = 0; t < side; t++) { cos[t] = Math.cos(t * f) * r; }

		COLOR.forceRGB().setAlpha(1F);
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				float value = OpenSimplex2S.noise4_Fallback(seed, sin[x], cos[x], sin[y], cos[y]);
				COLOR.set(value, value, value);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture blend(BufferTexture a, BufferTexture b, float mix) {
		BufferTexture result = a.clone();
		if (a.width > b.width) a = TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) a = TextureHelper.upScale(a, b.width / a.width);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y));
				COLOR2.set(b.getPixel(x, y));
				if (COLOR2.getAlpha() > 0 && COLOR.getAlpha() > 0) {
					COLOR.mixWith(COLOR2, mix,false,true);
				}
				if (COLOR2.getAlpha() > 0 && (COLOR.getAlpha() <= 0)) {
					COLOR.set(COLOR2);
				}
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture cover(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.clone();
		if (a.width > b.width) a = TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) a = TextureHelper.upScale(a, b.width / a.width);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				int pixelA = a.getPixel(x, y);
				int pixelB = b.getPixel(x, y);
				COLOR.set(pixelA);
				COLOR2.set(pixelB);
				if (COLOR.getAlpha() < 0.01F) {
					result.setPixel(x, y, COLOR);
				} else {
					COLOR.set(pixelA).mixWith(COLOR2.set(pixelB), COLOR2.getAlpha());
				}
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture combine(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.clone();
		if (a.width > b.width) a = TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) a = TextureHelper.upScale(a, b.width / a.width);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				int pixelA = a.getPixel(x, y);
				int pixelB = b.getPixel(x, y);
				COLOR.set(pixelA);
				COLOR2.set(pixelB);
				COLOR.set(pixelA).alphaBlend(COLOR2).copyMaxAlpha(COLOR2);
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture outline(BufferTexture texture, CustomColor dark, CustomColor bright, int offsetX, int offsetY) {
		BufferTexture result = texture.clone();
		BufferTexture darkOffset = offset(texture, offsetX, offsetY);
		BufferTexture lightOffset = offset(texture, -offsetX, -offsetY);
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(lightOffset.getPixel(x, y));
				if (COLOR.getAlpha() > 0) {
					COLOR.set(texture.getPixel(x, y));
					if (COLOR.getAlpha() == 0) {
						result.setPixel(x, y, bright);
						continue;
					}
				}
				COLOR.set(darkOffset.getPixel(x, y));
				if (COLOR.getAlpha() > 0) {
					COLOR.set(texture.getPixel(x, y));
					if (COLOR.getAlpha() == 0) {
						result.setPixel(x, y, dark);
					}
				}
			}
		}
		return result;
	}

	public static BufferTexture clamp(BufferTexture texture, int levels) {
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				float r = (float) Mth.floor(COLOR.getRed() * levels) / levels;
				float g = (float) Mth.floor(COLOR.getGreen() * levels) / levels;
				float b = (float) Mth.floor(COLOR.getBlue() * levels) / levels;
				float a = (float) Mth.floor(COLOR.getAlpha() * levels) / levels;
				COLOR.set(r, g, b, a);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture clampValue(BufferTexture texture, float[] levels) {
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
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
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static float[] getValues(BufferTexture texture) {
		ArrayList<Float> list = new ArrayList<>();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR.switchToHSV();
				float v = COLOR.getBrightness();
				if(!list.contains(v))list.add(v);
				COLOR.forceRGB();
			}
		}
		float[] out = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			out[i] = list.get(i);
		}
		return out;
	}

	public static BufferTexture distort(BufferTexture texture, BufferTexture distortion, float amount) {
		BufferTexture result = texture.clone();
		if (texture.width > distortion.width) texture = TextureHelper.downScale(texture, texture.width / distortion.width);
		if (texture.width < distortion.width) texture = TextureHelper.upScale(texture, distortion.width / texture.width);
		Vector3f dirX = new Vector3f();
		Vector3f dirY = new Vector3f();
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
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
				result.setPixel(x, y, getFromTexture(texture, x + dx, y + dy));
			}
		}
		return result;
	}

	public static BufferTexture applyGradient(BufferTexture texture, ColorGradient gradient) {
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				texture.setPixel(x, y, COLOR2.set(gradient.getColor(COLOR.getRed())).setAlpha(COLOR.getAlpha()));
			}
		}
		return texture;
	}

	public static BufferTexture heightPass(BufferTexture texture, int offsetX, int offsetY) {
		BufferTexture result = texture.clone();
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR2.set(texture.getPixel(MHelper.wrap(x + offsetX, texture.getWidth()), MHelper.wrap(y + offsetY, texture.getHeight())));
				float r = Mth.abs(COLOR.getRed() - COLOR2.getRed());
				float g = Mth.abs(COLOR.getGreen() - COLOR2.getGreen());
				float b = Mth.abs(COLOR.getBlue() - COLOR2.getBlue());
				result.setPixel(x, y, COLOR.set(r, g, b));
			}
		}
		return result;
	}

	public static BufferTexture normalize(BufferTexture texture) {
		float minR = 1;
		float minG = 1;
		float minB = 1;
		float normR = 0;
		float normG = 0;
		float normB = 0;
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				normR = MHelper.max(normR, COLOR.getRed());
				normG = MHelper.max(normG, COLOR.getGreen());
				normB = MHelper.max(normB, COLOR.getBlue());
				minR = MHelper.min(minR, COLOR.getRed());
				minG = MHelper.min(minG, COLOR.getGreen());
				minB = MHelper.min(minB, COLOR.getBlue());
			}
		}
		normR = (normR == 0 ? 1 : normR) - minR;
		normG = (normG == 0 ? 1 : normG) - minG;
		normB = (normB == 0 ? 1 : normB) - minB;
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR.setRed((COLOR.getRed() - minR) / normR);
				COLOR.setGreen((COLOR.getGreen() - minG) / normG);
				COLOR.setBlue((COLOR.getBlue() - minB) / normB);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture normalize(BufferTexture texture, float min, float max) {
		float delta = max - min;
		float minR = 1;
		float minG = 1;
		float minB = 1;
		float normR = 0;
		float normG = 0;
		float normB = 0;
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				normR = MHelper.max(normR, COLOR.getRed());
				normG = MHelper.max(normG, COLOR.getGreen());
				normB = MHelper.max(normB, COLOR.getBlue());
				minR = MHelper.min(minR, COLOR.getRed());
				minG = MHelper.min(minG, COLOR.getGreen());
				minB = MHelper.min(minB, COLOR.getBlue());
			}
		}
		normR = (normR == 0 ? 1 : normR) - minR;
		normG = (normG == 0 ? 1 : normG) - minG;
		normB = (normB == 0 ? 1 : normB) - minB;
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR.setRed((COLOR.getRed() - minR) / normR * delta + min);
				COLOR.setGreen((COLOR.getGreen() - minG) / normG * delta + min);
				COLOR.setBlue((COLOR.getBlue() - minB) / normB * delta + min);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture add(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.clone();
		if (a.width > b.width) a = TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) a = TextureHelper.upScale(a, b.width / a.width);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y));
				COLOR2.set(b.getPixel(x, y));
				float cr = COLOR.getRed() + COLOR2.getRed();
				float cg = COLOR.getGreen() + COLOR2.getGreen();
				float cb = COLOR.getBlue() + COLOR2.getBlue();
				float ca = COLOR.getAlpha() + COLOR2.getAlpha();
				result.setPixel(x, y, COLOR.set(cr, cg, cb, ca));
			}
		}
		return result;
	}

	public static BufferTexture sub(BufferTexture a, BufferTexture b) {
		BufferTexture result = a.clone();
		if (a.width > b.width) a = TextureHelper.downScale(a, a.width / b.width);
		if (a.width < b.width) a = TextureHelper.upScale(a, b.width / a.width);
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y));
				COLOR2.set(b.getPixel(x, y));
				float cr = COLOR.getRed() - COLOR2.getRed();
				float cg = COLOR.getGreen() - COLOR2.getGreen();
				float cb = COLOR.getBlue() - COLOR2.getBlue();
				result.setPixel(x, y, COLOR.set(cr, cg, cb));
			}
		}
		return result;
	}

	public static BufferTexture offset(BufferTexture texture, int offsetX, int offsetY) {
		BufferTexture result = texture.clone();
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(MHelper.wrap(x + offsetX, texture.getWidth()), MHelper.wrap(y + offsetY, texture.getHeight())));
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture invert(BufferTexture texture) {
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR.setRed(1 - COLOR.getRed());
				COLOR.setGreen(1 - COLOR.getGreen());
				COLOR.setBlue(1 - COLOR.getBlue());
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture upScale(BufferTexture texture, int scale) {
		BufferTexture result = texture.clone();
		result.upscale(scale);

		for (int x = 0; x < result.getWidth(); x++) {
			int px = x * scale;
			for (int y = 0; y < result.getHeight(); y++) {
				int py = y * scale;
				result.setPixel(x, y, getAverageColor(texture, px, py, scale, scale));
			}
		}
		return result;
	}

	public static BufferTexture downScale(BufferTexture texture, int scale) {
		BufferTexture result = texture.clone();
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

	public static ColorGradient makeDistortedPalette(CustomColor color, float hueDist, float satDist, float valDist) {
		CustomColor colorStart = new CustomColor().set(color).switchToHSV();
		colorStart
				.setHue(colorStart.getHue() - hueDist)
				.setSaturation(Mth.clamp(colorStart.getSaturation() - satDist, 0.01F, 1F))
				.setBrightness(Mth.clamp(colorStart.getBrightness() - valDist, 0.07F, 0.55F));
		CustomColor colorMid = new CustomColor().set(color).switchToHSV();
		colorStart
				.setHue(colorStart.getHue() )
				.setSaturation(Mth.clamp(colorStart.getSaturation() , 0.03F, 1F))
				.setBrightness(Mth.clamp(colorStart.getBrightness() , 0.1F, 0.6F));
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
		colorStart
				.setHue(colorStart.getHue() )
				.setSaturation(Mth.clamp(colorStart.getSaturation() , 0.0F, 1F))
				.setBrightness(Mth.clamp(colorStart.getBrightness() , 0.0F, 1F));
		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
				.setHue(colorEnd.getHue() + forHue)
				.setSaturation(Mth.clamp(colorEnd.getSaturation() + forSat, 0.01F, 1F))
				.setBrightness(Mth.clamp(colorEnd.getBrightness() + forVal, 0.5F, 1F));
		return new ColorGradient(colorStart, colorMid, colorEnd);
	}

	public static CustomColor getAverageColor(BufferTexture texture, int x, int y, int r) {
		float cr = 0;
		float cg = 0;
		float cb = 0;

		COLOR.forceRGB();
		for (int px = -r; px <= r; px ++) {
			int posX = MHelper.wrap(x + px, texture.getWidth());
			for (int py = -r; py <= r; py ++) {
				int posY = MHelper.wrap(y + py, texture.getHeight());
				COLOR.set(texture.getPixel(posX, posY));
				cr += COLOR.getRed();
				cg += COLOR.getGreen();
				cb += COLOR.getBlue();
			}
		}

		int count = r * 2 + 1;
		count *= count;
		return COLOR.set(cr / count, cg / count, cb / count);
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

	public static float fakeDispersion(BufferTexture texture) {
		int count = 0;
		float disp = 0;
		COLOR.forceRGB();
		for (int x = 1; x < texture.getWidth(); x += 2) {
			for (int y = 1; y < texture.getHeight(); y += 2) {
				COLOR.set(texture.getPixel(x, y));
				float v1 = COLOR.switchToHSV().getBrightness();
				float v2 = getAverageColor(texture, x, y, 1).switchToHSV().getBrightness();
				disp += Mth.abs(v1 - v2);
				count ++;
			}
		}
		return disp / (float) count;
	}

	public static ColorGradient makeSoftPalette(CustomColor color) {
		CustomColor colorStart = new CustomColor().set(color).switchToHSV();

		colorStart
				.setHue(colorStart.getHue() - 0.05F)
				.setSaturation(colorStart.getSaturation() * 0.5F)
				.setBrightness(colorStart.getBrightness() - 0.3F);

		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
				.setHue(colorEnd.getHue() + 0.05F)
				.setSaturation(colorEnd.getSaturation() * 0.5F)
				.setBrightness(colorEnd.getBrightness() + 0.3F);

		return new ColorGradient(colorStart, colorEnd);
	}

}