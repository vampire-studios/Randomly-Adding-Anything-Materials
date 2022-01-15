package io.github.vampirestudios.raa_materials.mixins.server;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.api.RegistryRemover;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(MappedRegistry.class)
public class RegistryMixin<T> implements RegistryRemover {
	@Final
	@Shadow
	private ObjectList<T> byId;
	@Final
	@Shadow
	private Object2IntMap<T> toId;
	@Final
	@Shadow
	private BiMap<ResourceLocation, T> storage;
	@Final
	@Shadow
	private BiMap<ResourceKey<T>, T> keyStorage;
	@Final
	@Shadow
	private Map<T, Lifecycle> lifecycles;
	@Shadow
	private int nextId;
	
	@Override
	public void remove(ResourceLocation key) {
		if (!storage.containsKey(key)) {
			return;
		}
		
		T object = storage.get(key);
		storage.remove(key);
		byId.remove(object);
		toId.removeInt(object);
		lifecycles.remove(object);
		ResourceKey<T> storageKey = null;
		for (ResourceKey<T> searchKey: keyStorage.keySet()) {
			if (keyStorage.get(searchKey) == object) {
				storageKey = searchKey;
				break;
			}
		}
		if (storageKey != null) {
			keyStorage.remove(storageKey);
		}
	}
}