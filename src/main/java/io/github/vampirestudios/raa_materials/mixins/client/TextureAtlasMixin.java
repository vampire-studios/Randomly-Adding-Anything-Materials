package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_core.RAACore;
import net.fabricmc.fabric.impl.client.texture.FabricSprite;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(SpriteAtlasTexture.class)
public class TextureAtlasMixin {
	private static final int EMISSIVE_ALPHA = 253 << 24;
	@Shadow @Final private Identifier id;

	@Inject(method = "loadSprite", at = @At("HEAD"), cancellable = true)
	private void raa_loadSprite(ResourceManager resourceManager, Sprite.Info spriteInfo, int atlasWidth, int atlasHeight, int maxLevel, int posX, int posY, CallbackInfoReturnable<Sprite> info) {
		//Planning on making banners and shields work with glow ink sack
		if (!this.id.equals(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE) && !this.id.equals(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE)) {
			Identifier location = spriteInfo.getId();
			Identifier emissiveLocation = new Identifier(location.getNamespace(), "textures/" + location.getPath() + "_e.png");
			if (resourceManager.containsResource(emissiveLocation)) {
				NativeImage sprite = null;
				NativeImage emission = null;
				try {
					Identifier spriteLocation = new Identifier(location.getNamespace(), "textures/" + location.getPath() + ".png");
					Resource resource = resourceManager.getResource(spriteLocation);
					sprite = NativeImage.read(resource.getInputStream());
					resource.close();

					resource = resourceManager.getResource(emissiveLocation);
					emission = NativeImage.read(resource.getInputStream());
					resource.close();
				}
				catch (IOException e) {
					RAACore.LOGGER.info(e.getMessage());
				}
				if (sprite != null && emission != null) {
					int width = Math.min(sprite.getWidth(), emission.getWidth());
					int height = Math.min(sprite.getHeight(), emission.getHeight());
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							int argb = emission.getPixelColor(x, y);
							int alpha = (argb >> 24) & 255;
							if (alpha > 127) {
								int r = (argb >> 16) & 255;
								int g = (argb >> 8) & 255;
								int b = argb & 255;
								if (r > 0 || g > 0 || b > 0) {
									argb = (argb & 0x00FFFFFF) | (250 << 24);
									sprite.setPixelColor(x, y, argb);
								}
							}
						}
					}
					SpriteAtlasTexture self = (SpriteAtlasTexture) (Object) this;
					FabricSprite result = new FabricSprite(self, spriteInfo, maxLevel, atlasWidth, atlasHeight, posX, posY, sprite);
					info.setReturnValue(result);
				}
			}
		}
	}
}