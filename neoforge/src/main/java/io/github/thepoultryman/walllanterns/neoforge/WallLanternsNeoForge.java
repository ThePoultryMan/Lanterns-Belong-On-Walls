package io.github.thepoultryman.walllanterns.neoforge;

import com.mojang.serialization.Codec;
import io.github.thepoultryman.arrp_but_different.neoforge.ARRPForNeoForge;
import io.github.thepoultryman.arrp_but_different.neoforge.ARRPNeoForgeEvent;
import io.github.thepoultryman.walllanterns.WallLanternBlock;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.List;

@Mod(WallLanterns.MOD_ID)
public final class WallLanternsNeoForge {

    private static final Codec<List<ResourceLocation>> CODEC = ResourceLocation.CODEC.listOf();

    public WallLanternsNeoForge(IEventBus modBus) {
        // Run our common setup.
        WallLanterns.init();

        modBus.register(EventHandler.class);
        ARRPForNeoForge.ARRP_EVENT_BUS.addListener((ARRPNeoForgeEvent.BeforeUser event) -> {
            event.addPack(WallLanterns.createRuntimePack());
        });
    }

    private static class EventHandler {
        @SubscribeEvent
        private static void register(RegisterEvent event) {
            event.register(Registries.BLOCK, registry -> {
                WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
                    ResourceLocation lanternLocation = WallLanterns.dynamicResourceLocation(wallLantern.getResourceLocation());
                    WallLanternBlock block = new WallLanternBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                    .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                    );
                    registry.register(lanternLocation, block);
                    WallLanterns.LANTERN_WRAPPERS.put(
                            wallLantern.getResourceLocation(),
                            new WallLanternWrapper(() -> block)
                    );
                });
            });
        }
    }
}
