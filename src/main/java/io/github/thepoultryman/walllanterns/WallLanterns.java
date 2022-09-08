package io.github.thepoultryman.walllanterns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WallLanterns implements ModInitializer {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final List<String> COMPAT_MODS = List.of("oxidized", "byg", "charm", "variant_lanterns");

	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
			if (FabricLoader.getInstance().isModLoaded("secretrooms"))
				ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "secretroomscompat"),
						modContainer, ResourcePackActivationType.ALWAYS_ENABLED);
			for (String modId : COMPAT_MODS) {
				if (FabricLoader.getInstance().isModLoaded(modId)) {
					ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "moddedlanternscompat"),
							modContainer, ResourcePackActivationType.ALWAYS_ENABLED);
					break;
				}
			}
		});
	}
}
