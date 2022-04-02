package io.github.vampirestudios.raa_materials.mixins.server;

import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.utils.ChangeableRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(MappedRegistry.class)
public class RegistryMixin<T> implements ChangeableRegistry {
	@Final
	@Shadow
	private ObjectList<T> byId;
	@Final
	@Shadow
	private Object2IntMap<T> toId;
	@Final
	@Shadow
	private Map<ResourceLocation, Holder.Reference<T>> byLocation;
	@Final
	@Shadow
	private Map<ResourceKey<T>, Holder.Reference<T>> byKey;
	@Final
	@Shadow
	private Map<T, Holder.Reference<T>> byValue;
	@Final
	@Shadow
	private Map<T, Lifecycle> lifecycles;
	@Shadow
	private int nextId;

	@Override
	public void remove(ResourceLocation key) {
		T entry = byLocation.get(key).value();
		if (entry != null) {
			int rawID = toId.getInt(entry);
			//byId.set(rawID, null);
			//toId.removeInt(rawID);
			byId.remove(rawID);
			toId.remove(entry,rawID);
			for (T entr : toId.keySet()) {
				int id = toId.getInt(entr);
				toId.replace(entr, id > rawID ? id - 1 : id);
			}
			byLocation.remove(key);
			ResourceKey<T> storageKey = null;
			for (ResourceKey<T> searchKey: byKey.keySet()) {
				if (byKey.get(searchKey) != null) {
					if (byKey.get(searchKey).value() == entry) {
						storageKey = searchKey;
						break;
					}
				}
			}
			if (storageKey != null) {
				byKey.remove(storageKey);
			}
			byValue.remove(entry);
			lifecycles.remove(entry);
		}
	}

	@Override
	public void recalculateLastID() {
		int lastID = 0;
		for (int id: toId.values()) {
			lastID = Math.max(id, lastID);
		}
		nextId = lastID + 1;
	}
}