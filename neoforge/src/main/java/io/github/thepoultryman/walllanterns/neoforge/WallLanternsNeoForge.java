package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.arrp_but_different.neoforge.ARRPForNeoForge;
import io.github.thepoultryman.arrp_but_different.neoforge.ARRPNeoForgeEvent;
import io.github.thepoultryman.walllanterns.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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
                                            .setId(ResourceKey.create(Registries.BLOCK, lanternLocation)),
                                    wallLantern.getBlockItem(),
                                    wallLantern.getOptions()
                            );
                            registry.register(lanternLocation, block);
                            WallLanterns.LANTERN_WRAPPERS.put(
                                    wallLantern.getResourceLocation(),
                                    new WallLanternWrapper(
                                            () -> block,
                                            wallLantern.getOptions()
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
                    WallLanternOptions.create().cutout(),
                    ResourceLocation.withDefaultNamespace("lantern"),
                    Items.LANTERN
            ));
            event.addLantern(new WallLantern(
                    WallLanternOptions.create().cutout(),
                    ResourceLocation.withDefaultNamespace("soul_lantern"),
                    Items.SOUL_LANTERN
            ));
        }

        @SubscribeEvent
        private static void onClientSetup(FMLClientSetupEvent ignored) {
            WallLanterns.LANTERN_WRAPPERS.forEach((resourceLocation, wallLanternWrapper) -> {
                if (wallLanternWrapper.getOptions().isCutoutLayer()) {
                    ItemBlockRenderTypes.setRenderLayer(wallLanternWrapper.getWallLantern(), RenderType.CUTOUT);
                }
            });
        }
    }
}
