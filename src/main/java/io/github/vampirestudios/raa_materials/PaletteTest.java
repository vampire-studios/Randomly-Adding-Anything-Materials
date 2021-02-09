package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.ColorUtil;
import io.github.vampirestudios.vampirelib.utils.Rands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class PaletteTest {
    public static void main(String[] args) throws Exception {
        int paletteSize = 7;
        int mainColor = 0x432e63;
        double hueshift = -130;
        double proportion = 1.3;
        double bias = 2.0;
        double extra = 1.6;

        int[] palette = PaletteGenerator.generateUpliftPalette(
                new Random(Rands.getRandom().nextLong()),
            mainColor,
            paletteSize,
            hueshift,
            proportion,
            bias,
            extra,
            0.03,
            0.0,
            0.0,
            0.0
        );

        BufferedImage img = new BufferedImage(paletteSize * 32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        for(int i = 0; i < paletteSize; i++) {
            int x = i * 32;
            g2d.setColor(new Color(palette[i]));
            g2d.fillRect(x, 0, 32, 32);
            System.out.println(ColorUtil.toHexString(palette[i]));
        }
        ImageIO.write(img, "PNG", new File("./palette.png"));
    }
}