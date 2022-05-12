package io.github.thepoultryman.walllanterns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallLanterns implements ModInitializer {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
			if (FabricLoader.getInstance().isModLoaded("secretrooms"))
				ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "secretroomscompat"),
						modContainer, ResourcePackActivationType.ALWAYS_ENABLED);
		});
	}
}