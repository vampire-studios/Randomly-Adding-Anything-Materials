package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin<S extends MinecraftServer> {
	@Shadow public abstract SaveProperties getSaveProperties();
	@Shadow @Final protected LevelStorage.Session session;
	@Shadow @Final private ResourcePackManager dataPackManager;

	private static final String raa_datapackUrl = "https://launcher.mojang.com/v1/objects/622bf0fd298e1e164ecd05d866045ed5941283cf/CavesAndCliffsPreview.zip";
	private static final File raa_datapackLocation = new File("config/CavesAndCliffsPreview.zip");

	@Inject(method = "exit", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnExit(CallbackInfo info) {
		RAAMaterials.onServerStop();
	}

	@Inject(method = "loadWorld", at = @At("RETURN"))
	protected void loadWorld(CallbackInfo ci) {
		String levelFolderName = this.session.getDirectoryName();
		try {
			raa_addDatapack(levelFolderName, dataPackManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Inject(method = "createWorlds", at = @At("RETURN"))
	protected void createWorlds(CallbackInfo ci) {
		String levelFolderName = this.session.getDirectoryName();
		try {
			raa_addDatapack(levelFolderName, dataPackManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void raa_addDatapack(String worldName, ResourcePackManager dataPackManager) throws IOException {
		if(RAAMaterials.CONFIG.applyCavesAndCliffsPreviewPack) {
			if(!raa_datapackLocation.exists()) {
				try(BufferedInputStream in = new BufferedInputStream(new URL(raa_datapackUrl).openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(raa_datapackLocation)) {
					byte[] dataBuffer = new byte[1024];
					int bytesRead;
					while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
						fileOutputStream.write(dataBuffer, 0, bytesRead);
					}
				} catch(IOException ignored) { }
			}
			if(raa_datapackLocation.exists()) {
				Path dest = Paths.get("saves", worldName, "datapacks/CavesAndCliffsPreview.zip");
				Files.copy(raa_datapackLocation.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
				Collection<String> newEnabledProfiles = dataPackManager.getEnabledProfiles().stream().map(ResourcePackProfile::getName).collect(Collectors.toList());
				newEnabledProfiles.add("file/CavesAndCliffsPreview.zip");
				dataPackManager.setEnabledProfiles(newEnabledProfiles);
			}
		}
	}
}