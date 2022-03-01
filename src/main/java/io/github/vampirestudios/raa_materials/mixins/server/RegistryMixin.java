package io.github.vampirestudios.raa_materials.mixins.server;

import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.api.RegistryRemover;
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
public class RegistryMixin<T> implements RegistryRemover {
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
	private Map<T, Lifecycle> lifecycles;
	@Shadow
	private int nextId;
	
	@Override
	public void remove(ResourceLocation key) {
		if (!byLocation.containsKey(key)) {
			return;
		}
		
		T object = byLocation.get(key).value();
		byLocation.remove(key);
		byId.remove(object);
		toId.removeInt(object);
		lifecycles.remove(object);
		ResourceKey<T> storageKey = null;
		for (ResourceKey<T> searchKey: byKey.keySet()) {
			if (byKey.get(searchKey).value() == object) {
				storageKey = searchKey;
				break;
			}
		}
		if (storageKey != null) {
			byKey.remove(storageKey);
		}
	}
}