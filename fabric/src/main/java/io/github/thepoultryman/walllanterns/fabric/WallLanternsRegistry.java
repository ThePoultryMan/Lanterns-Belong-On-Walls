package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.resources.ResourceLocation;

public class WallLanternsRegistry {
    public void registerLantern(ResourceLocation resourceLocation) {
        WallLanterns.WALL_LANTERNS.add(new WallLantern(resourceLocation));
    }
}
