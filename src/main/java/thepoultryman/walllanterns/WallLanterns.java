package thepoultryman.walllanterns;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallLanterns implements ModInitializer {
	public static final String MOD_ID = "walllanterns";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Putting lanterns on walls.");
	}
}
