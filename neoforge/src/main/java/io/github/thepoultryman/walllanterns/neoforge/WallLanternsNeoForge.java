package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.arrp_but_different.neoforge.ARRPForNeoForge;
import io.github.thepoultryman.arrp_but_different.neoforge.ARRPNeoForgeEvent;
import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanternBlock;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(WallLanterns.MOD_ID)
public final class WallLanternsNeoForge {
    public WallLanternsNeoForge(IEventBus modBus) {
        modBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.BLOCK)) {
                modBus.post(new WallLanternsEvent());
                event.register(Registries.BLOCK, registry ->
                        WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
                            ResourceLocation lanternLocation = WallLanterns.dynamicResourceLocation(
                                    wallLantern.getResourceLocation()
                            );
                            WallLanternBlock block = new WallLanternBlock(
                                    BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                            .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                            );
                            registry.register(lanternLocation, block);
                            WallLanterns.LANTERN_WRAPPERS.put(
                                    wallLantern.getResourceLocation(),
                                    new WallLanternWrapper(
                                            () -> block,
                                            wallLantern.getType() == WallLantern.Type.StandardCutout
                                    )
                            );
                        })
                );
            }
        });
        ARRPForNeoForge.ARRP_EVENT_BUS.addListener((ARRPNeoForgeEvent.BeforeUser event) ->
                event.addPack(WallLanterns.createRuntimePack())
        );
        modBus.register(EventHandler.class);
    }

    private static class EventHandler {
        @SubscribeEvent
        private static void onWallLanternsEvent(WallLanternsEvent event) {
            event.addLantern(new WallLantern(
                    WallLantern.Type.StandardCutout,
                    ResourceLocation.parse("minecraft:lantern"))
            );
            event.addLantern(new WallLantern(
                    WallLantern.Type.StandardCutout,
                    ResourceLocation.parse("minecraft:soul_lantern"))
            );
        }

        @SubscribeEvent
        private static void onClientSetup(FMLClientSetupEvent ignored) {
            WallLanterns.LANTERN_WRAPPERS.forEach((resourceLocation, wallLanternWrapper) -> {
                if (wallLanternWrapper.useCutoutLayer()) {
                    ItemBlockRenderTypes.setRenderLayer(wallLanternWrapper.getWallLantern(), RenderType.CUTOUT);
                }
            });
        }
    }
}
