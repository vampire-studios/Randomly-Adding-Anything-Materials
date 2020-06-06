package io.github.vampirestudios.raa_materials.data;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_core.api.data.DataProvider;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class MaterialDataProviders {

    public static final Identifier MATERIAL_ITEMS_BLOCKS = new Identifier(RAAMaterials.MOD_ID, "material_items_blocks");
    public static final Identifier MATERIAL_ARMORS = new Identifier(RAAMaterials.MOD_ID, "material_armors");
    public static final Identifier MATERIAL_TOOLS = new Identifier(RAAMaterials.MOD_ID, "material_tools");
    public static final Identifier MATERIAL_WEAPONS = new Identifier(RAAMaterials.MOD_ID, "material_weapons");

    public static void init() {
        register(new MaterialItemsBlocksDataProvider());
        register(new MaterialArmorsDataProvider());
        register(new MaterialToolsDataProvider());
        register(new MaterialWeaponsDataProvider());
    }

    private static void register(DataProvider textureProvider) {
        Registry.register(DataProvider.DATA_PROVIDER_REGISTRY, textureProvider.getId(), textureProvider);
    }

    public static <T> void generateJSON(Identifier textureProviderId, T t, ArtificeResourcePack.ServerResourcePackBuilder clientResourcePackBuilder) {
        Objects.requireNonNull(DataProvider.DATA_PROVIDER_REGISTRY.get(textureProviderId)).generateJSONs(t, clientResourcePackBuilder);
    }
}
