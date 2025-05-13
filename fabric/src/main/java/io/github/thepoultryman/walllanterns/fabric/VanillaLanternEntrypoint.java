package io.github.thepoultryman.walllanterns.fabric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class VanillaLanternEntrypoint implements WallLanternsEntrypoint {
    @Override
    public void registerLanterns(WallLanternsRegistry registry) {
        registry.registerLantern(ResourceLocation.withDefaultNamespace("lantern"), Items.LANTERN);
        registry.registerLantern(ResourceLocation.withDefaultNamespace("soul_lantern"), Items.SOUL_LANTERN);
    }
}
