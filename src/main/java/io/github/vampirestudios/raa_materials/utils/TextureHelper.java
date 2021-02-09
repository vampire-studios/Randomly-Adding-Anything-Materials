package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
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
		img.setPixelColor(x, y, color(r, g, b));
	}
	
	public static int color(int r, int g, int b) {
		return ALPHA | (b << 16) | (g << 8) | r;
	}
	
	public static int color(int r, int g, int b, int a) {
		return (a << 24) | (b << 16) | (g << 8) | r;
	}
	
	public static Identifier makeBlockTextureID(String name) {
		return RAAMaterials.id("block/" + name);
	}
	
	public static Identifier makeItemTextureID(String name) {
		return RAAMaterials.id("item/" + name);
	}

	public static Identifier makeArmorTextureID(String name) {
		return RAAMaterials.id("models/misc/armor/" + name);
	}
	
	public static CustomColor getFromTexture(NativeImage img, int x, int y) {
		return COLOR.forceRGB().set(img.getPixelColor(x, y));
	}

	public static CustomColor getFromTexture(BufferTexture img, float x, float y) {
		int x1 = MathHelper.floor(MHelper.wrap(x, img.getWidth()));
		int y1 = MathHelper.floor(MHelper.wrap(y, img.getHeight()));
		int x2 = (x1 + 1) % img.getWidth();
		int y2 = (y1 + 1) % img.getHeight();
		float deltaX = x - MathHelper.floor(x);
		float deltaY = y - MathHelper.floor(y);

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

		r1 = MathHelper.lerp(deltaX, r1, r2);
		g1 = MathHelper.lerp(deltaX, g1, g2);
		b1 = MathHelper.lerp(deltaX, b1, b2);
		a1 = MathHelper.lerp(deltaX, a1, a2);

		r2 = MathHelper.lerp(deltaX, r3, r4);
		g2 = MathHelper.lerp(deltaX, g3, g4);
		b2 = MathHelper.lerp(deltaX, b3, b4);
		a2 = MathHelper.lerp(deltaX, a3, a4);

		r1 = MathHelper.lerp(deltaY, r1, r2);
		g1 = MathHelper.lerp(deltaY, g1, g2);
		b1 = MathHelper.lerp(deltaY, b1, b2);
		a1 = MathHelper.lerp(deltaY, a1, a2);

		return COLOR.set(r1, g1, b1, a1);
	}

	public static NativeImage loadImage(Identifier name) {
		try {
			Resource input = MinecraftClient.getInstance().getResourceManager().getResource(Utils.appendAndPrependToPath(name, "textures/", ".png"));
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String name) {
		try {
			Identifier id = RAAMaterials.id(name);
			Resource input = MinecraftClient.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NativeImage loadImage(String namespace, String name) {
		try {
			Identifier id = new Identifier(namespace, name);
			Resource input = MinecraftClient.getInstance().getResourceManager().getResource(id);
			return NativeImage.read(input.getInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferTexture loadTexture(String name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(name)));
	}

	public static BufferTexture loadTexture(String namespace, String name) {
		return new BufferTexture(Objects.requireNonNull(loadImage(namespace, name)));
	}

	public static BufferTexture makeNoiseTexture(Random random) {
		BufferTexture texture = new BufferTexture(16, 16);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		COLOR.forceRGB().setAlpha(1F);
		float scale = MHelper.randRange(0.4F, 0.7F, random);
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				float nx = (float) x / texture.getWidth();
				float ny = (float) y / texture.getWidth();

				float px1 = x * scale;
				float py1 = y * scale;
				float px2 = (x - texture.getWidth()) * scale;
				float py2 = (y - texture.getHeight()) * scale;

				float v1 = (float) noise.eval(px1, py1) * 0.5F + 0.5F;
				float v2 = (float) noise.eval(px2, py1) * 0.5F + 0.5F;
				float v3 = (float) noise.eval(px1, py2) * 0.5F + 0.5F;
				float v4 = (float) noise.eval(px2, py2) * 0.5F + 0.5F;

				v1 = MathHelper.lerp(nx, v1, v2);
				v2 = MathHelper.lerp(nx, v3, v4);

				v1 = MathHelper.lerp(ny, v1, v2);
				COLOR.set(v1, v1, v1);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture makeNoiseTexture(Random random, float scale) {
		BufferTexture texture = new BufferTexture(16, 16);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		COLOR.forceRGB().setAlpha(1F);
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				float nx = (float) x / texture.getWidth();
				float ny = (float) y / texture.getWidth();

				float px1 = x * scale;
				float py1 = y * scale;
				float px2 = (x - texture.getWidth()) * scale;
				float py2 = (y - texture.getHeight()) * scale;

				float v1 = (float) noise.eval(px1, py1) * 0.5F + 0.5F;
				float v2 = (float) noise.eval(px2, py1) * 0.5F + 0.5F;
				float v3 = (float) noise.eval(px1, py2) * 0.5F + 0.5F;
				float v4 = (float) noise.eval(px2, py2) * 0.5F + 0.5F;

				v1 = MathHelper.lerp(nx, v1, v2);
				v2 = MathHelper.lerp(nx, v3, v4);

				v1 = MathHelper.lerp(ny, v1, v2);
				COLOR.set(v1, v1, v1);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}


	public static BufferTexture makeNoiseTexture(Random random, int side, float scale) {
		BufferTexture texture = new BufferTexture(side, side);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		COLOR.forceRGB().setAlpha(1F);
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				float nx = (float) x / side;
				float ny = (float) y / side;

				float px1 = x * scale;
				float py1 = y * scale;
				float px2 = (x - side) * scale;
				float py2 = (y - side) * scale;

				float v1 = (float) noise.eval(px1, py1) * 0.5F + 0.5F;
				float v2 = (float) noise.eval(px2, py1) * 0.5F + 0.5F;
				float v3 = (float) noise.eval(px1, py2) * 0.5F + 0.5F;
				float v4 = (float) noise.eval(px2, py2) * 0.5F + 0.5F;

				v1 = MathHelper.lerp(nx, v1, v2);
				v2 = MathHelper.lerp(nx, v3, v4);

				v1 = MathHelper.lerp(ny, v1, v2);
				COLOR.set(v1, v1, v1);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture blend(BufferTexture a, BufferTexture b, float mix) {
		BufferTexture result = new BufferTexture(a.getWidth(), a.getHeight());
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y));
				COLOR2.set(b.getPixel(x, y));
				if (COLOR2.getAlpha() > 0 && COLOR.getAlpha() > 0) {
					COLOR.mixWith(COLOR2, mix);
				}
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture cover(BufferTexture a, BufferTexture b) {
		BufferTexture result = new BufferTexture(a.getWidth(), a.getHeight());
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				int pixelA = a.getPixel(x, y);
				int pixelB = b.getPixel(x, y);
				COLOR.set(pixelA);
				if (COLOR.getAlpha() < 0.01F) {
					result.setPixel(x, y, COLOR);
				}
				else {
					COLOR2.set(pixelB);
					COLOR.set(pixelA).mixWith(COLOR2.set(pixelB), COLOR2.getAlpha());
				}
				result.setPixel(x, y, COLOR);
			}
		}
		return result;
	}

	public static BufferTexture outline(BufferTexture texture, CustomColor dark, CustomColor bright, int offsetX, int offsetY) {
		BufferTexture result = new BufferTexture(texture.getWidth(), texture.getHeight());
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
				float r = (float) MathHelper.floor(COLOR.getRed() * levels) / levels;
				float g = (float) MathHelper.floor(COLOR.getGreen() * levels) / levels;
				float b = (float) MathHelper.floor(COLOR.getBlue() * levels) / levels;
				COLOR.set(r, g, b);
				texture.setPixel(x, y, COLOR);
			}
		}
		return texture;
	}

	public static BufferTexture distort(BufferTexture texture, BufferTexture distortion, float amount) {
		BufferTexture result = new BufferTexture(texture.getWidth(), texture.getHeight());
		Vector3f dirX = new Vector3f();
		Vector3f dirY = new Vector3f();
		COLOR.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(distortion.getPixel(x, y));
				float h1 = COLOR.getRed();
				COLOR.set(distortion.getPixel((x + 1) % distortion.getWidth(), y));
				float h2 = COLOR.getRed();
				COLOR.set(distortion.getPixel(x, (y + 1) % distortion.getHeight()));
				float h3 = COLOR.getRed();
				dirX.set(1, h2 - h1, 1);
				dirY.set(1, h3 - h1, 1);
				dirX.normalize();
				dirY.normalize();
				dirX.cross(dirY);
				dirX.normalize();

				float dx = dirX.getX() * amount;
				float dy = dirX.getY() * amount;
				result.setPixel(x, y, getFromTexture(texture, x + dx, y + dy));
			}
		}
		return result;
	}

	public static BufferTexture applyGradient(BufferTexture texture, ColorGradient gradient) {
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				texture.setPixel(x, y, gradient.getColor(COLOR.getRed()).setAlpha(COLOR.getAlpha()));
			}
		}
		return texture;
	}

	public static BufferTexture heightPass(BufferTexture texture, int offsetX, int offsetY) {
		BufferTexture result = new BufferTexture(texture.getWidth(), texture.getHeight());
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				COLOR.set(texture.getPixel(x, y));
				COLOR2.set(texture.getPixel(MHelper.wrap(x + offsetX, texture.getWidth()), MHelper.wrap(y + offsetY, texture.getHeight())));
				float r = MathHelper.abs(COLOR.getRed() - COLOR2.getRed());
				float g = MathHelper.abs(COLOR.getGreen() - COLOR2.getGreen());
				float b = MathHelper.abs(COLOR.getBlue() - COLOR2.getBlue());
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
		BufferTexture result = new BufferTexture(a.getWidth(), a.getHeight());
		COLOR.forceRGB();
		COLOR2.forceRGB();
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				COLOR.set(a.getPixel(x, y));
				COLOR2.set(b.getPixel(x, y));
				float cr = COLOR.getRed() + COLOR2.getRed();
				float cg = COLOR.getGreen() + COLOR2.getGreen();
				float cb = COLOR.getBlue() + COLOR2.getBlue();
				result.setPixel(x, y, COLOR.set(cr, cg, cb));
			}
		}
		return result;
	}

	public static BufferTexture sub(BufferTexture a, BufferTexture b) {
		BufferTexture result = new BufferTexture(a.getWidth(), a.getHeight());
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
		BufferTexture result = new BufferTexture(texture.getWidth(), texture.getHeight());
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

	public static BufferTexture downScale(BufferTexture texture, int scale) {
		BufferTexture result = new BufferTexture(texture.getWidth() / scale, texture.getHeight() / scale);
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
				.setSaturation(colorStart.getSaturation() - satDist)
				.setBrightness(colorStart.getBrightness() - valDist);

		CustomColor colorEnd = new CustomColor().set(color).switchToHSV();
		colorEnd
				.setHue(colorEnd.getHue() + hueDist)
				.setSaturation(colorEnd.getSaturation() + satDist)
				.setBrightness(colorEnd.getBrightness() + valDist);

		return new ColorGradient(colorStart, colorEnd);
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
				disp += MathHelper.abs(v1 - v2);
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

	public static BufferTexture createOreTexture(ColorGradient gradient, Random random, NativeImage stone, NativeImage[] overlays) {
		BufferTexture texture = new BufferTexture(16, 16);
		NativeImage overlay = overlays[random.nextInt(overlays.length)];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(overlay, x, y);
				float r = color.getRed();
				float a = color.getAlpha();
				color = TextureHelper.getFromTexture(stone, x, y);
				if (a > 0) {
					color = gradient.getColor(r);
				}
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createColoredBlockTextures(ColorGradient gradient, Random random, NativeImage[] blocks) {
		BufferTexture texture = new BufferTexture(16, 16);
		NativeImage block = blocks[random.nextInt(blocks.length)];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(block, x, y);
				if (color.getAlpha() > 0) {
					color = gradient.getColor(color.getRed());
				}
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createColoredItemTextures(ColorGradient gradient, Random random, NativeImage[] items) {
		BufferTexture texture = new BufferTexture(16, 16);
		NativeImage item = items[random.nextInt(items.length)];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(item, x, y);
				if (color.getAlpha() > 0) {
					color = gradient.getColor(color.getRed());
				}
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createColoredArmorTextures(ColorGradient gradient, NativeImage armor) {
		BufferTexture texture = new BufferTexture(64, 32);
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 32; y++) {
				CustomColor color = TextureHelper.getFromTexture(armor, x, y);
				if (color.getAlpha() > 0) {
					color = gradient.getColor(color.getRed());
				}
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createColoredItemTexture(ColorGradient gradient, NativeImage item) {
		BufferTexture texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(item, x, y);
				if (color.getAlpha() > 0) {
					color = gradient.getColor(color.getRed());
				}
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createNonColoredItemTexture(NativeImage item) {
		BufferTexture texture = new BufferTexture(16, 16);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(item, x, y);
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

	public static BufferTexture createNonColoredItemTextures(Random random, NativeImage[] items) {
		BufferTexture texture = new BufferTexture(16, 16);
		NativeImage item = items[random.nextInt(items.length)];
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				CustomColor color = TextureHelper.getFromTexture(item, x, y);
				texture.setPixel(x, y, color);
			}
		}
		return texture;
	}

}