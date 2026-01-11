//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLanternOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

public class VanillaLanternEntrypoint implements WallLanternsEntrypoint {
    @Override
    public void registerLanterns(WallLanternsRegistry registry) {
        registry.registerLantern(WallLanternOptions.create().cutout(), Identifier.withDefaultNamespace("lantern"), Items.LANTERN);
        registry.registerLantern(WallLanternOptions.create().cutout(), Identifier.withDefaultNamespace("soul_lantern"), Items.SOUL_LANTERN);
    }
}
*///?}
