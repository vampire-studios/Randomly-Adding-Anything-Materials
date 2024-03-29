package io.github.vampirestudios.raa_materials.testing;

import io.github.vampirestudios.raa_materials.utils.ColorDualGradient;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        int paletteSize = 7;
        int palettes = 32;

        //ColorGradient mat;
        ColorDualGradient mat;
        BufferedImage img = new BufferedImage(palettes/2 * paletteSize * 32, palettes/2 * 3 * 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        for(int y = 0; y < palettes/2; y++) {
            for(int c = 0; c < palettes/2; c++) {
                mat = ProceduralTextures.makeMetalPalette(random);
                for (int i = 0; i < paletteSize; i++) {
                    int x = (paletteSize * c + i ) * 32;
                    for (int z = 0; z < 3; z++) {
                        g2d.setColor(new Color(mat.getColor((float) i / paletteSize, (float) z / 2).getAsInt()));
                        g2d.fillRect(x, (y * 3 + z) * 32, 32, 32);
                    }
                }
            }
        }
        ImageIO.write(img, "PNG", new File("./test/palette.png"));
    }
}