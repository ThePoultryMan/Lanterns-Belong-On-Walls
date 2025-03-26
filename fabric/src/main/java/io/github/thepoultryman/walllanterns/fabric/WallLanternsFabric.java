package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.api.ModInitializer;

public final class WallLanternsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WallLanterns.init();
    }
}
