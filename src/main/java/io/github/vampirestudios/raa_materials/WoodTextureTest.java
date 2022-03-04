package io.github.vampirestudios.raa_materials;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class WoodTextureTest {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        for(int i =0; i < 50; i++) {
            new WoodTextureTest(random,i);
        }
    }


    // Okay so this is build, so you replace the appropriate class references in a copy-pasted section of the material texture generation you're working on with the "not" version
    //ideally we'd just call some material creation instead but we literally can't without calling most of the game with it.

    //these get replaced with every single base texture reference in the material, course some materials load them differently, but we're working on stone first
    Pair<ColorGradient, ColorGradient> gradient;

    private WoodTextureTest(Random random, int id) throws Exception {
        String textureTopBaseName = "test_" + id + "_top";
        String textureSideBaseName = "test_" + id + "_side";

        this.gradient = ProceduralTextures.makeWoodPalette(random);

        ResourceLocation stoneTexID = notTextureHelper.makeBlockTextureID(textureSideBaseName);
        BufferTexture texture = ProceduralTextures.makeStoneTexture(random);
        BufferTexture variant = TextureHelper.applyGradient(texture, gradient.getFirst());
        notInnerRegistry.registerTexture(stoneTexID, variant);

        stoneTexID = notTextureHelper.makeBlockTextureID(textureTopBaseName);
        texture = ProceduralTextures.makeStoneTexture(random);
        variant = TextureHelper.applyGradient(texture, gradient.getSecond());
        notInnerRegistry.registerTexture(stoneTexID, variant);
    }

    private static class notTextureHelper{
        public static ResourceLocation makeBlockTextureID(String name) {
            return new ResourceLocation("raa", ("block/" + name).replaceAll("'|`|\\^|/| |´", ""));
        }
    }


    private static class notInnerRegistry{
        public static void registerTexture(ResourceLocation id, BufferTexture image) throws IOException {
            ImageIO.write(makeImage(tile(image)), "PNG", new File("./test/wood/" + id.toString().replaceAll(":", "") + ".png"));
        }
    }

    public static BufferTexture tile(BufferTexture tex) {
        return tile(tex, 3);
    }

    public static BufferTexture tile(BufferTexture tex, int amount) {
        BufferTexture out = new BufferTexture(tex.getWidth() * amount,tex.getHeight() * amount);
        for(int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                out.setPixel(x, y, new CustomColor(tex.getPixel(x % tex.getWidth(),y % tex.getHeight())));
            }
        }
        return out;
    }

    public static BufferedImage makeImage(BufferTexture tex) {
        BufferedImage img = new BufferedImage(tex.getWidth(), tex.getHeight(),2);
        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, tex.getPixel(x, y));
            }
        }
        return img;
    }
}