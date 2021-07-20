package io.github.vampirestudios.raa_materials.mixins.server;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.api.RegistryRemover;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(SimpleRegistry.class)
public class RegistryMixin<T> implements RegistryRemover {
	@Final
	@Shadow
	private ObjectList<T> rawIdToEntry;
	@Final
	@Shadow
	private Object2IntMap<T> entryToRawId;
	@Final
	@Shadow
	private BiMap<Identifier, T> idToEntry;
	@Final
	@Shadow
	private BiMap<RegistryKey<T>, T> keyToEntry;
	@Final
	@Shadow
	private Map<T, Lifecycle> entryToLifecycle;
	@Shadow
	private int nextId;
	
	@Override
	public void remove(Identifier key) {
		if (!idToEntry.containsKey(key)) {
			return;
		}
		
		T object = idToEntry.get(key);
		idToEntry.remove(key);
		rawIdToEntry.remove(object);
		entryToRawId.removeInt(object);
		entryToLifecycle.remove(object);
		RegistryKey<T> storageKey = null;
		for (RegistryKey<T> searchKey: keyToEntry.keySet()) {
			if (keyToEntry.get(searchKey) == object) {
				storageKey = searchKey;
				break;
			}
		}
		if (storageKey != null) {
			keyToEntry.remove(storageKey);
		}
	}
}