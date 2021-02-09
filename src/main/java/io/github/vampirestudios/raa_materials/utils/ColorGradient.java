package io.github.vampirestudios.raa_materials.utils;

public class ColorGradient {
	private static final CustomColor VALUE = new CustomColor();
	private final CustomColor start;
	private final CustomColor end;
	
	public ColorGradient(CustomColor start, CustomColor end) {
		this.start = start;
		this.end = end;
	}
	
	public CustomColor getColor(float value) {
		if (value < 0) {
			return VALUE.set(start);
		}
		else if (value > 1) {
			return VALUE.set(end);
		}
		else {
			return VALUE.set(start).mixWith(end, value);
		}
	}
}