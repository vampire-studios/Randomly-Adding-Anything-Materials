package io.github.vampirestudios.raa_materials.client;

import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

import java.util.Random;
import java.util.function.Function;

public class ModelBuilder {

    public static final int FULL_BRIGHTNESS = 15 << 20 | 15 << 4;
    private static ModelBuilder instance;
    private static Function<SpriteIdentifier, Sprite> spriteFunc;
    public final MeshBuilder builder;
    private final MaterialFinder finder;
    private ModelBuilder() {
        final Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        builder = renderer.meshBuilder();
        finder = renderer.materialFinder();
    }

    public static ModelBuilder prepare(Function<SpriteIdentifier, Sprite> spriteFuncIn) {
        if (instance == null) {
            instance = new ModelBuilder();
        }
        spriteFunc = spriteFuncIn;
        return instance;
    }

    public static int randomPastelColor(Random random) {
        return 0xFF000000 | ((random.nextInt(127) + 127) << 16) | ((random.nextInt(127) + 127) << 8) | (random.nextInt(127) + 127);
    }

    /**
     * Makes a regular icosahedron, which is a very close approximation to a sphere for most purposes.
     * Loosely based on http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
     */
    public static void makeIcosahedron(Vec3f center, float radius, QuadEmitter qe, RenderMaterial renderMaterial, Sprite sprite, boolean smoothNormals, Material material) {
        /** vertex scale */
        final float s = (float) (radius / (2 * Math.sin(2 * Math.PI / 5)));

        final Vec3f[] vertexes = new Vec3f[12];
        Vec3f[] normals = new Vec3f[12];
        // create 12 vertices of a icosahedron
        final float t = (float) (s * (1.0 + Math.sqrt(5.0)) / 2.0);
        int vi = 0;

        normals[vi++] = new Vec3f(-s, t, 0);
        normals[vi++] = new Vec3f(s, t, 0);
        normals[vi++] = new Vec3f(-s, -t, 0);
        normals[vi++] = new Vec3f(s, -t, 0);

        normals[vi++] = new Vec3f(0, -s, t);
        normals[vi++] = new Vec3f(0, s, t);
        normals[vi++] = new Vec3f(0, -s, -t);
        normals[vi++] = new Vec3f(0, s, -t);

        normals[vi++] = new Vec3f(t, 0, -s);
        normals[vi++] = new Vec3f(t, 0, s);
        normals[vi++] = new Vec3f(-t, 0, -s);
        normals[vi++] = new Vec3f(-t, 0, s);

        for (int i = 0; i < 12; i++) {
            final Vec3f n = normals[i];
            vertexes[i] = new Vec3f(center.getX() + n.getX(), center.getY() + n.getY(), center.getZ() + n.getZ());

            if (smoothNormals) {
                float x = n.getX();
                float y = n.getY();
                float z = n.getZ();

                final float len = (float) Math.sqrt(x * x + y * y + z * z);

                x /= len;
                y /= len;
                z /= len;
                n.set(x, y, z);
            }
        }

        if (!smoothNormals) {
            normals = null;
        }

        // create 20 triangles of the icosahedron
        makeIcosahedronFace(true, 0, 11, 5, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 4, 5, 11, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 0, 5, 1, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 9, 1, 5, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 0, 1, 7, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 8, 7, 1, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 0, 7, 10, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 6, 10, 7, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 0, 10, 11, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 2, 11, 10, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 5, 4, 9, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 3, 9, 4, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 11, 2, 4, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 3, 4, 2, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 10, 6, 2, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 3, 2, 6, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 7, 8, 6, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 3, 6, 8, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(true, 1, 9, 8, vertexes, normals, qe, renderMaterial, sprite, material);
        makeIcosahedronFace(false, 3, 8, 9, vertexes, normals, qe, renderMaterial, sprite, material);
    }

    private static void makeIcosahedronFace(boolean topHalf, int p1, int p2, int p3, Vec3f[] points, Vec3f[] normals, QuadEmitter qe, RenderMaterial renderMaterial, Sprite sprite, Material material) {
        if (topHalf) {
            qe.pos(0, points[p1]).sprite(0, 0, 1, 1).spriteColor(0, -1, -1, -1, -1);
            qe.pos(1, points[p2]).sprite(1, 0, 0, 1).spriteColor(0, -1, -1, -1, -1);
            qe.pos(2, points[p3]).sprite(2, 0, 1, 0).spriteColor(0, -1, -1, -1, -1);
            qe.pos(3, points[p3]).sprite(3, 0, 1, 0).spriteColor(0, -1, -1, -1, -1);
        } else {
            qe.pos(0, points[p1]).sprite(0, 0, 0, 0).spriteColor(0, -1, -1, -1, -1);
            qe.pos(1, points[p2]).sprite(1, 0, 1, 0).spriteColor(0, -1, -1, -1, -1);
            qe.pos(2, points[p3]).sprite(2, 0, 0, 1).spriteColor(0, -1, -1, -1, -1);
            qe.pos(3, points[p3]).sprite(3, 0, 0, 1).spriteColor(0, -1, -1, -1, -1);
        }
        if (normals != null) {
            qe.normal(0, normals[p1]);
            qe.normal(1, normals[p2]);
            qe.normal(2, normals[p3]);
            qe.normal(3, normals[p3]);
        }
        qe.spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED);
        qe.material(renderMaterial);
        Random random = Rands.getRandom();
        int color = Rands.randIntRange(randomPastelColor(random), material.getColor());
        qe.spriteColor(0, color, color, color, 255);
        qe.emit();
    }

    public MaterialFinder finder() {
        return finder.clear();
    }

    public Sprite getSprite(String spriteName) {
        return spriteFunc.apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(spriteName)));
    }

    public void box(
            RenderMaterial material,
            int color, Sprite sprite,
            float minX, float minY, float minZ,
            float maxX, float maxY, float maxZ) {

        builder.getEmitter()
                .material(material)
                .square(Direction.UP, minX, minZ, maxX, maxZ, 1 - maxY)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit()

                .material(material)
                .square(Direction.DOWN, minX, minZ, maxX, maxZ, minY)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit()

                .material(material)
                .square(Direction.EAST, minZ, minY, maxZ, maxY, 1 - maxX)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit()

                .material(material)
                .square(Direction.WEST, minZ, minY, maxZ, maxY, minX)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit()

                .material(material)
                .square(Direction.SOUTH, minX, minY, maxX, maxY, 1 - maxZ)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit()

                .material(material)
                .square(Direction.NORTH, minX, minY, maxX, maxY, minZ)
                .spriteColor(0, color, color, color, color)
                .spriteUnitSquare(0)
                .spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED)
                .emit();
    }
}