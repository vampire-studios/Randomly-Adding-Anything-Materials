package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_core.api.client.textures.TextureProvider;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class TextureProviders {

    public static final Identifier MATERIAL_BLOCK = new Identifier(RAAMaterials.MOD_ID, "material_block");
    public static final Identifier MATERIAL_ORE_BLOCK = new Identifier(RAAMaterials.MOD_ID, "material_ore_block");

    public static void init() {
        register(MATERIAL_BLOCK, new MaterialBlockTextureProvider());
        register(MATERIAL_ORE_BLOCK, new MaterialOreBlockTextureProvider());
    }

    private static void register(Identifier identifier, TextureProvider textureProvider) {
        Registry.register(TextureProvider.TEXTURE_PROVIDER_REGISTRY, identifier, textureProvider);
    }

    public static <T> void generateJSON(Identifier textureProviderId, T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Objects.requireNonNull(TextureProvider.TEXTURE_PROVIDER_REGISTRY.get(textureProviderId)).generateJSONs(t, clientResourcePackBuilder);
    }
}
