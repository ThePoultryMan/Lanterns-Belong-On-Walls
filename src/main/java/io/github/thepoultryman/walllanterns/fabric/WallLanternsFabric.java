//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.arrp_but_different.fabric.ARRPEvent;
import io.github.thepoultryman.walllanterns.WallLanternBlock;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public final class WallLanternsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WallLanternsRegistry wallLanternsRegistry = new WallLanternsRegistry();
        FabricLoader.getInstance().getEntrypoints(WallLanterns.MOD_ID, WallLanternsEntrypoint.class).forEach(
                (entrypoint) -> entrypoint.registerLanterns(wallLanternsRegistry)
        );
        WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
            ResourceLocation lanternLocation = WallLanterns.dynamicResourceLocation(
                    wallLantern.getResourceLocation()
            );
            WallLanternBlock block = new WallLanternBlock(
                    Blocks.LANTERN.properties()
                            .setId(ResourceKey.create(Registries.BLOCK, lanternLocation)),
                    wallLantern.getBlockItem(),
                    wallLantern.getOptions()
            );
            Registry.register(
                    BuiltInRegistries.BLOCK,
                    lanternLocation,
                    block
            );
            WallLanterns.LANTERN_WRAPPERS.put(
                    wallLantern.getResourceLocation(),
                    new WallLanternWrapper(() -> block, wallLantern.getOptions())
            );
        });

        ARRPEvent.BEFORE_USER.register(resources -> resources.add(WallLanterns.createRuntimePack()));
    }
}
*///?}
