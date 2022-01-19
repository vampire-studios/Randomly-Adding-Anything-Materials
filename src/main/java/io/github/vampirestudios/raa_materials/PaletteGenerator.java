package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.ColorUtil;
import io.github.vampirestudios.raa_materials.utils.MathUtil;

import java.util.Random;

public class PaletteGenerator {
    public static int[] generateUpliftPalette(Random rand, int base, int colors, double hueshift, double proportion, double bias, double extraProportion, double randProportion, double randSaturation, double randValue, double saturationDecay) {
        int current = base;
        int[] palette = new int[colors];

        double lastLerp = 0;
        for (int i = 0; i < colors; i++) {
            palette[i] = current;
            double lv = (i + 1) / (double) colors;
            double blv = Math.pow(lv, bias);
            double dblv = blv - lastLerp;
            lastLerp = blv;
            current = nextColor(rand, blv, dblv, current, hueshift, proportion / colors, extraProportion / colors, randProportion, randSaturation, randValue, saturationDecay);
        }
        return palette;
    }

    private static int nextColor(Random rand, double biasedLerpFactor, double delta, int color, double hueshift, double proportion, double extraProportion, double randProportion, double randSaturation, double randValue, double saturationDecay) {
        double prop = proportion + biasedLerpFactor * extraProportion;
        prop += ((rand.nextDouble() - rand.nextDouble()) * 0.5 + 0.5) * randProportion;

        double h = ColorUtil.hued(color);
        double s = ColorUtil.saturationd(color);
        double v = ColorUtil.valued(color);

        h += hueshift * delta;
        color = ColorUtil.hsv(h, s, v);

        double r = ColorUtil.redd(color) * prop;
        double g = ColorUtil.greend(color) * prop;
        double b = ColorUtil.blued(color) * prop;
        color = addrgb(color, ColorUtil.rgb(r, g, b));

        h = ColorUtil.hued(color);
        s = ColorUtil.saturationd(color);
        v = ColorUtil.valued(color);

        s += ((rand.nextDouble() - rand.nextDouble()) * 0.5 + 0.5) * randSaturation;
        v += ((rand.nextDouble() - rand.nextDouble()) * 0.5 + 0.5) * randValue;
        s -= saturationDecay * delta;

        return ColorUtil.hsv(h, s, v);
    }

    public static int addrgb(int a, int b) {
        float rA = ColorUtil.redf(a);
        float gA = ColorUtil.greenf(a);
        float bA = ColorUtil.bluef(a);
        float rB = ColorUtil.redf(b);
        float gB = ColorUtil.greenf(b);
        float bB = ColorUtil.bluef(b);
        return ColorUtil.rgb(
            MathUtil.clamp(rA + rB, 0, 1),
            MathUtil.clamp(gA + gB, 0, 1),
            MathUtil.clamp(bA + bB, 0, 1)
        );
    }
}