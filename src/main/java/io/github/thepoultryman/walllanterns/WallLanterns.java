package io.github.thepoultryman.walllanterns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.api.StatementApi;

import java.util.ArrayList;
import java.util.List;

public class WallLanterns implements ModInitializer, StatementApi {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final List<String> STRUCTURE_LANTERNS = new ArrayList<>(List.of("minecraft:lantern", "minecraft:soul_lantern"));

	@Override
	public void onInitialize() {
		for (WallLanternsEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints(MOD_ID, WallLanternsEntrypoint.class)) {
			entrypoint.patchLanterns();
		}
		StateRefresher.INSTANCE.reorderBlockStates();
	}
}
