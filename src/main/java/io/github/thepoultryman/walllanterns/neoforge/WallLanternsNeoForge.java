//? if neoforge {
package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.walllanterns.RegistryHolder;
import io.github.thepoultryman.walllanterns.WallLanternOptions;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternEntity;
import io.github.thepoultryman.walllanterns.block.CompatibilityWallLantern;
import io.github.thepoultryman.walllanterns.block.WallLanternBlock;
import io.github.thepoultryman.walllanterns.block.WeatheringWallLanternBlock;
//import net.minecraft.client.renderer.ItemBlockRenderTypes;
//? if > 1.21.1 {
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
 //? } else
//import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jspecify.annotations.NonNull;

@Mod(WallLanterns.MOD_ID)
public final class WallLanternsNeoForge {
    public static final DeferredRegister<@NonNull BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE, WallLanterns.MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WallLanterns.MOD_ID);

    public WallLanternsNeoForge(IEventBus modBus) {
        WallLanterns.LANTERN_ENTITY_TYPE = new RegistryHolder<>(
                WallLanternsNeoForge.BLOCK_ENTITY_TYPES.register(WallLanterns.COMPAT_LANTERN_ID.getPath(),
                        //? if > 1.21.1 {
                        () -> new BlockEntityType<>(CompatibilityLanternEntity::new,
                                false, WallLanterns.COMPATIBILITY_WALL_LANTERN.get()
                        )
                        //? } else {
                        /*() -> BlockEntityType.Builder.of(CompatibilityLanternEntity::new,
                                WallLanterns.COMPATIBILITY_WALL_LANTERN.get()
                        ).build(null)
                        *///? }
                )
        );
        WallLanterns.COMPATIBILITY_WALL_LANTERN = new RegistryHolder<>(
                WallLanternsNeoForge.BLOCKS.register(WallLanterns.COMPAT_LANTERN_ID.getPath(),
                        () -> new CompatibilityWallLantern(
                                BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                //? if > 1.21.1 {
                                        .setId(ResourceKey.create(Registries.BLOCK,
                                                WallLanterns.COMPAT_LANTERN_ID
                                        ))
                                        //? }
                                ,
                                WallLanternOptions.create()
                        )
                )
        );

        BLOCK_ENTITY_TYPES.register(modBus);
        BLOCKS.register(modBus);
        modBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.BLOCK)) {
                modBus.post(new WallLanternsEvent());

                event.register(Registries.BLOCK, registry ->
                        WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
                            Identifier lanternLocation = WallLanterns.wallId(
                                    wallLantern.identifier().getPath()
                            );
                            WallLanternBlock block;
                            if (wallLantern.options().getWeatheringState() != null) {
                                block = new WeatheringWallLanternBlock(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                        //? if > 1.21.1
                                        .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                                        ,
                                        wallLantern.blockItem(),
                                        wallLantern.options()
                                );
                            } else {
                                block = new WallLanternBlock(
                                        BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                        //? if > 1.21.1
                                        .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                                        ,
                                        wallLantern.blockItem(),
                                        wallLantern.options()
                                );
                            }
                            registry.register(lanternLocation, block);
                            WallLanterns.LANTERN_WRAPPERS.put(
                                    wallLantern.identifier(),
                                    new WallLanternWrapper(
                                            () -> block,
                                            wallLantern.options()
                                    )
                            );
                        })
                );
            }
        });
        modBus.register(EventHandler.class);
    }

    private static class EventHandler {
        @SubscribeEvent
        private static void onWallLanternsEvent(WallLanternsEvent event) {
            WallLanterns.addVanillaLanterns(event);
        }

        @SubscribeEvent
        private static void onClientSetup(FMLClientSetupEvent ignored) {
            WallLanterns.LANTERN_WRAPPERS.forEach((resourceLocation, wallLanternWrapper) -> {
                if (wallLanternWrapper.options().isCutoutLayer()) {
//                    ItemBlockRenderTypes.setRenderLayer(wallLanternWrapper.getWallLantern(),
//                            //? if > 1.21.1 {
//                            ChunkSectionLayer.CUTOUT
//                             //? } else
//                            //RenderType.cutout()
//                    );
                }
            });
        }
    }
}
//?}
