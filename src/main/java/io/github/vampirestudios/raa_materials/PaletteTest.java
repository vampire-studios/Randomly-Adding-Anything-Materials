package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.util.Mth;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import  io.github.vampirestudios.raa_materials.utils.ProceduralTextures;

public class PaletteTest {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        int paletteSize = 8;
        int palettes = 32;
        CustomColor startColor = new CustomColor(0.1f,0.05f,0.05f).switchToHSV();
        CustomColor mainColor = new CustomColor(//0.1f,1f,0.1f
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat()
        ).switchToHSV();
        CustomColor endColor = new CustomColor(1f,1f,0.9f).switchToHSV();


        ColorGradient mat = new ColorGradient(startColor,mainColor,endColor);

        BufferedImage img = new BufferedImage(paletteSize * 32, palettes * 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        for(int y = 0; y < palettes; y++) {
            mat = ProceduralTextures.makeCrystalPalette(random);
            for (int i = 0; i < paletteSize; i++) {
                int x = i * 32;
                g2d.setColor(new Color(mat.getColor((float) i / paletteSize).getAsInt()));
                g2d.fillRect(x, y*32, 32, 32);
                //System.out.println(ColorUtil.toHexString(palette[i]));
            }

        }
        ImageIO.write(img, "PNG", new File("./palette.png"));
    }
}