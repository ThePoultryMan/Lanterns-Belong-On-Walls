package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.walllanterns.api.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.util.RegistryUtils;

public class WallLanterns implements ModInitializer {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		for (Identifier lanternIdentifier : Config.INSTANCE.getVanillaTypeLanterns()) {
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, lanternIdentifier).ifPresent(
					lantern -> StateRefresher.INSTANCE.addBlockProperty(lantern, Properties.FACING, Direction.UP)
			);
		}

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
			boolean activateCompat = false;

			for (String modId : Config.getSupportedMods()) {
				if (FabricLoader.getInstance().isModLoaded(modId)) {
					activateCompat = true;
				}
			}

			if (activateCompat) {
				ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "moddedlanternscompat"),
						modContainer, ResourcePackActivationType.ALWAYS_ENABLED);
			}
		});

		StateRefresher.INSTANCE.reorderBlockStates();
	}
}
