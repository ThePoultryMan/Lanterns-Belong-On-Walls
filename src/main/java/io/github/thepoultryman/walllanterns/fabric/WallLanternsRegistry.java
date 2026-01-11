//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanternOptions;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class WallLanternsRegistry {
    public void registerLantern(Identifier identifier, Item blockItem) {
        this.registerLantern(WallLanternOptions.create(), identifier, blockItem);
    }

    public void registerLantern(WallLanternOptions options, Identifier identifier, Item blockItem) {
        WallLanterns.WALL_LANTERNS.add(new WallLantern(options, identifier, blockItem));
    }
}
*///?}
