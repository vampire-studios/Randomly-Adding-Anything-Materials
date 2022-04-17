package io.github.vampirestudios.raa_materials.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

import static io.github.vampirestudios.raa_materials.RAAMaterials.MOD_ID;

public class RAAMaterialsClient implements RAAAddonClient {
	@Override
	public void onClientInitialize() {
		KeyMapping keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.raa_materials.fully_reload_assets", // The translation key of the keybinding's name
				InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"category.raa_materials" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.consumeClick()) {
				assert client.player != null;
				client.player.displayClientMessage(new TextComponent("Reloading assets fully!"), false);
				Minecraft.getInstance().delayTextureReload().thenRun(() ->
						Minecraft.getInstance().getItemRenderer().getItemModelShaper().rebuildCache());
			}
		});
	}

	@Override
	public String getId() {
		return MOD_ID;
	}

	@Override
	public String[] shouldLoadAfter() {
		return new String[]{};
	}

}