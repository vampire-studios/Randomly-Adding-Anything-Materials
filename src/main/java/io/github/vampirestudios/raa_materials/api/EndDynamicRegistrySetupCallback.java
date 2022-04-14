/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.vampirestudios.raa_materials.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.core.RegistryAccess;

/**
 * This event gets triggered when a new {@link RegistryAccess} gets created, but after it gets filled.
 * This is in contract to {@link DynamicRegistrySetupCallback} which runs before it is filled.
 * Therefore, this is the ideal place to touch multiple dynamic registries at once, for example.
 * Below is an example usage of the event.
 *
 * <pre>
 *  * {@code
 *  * EndDynamicRegistrySetupCallback.EVENT.register(registryManager -> {
 *  *     Registry<StructurePool> pools = registryManager.get(Registry.STRUCTURE_POOL_KEY);
 *  *     Registry<StructurePoolProcessorList> lists = registryManager.get(Registry.STRUCTURE_PROCESSOR_LIST_KEY);
 *  *     ...
 *  * });
 *  * }
 *  * </pre>
 */
@FunctionalInterface
public interface EndDynamicRegistrySetupCallback {
	void onRegistrationFinish(RegistryAccess registryManager);

	Event<EndDynamicRegistrySetupCallback> EVENT = EventFactory.createArrayBacked(
			EndDynamicRegistrySetupCallback.class,
			callbacks -> registryManager -> {
				for (EndDynamicRegistrySetupCallback callback : callbacks) {
					callback.onRegistrationFinish(registryManager);
				}
			}
	);
}