package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;


public class TextureTest {
    public static void main(String[] args) throws Exception {
        Random random = new Random(Rands.getRandom().nextLong());
        for(int i =0; i < 200; i++) {
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
    protected static final String[] cobbled;
    protected static final String[] chiseled;

    static {
        stoneFrames = new String[10];
        for (int i = 0; i < stoneFrames.length; i++) {
            stoneFrames[i] = "textures/block/stone_frame_0" + (i+1) + ".png";
        }
        stoneBricks = new String[16];
        for (int i = 0; i < stoneBricks.length; i++) {
            stoneBricks[i] = "textures/block/stone_bricks_0" + (i+1) + ".png";
        }
        stoneTiles = new String[8];
        for (int i = 0; i < stoneTiles.length; i++) {
            stoneTiles[i] = "textures/block/stone_tiles_0" + (i+1) + ".png";
        }
        cobbled = new String[7];
        for (int i = 0; i < cobbled.length; i++) {
            cobbled[i] = "textures/block/stone_cobbled_0" + (i+1) + ".png";
        }
        chiseled = new String[3];
        for (int i = 0; i < chiseled.length; i++) {
            chiseled[i] = "textures/block/stone_chiseled_0" + (i+1) + ".png";
        }
    }

    private TextureTest(Random random,int id) throws Exception {
        String stoneFrame = stoneFrames[id % stoneFrames.length];
        String stoneBrick = stoneBricks[id % stoneBricks.length];
        String stoneTile = stoneTiles[id % stoneTiles.length];
        String stoneCobbled = cobbled[id % cobbled.length];
        String stoneChiseled = chiseled[id % chiseled.length];

        String textureBaseName = "test_" + id + "_";

        this.gradient = ProceduralTextures.makeStonePalette(random);

        float[] values = new float[]{0.13f,0.22f,0.34f,0.53f,0.60f,0.70f,0.85f,0.90f};

        ResourceLocation stoneTexID = notTextureHelper.makeBlockTextureID(textureBaseName);
        BufferTexture texture = makeStoneTexture(values, random); //from procedural texture
        float[] temp = TextureHelper.getValues(texture);
        values = new float[temp.length+1];
        System.arraycopy(temp, 0, values, 0, temp.length);
        values[temp.length] = 0.9f;

        BufferTexture variant = TextureHelper.applyGradient(texture.cloneTexture(), gradient);
        notInnerRegistry.registerTexture(stoneTexID, variant);

        texture = ProceduralTextures.makeBlurredTexture(texture);
        //texture = TextureHelper.clampValue(texture, values);

        BufferTexture overlayTexture = notTextureHelper.loadTexture(stoneFrame);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

        TextureHelper.applyGradient(variant, gradient);
        ResourceLocation frameTexID = notTextureHelper.makeBlockTextureID("polished_" + textureBaseName);
        notInnerRegistry.registerTexture(frameTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneBrick);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);
        TextureHelper.applyGradient(variant, gradient);
        ResourceLocation bricksTexID = notTextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
        notInnerRegistry.registerTexture(bricksTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneTile);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

        TextureHelper.applyGradient(variant, gradient);
        ResourceLocation tilesTexID = notTextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
        notInnerRegistry.registerTexture(tilesTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneCobbled);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

        TextureHelper.applyGradient(variant, gradient);
        ResourceLocation cobbledTexID = notTextureHelper.makeBlockTextureID("cobbled_" + textureBaseName);
        notInnerRegistry.registerTexture(cobbledTexID, variant);

        overlayTexture = notTextureHelper.loadTexture(stoneChiseled);
        TextureHelper.normalize(overlayTexture, 0.1F, 1F);
        variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

        TextureHelper.applyGradient(variant, gradient);
        ResourceLocation chiseledTexID = notTextureHelper.makeBlockTextureID("chiseled_" + textureBaseName);
        notInnerRegistry.registerTexture(chiseledTexID, variant);
    }

    public static BufferTexture makeStoneTexture(float[] values, Random random) {
        Rands.setRand(random);
        BufferTexture texture = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.6F, 1.2F) / 4F);
        BufferTexture distort = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.6F, 1.2F) / 4F);
        BufferTexture additions = TextureHelper.makeNoiseTexture(random, 64, Rands.randFloatRange(0.5F, 1.4F) / 4F);
        BufferTexture result = TextureHelper.distort(texture, distort, Rands.randFloatRange(0F, 8F));
        BufferTexture pass = TextureHelper.heightPass(result, -1, -1);

        pass = TextureHelper.normalize(pass);
        result = TextureHelper.clamp(result, 9);
        result = TextureHelper.normalize(result);
        result = TextureHelper.blend(result, pass, 0.3F);
        result = TextureHelper.add(result, pass);
        result = TextureHelper.blend(result, additions, 0.3F);
        result = TextureHelper.normalize(result);
        result = TextureHelper.clamp(result, 8);

        if (16 < result.getWidth())
            result = TextureHelper.downScale(result, result.getWidth() / 16);
        else result = TextureHelper.upScale(result, 16 / result.getWidth());

        result = TextureHelper.normalize(result, 0.1F, 0.7F);
        result = TextureHelper.clampValue(result, values);

        return result;
    }

    private static class notTextureHelper {

        public static ResourceLocation makeBlockTextureID(String name) {
            return new ResourceLocation("raa", ("block/" + name).replaceAll("'|`|\\^|/| |´", ""));
        }

        public static ResourceLocation makeItemTextureID(String name) {
            return new ResourceLocation("raa", ("item/" + name).replaceAll("'|`|\\^|/| |´", ""));
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