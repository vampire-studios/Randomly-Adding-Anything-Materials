package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MappedRegistry.class)
public class SimpleRegistryMixin{/*<T> implements ChangeableRegistry {
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
			int rawID = toId.getInt(entry);
			byId.set(rawID, null);
			toId.removeInt(rawID);
			byLocation.remove(key);
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
	}*/
}