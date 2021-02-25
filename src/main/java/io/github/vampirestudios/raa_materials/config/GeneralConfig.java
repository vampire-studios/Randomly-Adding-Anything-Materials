package io.github.vampirestudios.raa_materials.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.config.v1.GsonSerializer;
import net.fabricmc.loader.api.config.data.SaveType;
import net.fabricmc.loader.api.config.entrypoint.Config;
import net.fabricmc.loader.api.config.serialization.ConfigSerializer;
import net.fabricmc.loader.api.config.value.ValueKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GeneralConfig extends Config<JsonObject> {

    public static final ValueKey<Integer> materialGenAmount = value(40);

    public static final ValueKey<Boolean> useOnlyVanillaPotionEffects = value(true);
    public static final ValueKey<List<String>> blacklistedPotionEffects = value(() -> ImmutableList.of("immersive_portals:longer_reach"));

    @Override
    public @NotNull ConfigSerializer<JsonObject> getSerializer() {
        return GsonSerializer.DEFAULT;
    }

    @Override
    public @NotNull SaveType getSaveType() {
        return SaveType.ROOT;
    }

    @Override
    public @NotNull String getName() {
        return "general";
    }
}
