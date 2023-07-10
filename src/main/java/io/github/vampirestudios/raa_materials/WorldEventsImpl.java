package io.github.vampirestudios.raa_materials;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public class WorldEventsImpl {
    public static final Event<BeforeAddingTags> BEFORE_ADDING_TAGS = EventFactory.createArrayBacked(BeforeAddingTags.class, callbacks ->
            (directory, tagsMap) -> {
                for (var callback : callbacks) {
                    callback.apply(directory, tagsMap);
                }
            }
    );


    public static final Event<OnWorldLoad> ON_WORLD_LOAD = EventFactory.createArrayBacked(OnWorldLoad.class, callbacks -> () -> {
                for (var callback : callbacks) {
                    callback.onLoad();
                }
            }
    );

    public interface BeforeAddingTags {
        void apply(String directory, Map<ResourceLocation, List<TagLoader.EntryWithSource>> tagsMap);
    }

    public interface OnWorldLoad {
        void onLoad();
    }
}