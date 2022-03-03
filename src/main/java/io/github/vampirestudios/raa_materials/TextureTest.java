package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class TextureTest {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        for(int i =0; i< stoneBricks.length; i++){
            new TextureTest(random,i);
        }
    }


    // Okay so this is build, so you replace the appropriate class references in a copy-pasted section of the material texture generation you're working on with the "not" version
    //ideally we'd just call some material creation instead but we literally can't without calling most of the game with it.

    //these get replaced with every single base texture reference in the material, course some materials load them differently, but we're working on stone first
    ColorGradient gradient;

    protected static final String[] stoneFrames;
    protected static final String[] stoneBricks;
    protected static final String[] stoneTiles;

    static {
        stoneFrames = new String[2];
        for (int i = 0; i < stoneFrames.length; i++) {
            stoneFrames[i] = ("textures/block/stone_frame_0" + (i+1) + ".png");
        }
        stoneBricks = new String[6];
        for (int i = 0; i < stoneBricks.length; i++) {
            stoneBricks[i] = ("textures/block/stone_bricks_0" + (i+1) + ".png");
        }
        stoneTiles = new String[3];
        for (int i = 0; i < stoneTiles.length; i++) {
            stoneTiles[i] = ("textures/block/stone_tiles_0" + (i+1) + ".png");
        }
    }

    private TextureTest(Random random,int id) throws Exception {
        String stoneFrame = stoneFrames[id % stoneFrames.length];
        String stoneBrick = stoneBricks[id % stoneBricks.length];
        String stoneTile = stoneTiles[id % stoneTiles.length];

        String textureBaseName = "test_"+id+"_";

        this.gradient = ProceduralTextures.makeStonePalette(random);

        // Texture Generation
        ColorGradient palette = this.gradient;

        ResourceLocation stoneTexID = notTextureHelper.makeBlockTextureID(textureBaseName);
        BufferTexture texture = ProceduralTextures.makeStoneTexture(palette, random);
        BufferTexture variant = TextureHelper.applyGradient(texture, gradient);
        notInnerRegistry.registerTexture(stoneTexID, variant);

        texture = ProceduralTextures.makeBlurredTexture(texture);

        BufferTexture overlayTexture = notTextureHelper.loadTexture(stoneFrame);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture,9);
        variant = TextureHelper.applyGradient(variant, gradient);
        ResourceLocation frameTexID = notTextureHelper.makeBlockTextureID("polished_" + textureBaseName);
        notInnerRegistry.registerTexture(frameTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneBrick);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture,9);
        variant = TextureHelper.applyGradient(variant, gradient);
        ResourceLocation bricksTexID = notTextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
        notInnerRegistry.registerTexture(bricksTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneTile);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture,9);
        variant = TextureHelper.applyGradient(variant, gradient);
        ResourceLocation tilesTexID = notTextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
        notInnerRegistry.registerTexture(tilesTexID, variant);

    }

    private static class notTextureHelper{

        public static ResourceLocation makeBlockTextureID(String name) {
            return new ResourceLocation("raa",("block/" + name).replaceAll("'|`|\\^|/| |´", ""));
        }

        public static ResourceLocation makeItemTextureID(String name) {
            return new ResourceLocation("raa",("item/" + name).replaceAll("'|`|\\^|/| |´", ""));
        }

        private static BufferedImage loadImage(String name) {
            try {
                InputStream input = new FileInputStream("src/main/resources/assets/raa_materials/"+name);
                return ImageIO.read(input);
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected static BufferTexture loadTexture(String name) {
            BufferedImage img = Objects.requireNonNull(loadImage(name));
            BufferTexture out = new BufferTexture(img.getWidth(),img.getHeight());
            for(int y = 0; y < out.getHeight(); y++) {
                for (int x = 0; x < out.getWidth(); x++) {
                    out.setPixel(x, y, new CustomColor(img.getRGB(x,y)));
                }
            }
            return out;
        }
    }


    private static class notInnerRegistry{
        public static void registerTexture(ResourceLocation id, BufferTexture image) throws IOException {
            ImageIO.write( makeImage(tile(image)), "PNG", new File("./test/"+id.toString().replaceAll(":", "")+".png"));
        }
    }

    public static BufferTexture tile(BufferTexture tex) {
        BufferTexture out = new BufferTexture(tex.getWidth()*3,tex.getHeight()*3);
        for(int y = 0; y < out.getHeight(); y++) {
            for (int x = 0; x < out.getWidth(); x++) {
                out.setPixel(x, y, new CustomColor(tex.getPixel(x%tex.getWidth(),y%tex.getHeight())));
            }
        }
        return out;
    }

    public static BufferedImage makeImage(BufferTexture tex) {
        BufferedImage img = new BufferedImage(tex.getWidth(),tex.getHeight(),2);
        for(int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x,y,tex.getPixel(x,y));
            }
        }
        return img;
    }
}