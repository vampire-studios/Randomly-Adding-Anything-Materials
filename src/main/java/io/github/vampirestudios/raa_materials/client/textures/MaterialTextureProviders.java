package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_core.api.client.textures.TextureProvider;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class MaterialTextureProviders {

    public static final Identifier MATERIAL_BLOCKS = new Identifier(RAAMaterials.MOD_ID, "material_blocks");
    public static final Identifier MATERIAL_ITEMS = new Identifier(RAAMaterials.MOD_ID, "material_items");
    public static final Identifier MATERIAL_ARMORS = new Identifier(RAAMaterials.MOD_ID, "material_armors");
    public static final Identifier MATERIAL_TOOLS = new Identifier(RAAMaterials.MOD_ID, "material_tools");
    public static final Identifier MATERIAL_WEAPONS = new Identifier(RAAMaterials.MOD_ID, "material_weapons");

    public static void init() {
        register(new MaterialBlocksTextureProvider());
        register(new MaterialItemsTextureProvider());
        register(new MaterialArmorsTextureProvider());
        register(new MaterialToolsTextureProvider());
        register(new MaterialWeaponsTextureProvider());
    }

    private static void register(TextureProvider textureProvider) {
        Registry.register(TextureProvider.TEXTURE_PROVIDER_REGISTRY, textureProvider.getId(), textureProvider);
    }

    public static <T> void generateJSON(Identifier textureProviderId, T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Objects.requireNonNull(TextureProvider.TEXTURE_PROVIDER_REGISTRY.get(textureProviderId)).generateJSONs(t, clientResourcePackBuilder);
    }
}
