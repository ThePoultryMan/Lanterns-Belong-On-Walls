//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.arrp_but_different.fabric.ARRPEvent;
import io.github.thepoultryman.walllanterns.RegistryHolder;
import io.github.thepoultryman.walllanterns.WallLanternOptions;
import io.github.thepoultryman.walllanterns.WallLanternWrapper;
import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternEntity;
import io.github.thepoultryman.walllanterns.block.CompatibilityWallLantern;
import io.github.thepoultryman.walllanterns.block.WallLanternBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public final class WallLanternsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatibilityWallLantern lantern = Registry.register(BuiltInRegistries.BLOCK, WallLanterns.COMPAT_LANTERN_ID,
                new CompatibilityWallLantern(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                        .setId(ResourceKey.create(Registries.BLOCK, WallLanterns.COMPAT_LANTERN_ID)),
                        WallLanternOptions.create()
                )
        );
        WallLanterns.COMPATIBILITY_WALL_LANTERN = new RegistryHolder<>(() -> lantern);

        BlockEntityType<@NotNull CompatibilityLanternEntity> lanternEntity = Registry.register(
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
            Identifier lanternLocation = WallLanterns.dynamicIdentifier(wallLantern.identifier());
            WallLanternBlock block = new WallLanternBlock(
                    Blocks.LANTERN.properties().setId(ResourceKey.create(Registries.BLOCK, lanternLocation)),
                    wallLantern.blockItem(), wallLantern.options()
            );
            Registry.register(BuiltInRegistries.BLOCK, lanternLocation, block);
            WallLanterns.LANTERN_WRAPPERS.put(wallLantern.identifier(),
                    new WallLanternWrapper(() -> block, wallLantern.options())
            );
        });

        ARRPEvent.BEFORE_USER.register(resources -> resources.add(WallLanterns.createRuntimePack()));
    }
}
*///?}
