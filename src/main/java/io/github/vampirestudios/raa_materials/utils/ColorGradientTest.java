package io.github.vampirestudios.raa_materials.utils;

public class ColorGradientTest {
	private static final CustomColor VALUE = new CustomColor();
	private final ColorGradient gradient1;
	private final ColorGradient gradient2;

	public ColorGradientTest(ColorGradient gradient1, ColorGradient gradient2) {
		this.gradient1 = gradient1;
		this.gradient2 = gradient2;
	}
	
	public CustomColor getColor(float value) {
		CustomColor color1 = gradient1.getColor(value);
		CustomColor color2 = gradient2.getColor(value);
		if (value < 0) {
			CustomColor start = new CustomColor(color1.getRed() + color2.getRed(), color1.getGreen() + color2.getGreen(), color1.getBlue() + color2.getBlue(), color1.getAlpha() + color2.getAlpha());
			return VALUE.set(start);
		}
		else if (value > 1) {
			CustomColor end = new CustomColor(color1.getRed() - color2.getRed(), color1.getGreen() - color2.getGreen(), color1.getBlue() - color2.getBlue(), color1.getAlpha() - color2.getAlpha());
			return VALUE.set(end);
		}
		else {
			CustomColor start = new CustomColor(color1.getRed() + color2.getRed(), color1.getGreen() + color2.getGreen(), color1.getBlue() + color2.getBlue(), color1.getAlpha() + color2.getAlpha());
			CustomColor end = new CustomColor(color1.getRed() - color2.getRed(), color1.getGreen() - color2.getGreen(), color1.getBlue() - color2.getBlue(), color1.getAlpha() - color2.getAlpha());
			return VALUE.set(start).mixWith(end, value);
		}
	}
}