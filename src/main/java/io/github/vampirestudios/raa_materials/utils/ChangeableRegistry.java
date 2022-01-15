package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.resources.ResourceLocation;

public interface ChangeableRegistry {
	public void remove(ResourceLocation key);
	
	public void recalculateLastID();
}