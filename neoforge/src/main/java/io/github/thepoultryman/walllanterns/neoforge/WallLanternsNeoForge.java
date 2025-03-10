package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.neoforged.fml.common.Mod;

@Mod(WallLanterns.MOD_ID)
public final class WallLanternsNeoForge {
    public WallLanternsNeoForge() {
        // Run our common setup.
        WallLanterns.init();
    }
}
