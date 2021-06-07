package io.github.vampirestudios.raa_materials.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class RAAModMenuScreen implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(GeneralConfig.class, parent).get();
	}

}
