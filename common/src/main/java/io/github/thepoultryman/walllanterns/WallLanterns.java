package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.arrp_but_different.api.RuntimeResourcePack;
import io.github.thepoultryman.arrp_but_different.json.state.JBlockModel;
import io.github.thepoultryman.arrp_but_different.json.state.JMultipart;
import io.github.thepoultryman.arrp_but_different.json.state.JState;
import io.github.thepoultryman.arrp_but_different.json.state.JWhen;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;

public final class WallLanterns {
    public static final String MOD_ID = "walllanterns";

    public static final WallLanternList WALL_LANTERNS = new WallLanternList();
    public static final HashMap<ResourceLocation, WallLanternWrapper> LANTERN_WRAPPERS = new HashMap<>();

    public static void init() {
        WALL_LANTERNS.add(new WallLantern(ResourceLocation.parse("minecraft:lantern")));
        WALL_LANTERNS.add(new WallLantern(ResourceLocation.parse("minecraft:soul_lantern")));
    }

    public static PackResources createRuntimePack() {
        RuntimeResourcePack pack = RuntimeResourcePack.create(ResourceLocation.fromNamespaceAndPath(WallLanterns.MOD_ID, "walllanterns"));

        WallLanterns.LANTERN_WRAPPERS.forEach((resourceLocation, wallLanternWrapper) -> {
            ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath("walllanterns_dynamic",
                    "wall_" + resourceLocation.getPath());
            ResourceLocation blockLocation = resourceLocation.withPath("block/" + resourceLocation.getPath());

            JBlockModel lanternModel = new JBlockModel(blockLocation);
            JBlockModel holderModel = new JBlockModel(
                    ResourceLocation.fromNamespaceAndPath(WallLanterns.MOD_ID, "block/wall_lantern_holder"));
            pack.addBlockSate(modelLocation, new JState()
                    .addAll(new JMultipart()
                                    .when(new JWhen().add(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH))
                                    .addModel(holderModel),
                            new JMultipart()
                                    .when(new JWhen().add(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST))
                                    .addModel(holderModel.clone().y(90)),
                            new JMultipart()
                                    .when(new JWhen().add(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH))
                                    .addModel(holderModel.clone().y(180)),
                            new JMultipart()
                                    .when(new JWhen().add(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST))
                                    .addModel(holderModel.clone().y(270)),
                            new JMultipart()
                                    .when(new JWhen().add(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                    )
                                    .addModel(lanternModel)
                    )
            );
        });
        return pack;
    }

    public static ResourceLocation dynamicResourceLocation(ResourceLocation resourceLocation) {
        return ResourceLocation.fromNamespaceAndPath(WallLanterns.MOD_ID + "_dynamic", "wall_" + resourceLocation.getPath());
    }
}
