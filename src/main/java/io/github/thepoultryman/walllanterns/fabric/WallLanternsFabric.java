//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.RegistryHolder;
import io.github.thepoultryman.walllanterns.WallLanternOptions;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternEntity;
import io.github.thepoultryman.walllanterns.block.CompatibilityWallLantern;
import io.github.thepoultryman.walllanterns.block.WallLanternBlock;
import io.github.thepoultryman.walllanterns.block.WeatheringWallLanternBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public final class WallLanternsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatibilityWallLantern lantern = Registry.register(BuiltInRegistries.BLOCK, WallLanterns.COMPAT_LANTERN_ID,
                new CompatibilityWallLantern(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                        //? if > 1.21.1
                        .setId(ResourceKey.create(Registries.BLOCK, WallLanterns.COMPAT_LANTERN_ID))
                        ,
                        WallLanternOptions.create()
                )
        );
        WallLanterns.COMPATIBILITY_WALL_LANTERN = new RegistryHolder<>(() -> lantern);

        BlockEntityType<@NonNull CompatibilityLanternEntity> lanternEntity = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE, WallLanterns.COMPAT_LANTERN_ID,

                FabricBlockEntityTypeBuilder.create(CompatibilityLanternEntity::new,
                        WallLanterns.COMPATIBILITY_WALL_LANTERN.get()
                ).build()
        );
        WallLanterns.LANTERN_ENTITY_TYPE = new RegistryHolder<>(() -> lanternEntity);

        WallLanternsRegistry wallLanternsRegistry = new WallLanternsRegistry();
        FabricLoader.getInstance().getEntrypoints(WallLanterns.MOD_ID, WallLanternsEntrypoint.class)
                .forEach((entrypoint) -> entrypoint.registerLanterns(wallLanternsRegistry));
        WallLanterns.WALL_LANTERNS.forEach((wallLantern) -> {
            Identifier lanternLocation = WallLanterns.wallId(wallLantern.identifier().getPath());
            WallLanternBlock block;
            if (wallLantern.options().getWeatheringState() == null) {
                 block = new WallLanternBlock(
                        Blocks.LANTERN.properties()
                                //? if > 1.21.1
                                .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                        ,
                        wallLantern.blockItem(),
                        wallLantern.options()
                );
            } else {
                block = new WeatheringWallLanternBlock(
                        Blocks.LANTERN.properties()
                                //? if > 1.21.1
                                .setId(ResourceKey.create(Registries.BLOCK, lanternLocation))
                        ,
                        wallLantern.blockItem(),
                        wallLantern.options(),
                        wallLantern.options().getWeatheringState()
                );
            }
            Registry.register(BuiltInRegistries.BLOCK, lanternLocation, block);
            WallLanterns.LANTERN_WRAPPERS.put(wallLantern.identifier(),
                    new WallLanternWrapper(() -> block, wallLantern.options())
            );
        });

        // oxidation
        String[] baseCopperLanternIds = {
                "copper_lantern",
                "exposed_copper_lantern",
                "weathered_copper_lantern",
                "oxidized_copper_lantern",
        };
        for (int i = 0; i < baseCopperLanternIds.length - 1; i++) {
            Optional<Block> maybeLess = BuiltInRegistries.BLOCK.getOptional(WallLanterns.wallId(baseCopperLanternIds[i]));
            Optional<Block> maybeMore = BuiltInRegistries.BLOCK.getOptional(WallLanterns.wallId(baseCopperLanternIds[i + 1]));
            if (maybeLess.isPresent() && maybeMore.isPresent()) {
                //? if >= 26.1 {
                OxidizableBlocksRegistry.registerNextStage(maybeLess.get(), maybeMore.get());
                //? } else
                //OxidizableBlocksRegistry.registerOxidizableBlockPair(maybeLess.get(), maybeMore.get());
            }
        }
        // waxing
        for (String copperLanternId : baseCopperLanternIds) {
            Optional<Block> maybeUnwaxed = BuiltInRegistries.BLOCK.getOptional(WallLanterns.wallId(copperLanternId));
            Optional<Block> maybeWaxed = BuiltInRegistries.BLOCK.getOptional(WallLanterns.wallId("waxed_" + copperLanternId));
            if (maybeUnwaxed.isPresent() && maybeWaxed.isPresent()) {
                //? if >= 26.1 {
                OxidizableBlocksRegistry.registerWaxable(maybeUnwaxed.get(), maybeWaxed.get());
                //? } else
                //OxidizableBlocksRegistry.registerWaxableBlockPair(maybeUnwaxed.get(), maybeWaxed.get());
            }
        }
    }
}
*///?}
