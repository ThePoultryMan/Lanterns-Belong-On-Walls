package io.github.thepoultryman.walllanterns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import safro.oxidized.registry.BlockRegistry;
import virtuoel.statement.api.StateRefresher;

import java.util.List;

public class WallLanterns implements ModInitializer {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final List<String> COMPAT_MODS = List.of("oxidized", "byg", "charm", "extendedcopper");

	@Override
	public void onInitialize() {
		StateRefresher.INSTANCE.addBlockProperty(Blocks.LANTERN, Properties.FACING, Direction.UP);
		StateRefresher.INSTANCE.addBlockProperty(Blocks.SOUL_LANTERN, Properties.FACING, Direction.UP);

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
//			if (FabricLoader.getInstance().isModLoaded("secretrooms")) {
//				ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "secretroomscompat"),
//						modContainer, ResourcePackActivationType.ALWAYS_ENABLED);
//				StateRefresher.INSTANCE.addBlockProperty(SecretRooms.LANTERN_BUTTON_BLOCK, LANTERN_DIRECTION, Direction.UP);
//				StateRefresher.INSTANCE.addBlockProperty(SecretRooms.SOUL_LANTERN_BUTTON_BLOCK, LANTERN_DIRECTION, Direction.UP);
//			}
			boolean activateCompat = false;

			for (String modId : COMPAT_MODS) {
				if (FabricLoader.getInstance().isModLoaded(modId)) {
					if (modId.equals("oxidized")) StateRefresher.INSTANCE.addBlockProperty(BlockRegistry.COPPER_LANTERN, Properties.FACING, Direction.UP);
					//if (modId.equals("extendedcopper")) StateRefresher.INSTANCE.addBlockProperty(ModBlocks.COPPER_LANTERN, Properties.FACING, Direction.UP);

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
