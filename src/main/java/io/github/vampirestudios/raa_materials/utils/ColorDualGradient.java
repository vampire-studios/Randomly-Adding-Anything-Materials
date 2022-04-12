package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.util.Mth;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public record ColorDualGradient(ColorGradient gradient1,
								ColorGradient gradient2) {
	public static void main(String[] args) throws Exception {
		Random random = new Random();
		int paletteSize = 10;

		ColorDualGradient mat = makeMetalPalette(random);
		BufferedImage img = new BufferedImage(paletteSize * 32, paletteSize * 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		for (int y = 0; y < paletteSize; y++) {
			for (int x = 0; x < paletteSize; x++) {
				g2d.setColor(new Color(mat.getColor((float) (x + 1) / paletteSize, (float) (y + 1) / paletteSize).getAsInt()));
				g2d.fillRect(x * 32, y * 32, 32, 32);
				//System.out.println(ColorUtil.toHexString(palette[i]));
			}
		}
		ImageIO.write(img, "PNG", new File("./test/dualgradient.png"));
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

	private static final CustomColor VALUE = new CustomColor();

	public CustomColor getColor(float value, boolean gradient) {
		return VALUE.set(gradient ? gradient2.getColor(value) : gradient1.getColor(value));
	}

	public CustomColor getColor(float x, float y) {
		if (x == 0.5f) return (VALUE.set(gradient2.getColor(y)));
		else if (y == 0.5f) return (VALUE.set(gradient1.getColor(x)));
		else return VALUE.set(0, 0, 0, 0);
	}
}