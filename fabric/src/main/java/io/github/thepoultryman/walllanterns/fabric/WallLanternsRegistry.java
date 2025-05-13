package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WallLanternsRegistry {
    public void registerLantern(ResourceLocation resourceLocation, Item blockItem) {
        WallLanterns.WALL_LANTERNS.add(new WallLantern(resourceLocation, blockItem));
    }

    public void registerLantern(WallLantern.Type type, ResourceLocation resourceLocation, Item blockItem) {
        WallLanterns.WALL_LANTERNS.add(new WallLantern(type, resourceLocation, blockItem));
    }
}
