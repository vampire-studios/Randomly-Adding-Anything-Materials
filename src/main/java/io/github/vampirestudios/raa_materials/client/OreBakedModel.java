package io.github.vampirestudios.raa_materials.client;

import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class OreBakedModel extends RAABakedModel {

    public OreBakedModel(Material material) {
        super(material);
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh(blockView, pos));
    }

    private Mesh mesh(BlockRenderView blockView, BlockPos pos) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        RenderMaterial mat = renderer.materialFinder().blendMode(0, BlendMode.CUTOUT_MIPPED).disableDiffuse(0, true).disableAo(0, false).find();
        int color = 0xFFFFFFFF;
        Sprite sprite;
        Identifier baseTexture = new Identifier(Registry.BLOCK.getId(Objects.requireNonNull(
                RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock()).getNamespace(), "block/" +
                Registry.BLOCK.getId(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock()).getPath());
        if (material.getOreInformation().getTargetId() != CustomTargets.DOES_NOT_APPEAR.getName()) {
            sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(baseTexture);
        } else {
            sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/oak_planks"));
        }

        this.renderBase(emitter, mat, sprite, renderer, blockView, pos, color);

        if (material.isGlowing()) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).emissive(0, true).find();
        } else {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
        }
        color = material.getColor();
        sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(this.material.getTexturesInformation().getOverlayTexture());

        this.renderOverlay(emitter, mat, sprite, color);

        return builder.build();
    }

    private void renderBase(QuadEmitter emitter, RenderMaterial mat, Sprite sprite, Renderer renderer, BlockRenderView blockView, BlockPos pos, int color) {
        if (!material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString())&&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString())) {
            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/grass_block_side"));
            Sprite sideOverlaySprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).
                    apply(new Identifier("block/grass_block_side_overlay"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/grass_block_top"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/dirt"));
            int color2 = 0xffffff;
            BlockColorProvider blockColor = ColorProviderRegistry.BLOCK.get(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.
                    get(material.getOreInformation().getTargetId())).getBlock());
            if (blockColor != null) {
                color2 = 0xff000000 | blockColor.getColor(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.
                        get(material.getOreInformation().getTargetId())).getBlock().getDefaultState(), blockView, pos, 1);
            }

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color2, color2, color2, color2)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV).emit();

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color2, color2, color2, color2)
                    .spriteBake(0, sideOverlaySprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color2, color2, color2, color2)
                    .spriteBake(0, sideOverlaySprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color2, color2, color2, color2)
                    .spriteBake(0, sideOverlaySprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color2, color2, color2, color2)
                    .spriteBake(0, sideOverlaySprite, MutableQuadView.BAKE_LOCK_UV).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/sandstone"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/sandstone_top"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/sandstone_bottom"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/crimson_nylium_side"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/crimson_nylium"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/netherrack"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/warped_nylium_side"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/warped_nylium"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/netherrack"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/basalt_side"));
            Sprite endSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/basalt_top"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, endSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, endSprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString())) {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/red_sandstone"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/red_sandstone_top"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).
                    apply(new Identifier("block/red_sandstone_bottom"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U).emit();
        } else {
            mat = renderer.materialFinder().disableDiffuse(0, false).blendMode(0, BlendMode.CUTOUT_MIPPED).find();
            Sprite sideSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/podzol_side"));
            Sprite topSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/podzol_top"));
            Sprite bottomSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier("block/dirt"));

            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sideSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, bottomSprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, topSprite, MutableQuadView.BAKE_LOCK_UV).emit();
        }
    }

    private void renderOverlay(QuadEmitter emitter, RenderMaterial mat, Sprite sprite, int color) {
        if (!material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString())&&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString()) &&
                !material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString())) {
            emitter.square(Direction.SOUTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.EAST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.WEST, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.NORTH, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.DOWN, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
        } else {
            emitter.square(Direction.UP, 0, 0, 1, 1, 0)
                    .material(mat)
                    .spriteColor(0, color, color, color, color)
                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getBlockPos()));
    }

    @Override
    public Sprite getSprite() {
        return MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(this.material.getTexturesInformation().getOverlayTexture());
    }

    @Override
    public ModelOverrideList getOverrides() {
        return new ItemProxy();
    }

    protected class ItemProxy extends ModelOverrideList {
        public ItemProxy() {
            super(null, null, null, Collections.emptyList());
        }

        @Override
        public BakedModel apply(BakedModel bakedModel, ItemStack stack, ClientWorld world, LivingEntity livingEntity) {
            return OreBakedModel.this;
        }
    }
}
