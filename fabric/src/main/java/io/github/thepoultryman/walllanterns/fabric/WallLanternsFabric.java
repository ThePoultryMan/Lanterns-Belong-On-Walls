package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.arrp_but_different.fabric.ARRPEvent;
import io.github.thepoultryman.walllanterns.WallLanternBlock;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

public final class WallLanternsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WallLanternsRegistry wallLanternsRegistry = new WallLanternsRegistry();
        FabricLoader.getInstance().getEntrypoints(WallLanterns.MOD_ID, WallLanternsEntrypoint.class).forEach(
                (entrypoint) -> {
                    entrypoint.registerLanterns(wallLanternsRegistry);
                }
        );
        WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
            WallLanternBlock block = new WallLanternBlock(
                    Blocks.LANTERN.properties()
            );
            Registry.register(
                    BuiltInRegistries.BLOCK,
                    WallLanterns.dynamicResourceLocation(wallLantern.getResourceLocation()),
                    block
            );
            WallLanterns.LANTERN_WRAPPERS.put(
                    wallLantern.getResourceLocation(),
                    new WallLanternWrapper(() -> block)
            );
        });


        ARRPEvent.BEFORE_USER.register(resources -> {
            resources.add(WallLanterns.createRuntimePack());
        });
    }
}
