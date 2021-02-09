package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.util.Identifier;

public interface ChangeableRegistry {
	public void remove(Identifier key);
	
	public void recalculateLastID();
}