package io.github.vampirestudios.raa_materials.utils;

public class ColorGradient {
	private static final CustomColor VALUE = new CustomColor();
	private final CustomColor start;
	private final CustomColor midpoint;
	private final CustomColor end;
	
	public ColorGradient(CustomColor start, CustomColor end) {
		this(start, new CustomColor().set(start).mixWith(end,0.5f), end);
	}

	public ColorGradient(CustomColor start, CustomColor mid, CustomColor end) {
		this.start = start;
		this.midpoint = mid;
		this.end = end;
	}

	public CustomColor getColor(float value) {
		if (value < 0) {
			return VALUE.set(start);
		} else if (value > 1) {
			return VALUE.set(end);
		} else if (value == 0.5f) {
			return VALUE.set(midpoint);
		} else if (value < 0.5f){
			return VALUE.set(start).mixWith(midpoint, value*2);
		} else {
			return VALUE.set(midpoint).mixWith(end, (value-0.5f)*2);
		}
	}
}