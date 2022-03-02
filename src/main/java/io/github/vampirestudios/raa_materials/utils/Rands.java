/*
 * MIT License
 *
 * Copyright (c) 2020 Vampire Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.vampirestudios.raa_materials.utils;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Rands {

    private static final Random rand = new Random();

    public static Random getRandom() {
        return rand;
    }

    public static int randInt(int bound) {
        return rand.nextInt(bound);
    }

    public static int randIntRange(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static float randFloatRange(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    public static float randFloatRange(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static float randFloat(float bound) {
        return ((float) rand.nextInt((int) (bound * 10) + 1)) / 10;
    }

    public static Color randColor() {
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public static boolean chance(int bound) {
        return randInt(bound) == 0;
    }

    public static <O extends Object> O values(O[] values) {
        return values[randInt(values.length)];
    }

    public static <O extends Object> O list(List<O> list) {
        return list.get(randInt(list.size()));
    }

    public static <K, V extends Object> Map.Entry<K, V> map(Map<K, V> map) {
        Set<Map.Entry<K, V>> entry = map.entrySet();
        return new ArrayList<>(entry).get(randInt(entry.size()));
    }
}
