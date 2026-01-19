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
        this.registerLantern(new WallLantern(options, identifier, blockItem));
    }

    public void registerLantern(WallLantern lantern) {
        WallLanterns.WALL_LANTERNS.add(lantern);
    }

    /^*
     * Alias for {@link WallLanternsRegistry#registerLantern(WallLantern)}
     * that helps projects using the same source code across loaders.
     ^/
    public void addLantern(WallLantern lantern) {
        this.registerLantern(lantern);
    }
}
*///?}
