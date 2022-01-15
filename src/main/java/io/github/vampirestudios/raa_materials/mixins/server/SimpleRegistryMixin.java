package io.github.vampirestudios.raa_materials.mixins.server;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.utils.ChangeableRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Mixin(MappedRegistry.class)
public class SimpleRegistryMixin<T> implements ChangeableRegistry {
	@Final
	@Shadow
	private ObjectList<T> rawIdToEntry;
	@Final
	@Shadow
	private Object2IntMap<T> entryToRawId;
	@Final
	@Shadow
	private BiMap<ResourceLocation, T> idToEntry;
	@Final
	@Shadow
	private BiMap<ResourceKey<T>, T> keyToEntry;
	@Final
	@Shadow
	private Map<T, Lifecycle> entryToLifecycle;
	@Shadow
	private int nextId;

	@Override
	public void remove(ResourceLocation key) {
		T entry = idToEntry.get(key);
		if (entry != null) {
			int rawID = entryToRawId.getInt(entry);
			rawIdToEntry.set(rawID, null);
			entryToRawId.removeInt(rawID);
			idToEntry.remove(key);
			keyToEntry.inverse().remove(entry);
			entryToLifecycle.remove(entry);
		}
	}

	@Override
	public void recalculateLastID() {
		int lastID = 0;
		for (int id: entryToRawId.values()) {
			lastID = Math.max(id, lastID);
		}
		nextId = lastID + 1;
	}
}