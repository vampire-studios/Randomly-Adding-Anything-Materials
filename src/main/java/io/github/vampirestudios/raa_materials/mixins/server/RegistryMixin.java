package io.github.vampirestudios.raa_materials.mixins.server;

import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.api.DeletableObjectInternal;
import io.github.vampirestudios.raa_materials.api.ExtendedRegistry;
import io.github.vampirestudios.raa_materials.api.RegistryEntryDeletedCallback;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.registry.RegistryEntryRemovedCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Mixin(MappedRegistry.class)
public abstract class RegistryMixin<T> extends Registry<T> implements ExtendedRegistry<T> {
	@Shadow private boolean frozen;
	@Shadow @Final private @Nullable Function<T, Holder.Reference<T>> customHolderProvider;
	@Shadow private @Nullable Map<T, Holder.Reference<T>> intrusiveHolderCache;

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
	@Shadow private @Nullable List<Holder.Reference<T>> holdersInOrder;

	protected RegistryMixin(ResourceKey<? extends Registry<T>> resourceKey, Lifecycle lifecycle) {
		super(resourceKey, lifecycle);
	}

	@Shadow public abstract Optional<Holder<T>> getHolder(@NotNull ResourceKey<T> key);

	@SuppressWarnings("unchecked")
	private final Event<RegistryEntryDeletedCallback<T>> dynreg$entryDeletedEvent = EventFactory.createArrayBacked(RegistryEntryDeletedCallback.class, callbacks -> (rawId, entry) -> {
		for (var callback : callbacks) {
			callback.onEntryDeleted(rawId, entry);
		}

		if (entry.value() instanceof RegistryEntryDeletedCallback<?> callback)
			((RegistryEntryDeletedCallback<T>)callback).onEntryDeleted(rawId, entry);
	});

	@Override
	public Event<RegistryEntryDeletedCallback<T>> dynreg$getEntryDeletedEvent() {
		return dynreg$entryDeletedEvent;
	}

	@Override
	public void dynreg$remove(ResourceKey<T> key) {
		if (frozen) {
			throw new IllegalStateException("Registry is frozen (trying to remove key " + key + ")");
		}

		Holder.Reference<T> entry = (Holder.Reference<T>) getHolder(key).orElseThrow();
		int rawId = toId.getInt(entry.value());
		RegistryEntryRemovedCallback.event(this).invoker().onEntryRemoved(rawId, entry.key().location(), entry.value());
		dynreg$entryDeletedEvent.invoker().onEntryDeleted(rawId, entry);

		byId.set(rawId, null);
		toId.removeInt(entry);
		byLocation.remove(key.location());
		byKey.remove(key);
		byValue.remove(entry.value());
		lifecycles.remove(entry.value());
		holdersInOrder = null;

		((DeletableObjectInternal) entry).markAsDeleted();

		if (entry.value() instanceof DeletableObjectInternal obj)
			obj.markAsDeleted();
	}

	@Override
	public void dynreg$unfreeze() {
		frozen = false;
		if (customHolderProvider != null)
			this.intrusiveHolderCache = new IdentityHashMap<>();
	}
}