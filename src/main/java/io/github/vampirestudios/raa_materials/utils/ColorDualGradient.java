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

		ColorDualGradient mat = ProceduralTextures.makeMetalPalette(random);
		BufferedImage img = new BufferedImage(paletteSize * 32, paletteSize * 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		for (int y = 0; y < paletteSize; y++) {
			for (int x = 0; x < paletteSize; x++) {
				g2d.setColor(new Color(mat.getColor((float) (x + 1) / paletteSize, (float) (y + 1) / paletteSize, 0).getAsInt()));
				g2d.fillRect(x * 32, y * 32, 32, 32);
				//System.out.println(ColorUtil.toHexString(palette[i]));
			}
		}
		ImageIO.write(img, "PNG", new File("./test/dualgradient.png"));
	}

	private static final CustomColor VALUE = new CustomColor();

	public CustomColor getColor(float value, boolean gradient) {
		return VALUE.set(gradient ? gradient2.getColor(value) : gradient1.getColor(value));
	}

	public CustomColor getColor(float value, float mix) {
		if (mix <= 0f) return (VALUE.set(gradient1.getColor(value))).switchToHSV();
		else if (mix >= 1f) return (VALUE.set(gradient2.getColor(value))).switchToHSV();
		else return VALUE.set(gradient1.getColor(value).switchToRGB()).mixWith(gradient2.getColor(value).switchToRGB(), mix).switchToHSV();
	}

	public CustomColor getColor(float x, float y, int a) {
		if (x == 0.5f) return (VALUE.set(gradient2.getColor(y)));
		else if (y == 0.5f) return (VALUE.set(gradient1.getColor(x)));
		else return VALUE.set(0, 0, 0, 0);
	}

	public ColorGradient getIntermedaryGradient(float mix){
		CustomColor start = new CustomColor().set(getColor(0f,mix));
		CustomColor mid = new CustomColor().set(getColor(0.5f,mix));
		CustomColor end = new CustomColor().set(getColor(1f,mix));
		return new ColorGradient(start, mid, end);
	}
}