package io.github.thepoultryman.walllanterns.fabric;

import net.minecraft.resources.ResourceLocation;

public class VanillaLanternEntrypoint implements WallLanternsEntrypoint {
    @Override
    public void registerLanterns(WallLanternsRegistry registry) {
        registry.registerLantern(ResourceLocation.withDefaultNamespace("lantern"));
        registry.registerLantern(ResourceLocation.withDefaultNamespace("soul_lantern"));
    }
}
