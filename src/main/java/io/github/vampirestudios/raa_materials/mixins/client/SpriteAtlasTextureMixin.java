package io.github.vampirestudios.raa_materials.mixins.client;

import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import net.fabricmc.fabric.impl.client.texture.FabricSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Set;

@Mixin(TextureAtlas.class)
public class SpriteAtlasTextureMixin {
	@Inject(method = "getBasicSpriteInfos", at = @At("HEAD"))
	private void loadSpritesStart(ResourceManager resourceManager, Set<ResourceLocation> ids, CallbackInfoReturnable<Collection<TextureAtlasSprite.Info>> info) {
		ids.removeAll(InnerRegistry.getTextureIDs());
	}
	
	@Inject(method = "getBasicSpriteInfos", at = @At("RETURN"), cancellable = true)
	private void loadSpritesEnd(ResourceManager resourceManager, Set<ResourceLocation> ids, CallbackInfoReturnable<Collection<TextureAtlasSprite.Info>> info) {
		Collection<TextureAtlasSprite.Info> result = info.getReturnValue();
		InnerRegistry.iterateTextures((id, img) -> {

			Pair<Integer, Integer> pair = img.getAnimation().getFrameSize(img.getWidth(), img.getHeight());
			TextureAtlasSprite.Info spriteInfo = new TextureAtlasSprite.Info(id, pair.getFirst(), pair.getSecond(), img.getAnimation());
			result.add(spriteInfo);
		});
		info.setReturnValue(result);
	}
	
	@Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At("HEAD"), cancellable = true)
	private void loadSprite(ResourceManager container, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<TextureAtlasSprite> callbackInfo) {
		BufferTexture texture = InnerRegistry.getTexture(info.name());
		if (texture != null) {
			try {

				TextureAtlas atlas = TextureAtlas.class.cast(this);
				TextureAtlasSprite sprite = new FabricSprite(atlas, info, maxLevel, atlasWidth, atlasHeight, x, y, texture.makeImage());
				callbackInfo.setReturnValue(sprite);
				callbackInfo.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}