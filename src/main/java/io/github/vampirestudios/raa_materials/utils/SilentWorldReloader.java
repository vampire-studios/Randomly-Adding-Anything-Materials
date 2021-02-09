package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SilentWorldReloader extends Thread {
	private static boolean silent = false;
	private static SilentWorldReloader other;
	
	private final MinecraftClient client;
	private final ResourcePackManager resourcePackManager;
	private final WorldRenderer worldRenderer;
	private final ReloadableResourceManager resourceManager;
	private final CompletableFuture<Unit> future;
	
	public SilentWorldReloader(MinecraftClient client, ResourcePackManager resourcePackManager, WorldRenderer worldRenderer, ReloadableResourceManager resourceManager, CompletableFuture<Unit> future) {
		other = other == null ? this : other;
		this.client = client;
		this.resourcePackManager = resourcePackManager;
		this.worldRenderer = worldRenderer;
		this.resourceManager = resourceManager;
		this.future = future;
	}
	
	public void run() {
		if (other == this) {
			resourcePackManager.scanPacks();
			List<ResourcePack> list = resourcePackManager.createResourcePacks();
			ResourceReloadMonitor monitor = resourceManager.beginMonitoredReload(Util.getMainWorkerExecutor(), client, future, list);
			monitor.whenComplete().thenRun(() -> {
				worldRenderer.reload();
				client.getItemRenderer().getModels().reloadModels();
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