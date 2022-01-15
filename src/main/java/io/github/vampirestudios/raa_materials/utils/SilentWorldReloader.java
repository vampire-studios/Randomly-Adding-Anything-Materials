package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SilentWorldReloader extends Thread {
	private static boolean silent = false;
	private static SilentWorldReloader other;
	
	private final Minecraft client;
	private final PackRepository resourcePackManager;
	private final LevelRenderer worldRenderer;
	private final ReloadableResourceManager resourceManager;
	private final CompletableFuture<Unit> future;
	
	public SilentWorldReloader(Minecraft client, PackRepository resourcePackManager, LevelRenderer worldRenderer, ReloadableResourceManager resourceManager, CompletableFuture<Unit> future) {
		other = other == null ? this : other;
		this.client = client;
		this.resourcePackManager = resourcePackManager;
		this.worldRenderer = worldRenderer;
		this.resourceManager = resourceManager;
		this.future = future;
	}
	
	public void run() {
		if (other == this) {
			resourcePackManager.reload();
			List<PackResources> list = resourcePackManager.openAllSelected();
			ReloadInstance monitor = resourceManager.createReload(Util.backgroundExecutor(), client, future, list);
			monitor.done().thenRun(() -> {
				worldRenderer.allChanged();
				client.getItemRenderer().getItemModelShaper().rebuildCache();
				other = null;
			});
		}
	}
	
	public static void setSilent() {
		silent = true;
	}
	
	public static boolean isSilent() {
		if (silent) {
			silent = false;
			return true;
		}
		return false;
	}
}