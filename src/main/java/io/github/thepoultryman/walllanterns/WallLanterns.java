package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.arrp_but_different.api.RuntimeResourcePack;
import io.github.thepoultryman.arrp_but_different.json.JTag;
import io.github.thepoultryman.arrp_but_different.json.loot.JLootTable;
import io.github.thepoultryman.arrp_but_different.json.loot.JPool;
import io.github.thepoultryman.arrp_but_different.json.model.JModel;
import io.github.thepoultryman.arrp_but_different.json.model.JTextures;
import io.github.thepoultryman.arrp_but_different.json.state.JBlockModel;
import io.github.thepoultryman.arrp_but_different.json.state.JMultipart;
import io.github.thepoultryman.arrp_but_different.json.state.JState;
import io.github.thepoultryman.arrp_but_different.json.state.JWhen;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternEntity;
import io.github.thepoultryman.walllanterns.block.CompatibilityWallLantern;
import io.github.thepoultryman.walllanterns.neoforge.WallLanternsEvent;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class WallLanterns {
    public static final String MOD_ID = "walllanterns";

    public static final WallLanternList WALL_LANTERNS = new WallLanternList();
    public static final HashMap<Identifier, WallLanternWrapper> LANTERN_WRAPPERS = new HashMap<>();
    public static final Identifier COMPAT_LANTERN_ID = Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID,
            "compatibility_lantern"
    );
    public static RegistryHolder<CompatibilityWallLantern> COMPATIBILITY_WALL_LANTERN;
    public static RegistryHolder<BlockEntityType<@NotNull CompatibilityLanternEntity>> LANTERN_ENTITY_TYPE;

    public static PackResources createRuntimePack() {
        RuntimeResourcePack pack = RuntimeResourcePack.create(
                Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID, "walllanterns"));
        JTag mineableWithPickaxe = new JTag();

        WallLanterns.LANTERN_WRAPPERS.forEach((resourceLocation, wallLanternWrapper) -> {
            Identifier modelLocation = Identifier.fromNamespaceAndPath("walllanterns_dynamic",
                    "wall_" + resourceLocation.getPath()
            );

            JBlockModel lanternModel = new JBlockModel(wallLanternWrapper.options().getLanternModel());
            JBlockModel holderModel = new JBlockModel(wallLanternWrapper.options().getLanternHolderModel());
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
                                    .when(new JWhen()
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                            .add(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                    )
                                    .addModel(lanternModel)
                    )
            );

            pack.addLootTable(Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID + "_dynamic",
                            "blocks/" + modelLocation.getPath()
                    ),
                    new JLootTable("minecraft:block")
                            .pool(
                                    new JPool()
                                            .bonusRolls(ConstantValue.exactly(0.0f))
                                            .condition(
                                                    ExplosionCondition.survivesExplosion().build()
                                            )
                                            .entry(
                                                    LootItem.lootTableItem(wallLanternWrapper.getWallLantern().asItem())
                                                            .build()
                                            )
                                            .rolls(ConstantValue.exactly(1.0f))
                            )
            );

            mineableWithPickaxe.add(modelLocation);
        });
        pack.addTag(Identifier.withDefaultNamespace("block/mineable/pickaxe"), mineableWithPickaxe);

        addHolderModel(pack, "");
        addHolderModel(pack, "copper");
        addHolderModel(pack, "exposed_copper");
        addHolderModel(pack, "weathered_copper");
        addHolderModel(pack, "oxidized_copper");

        return pack;
    }

    public static void addVanillaLanterns(/*? if neoforge { */WallLanternsEvent/*? } else {*//*WallLanternsRegistry*//*?}*/ event) {
        for (Item lantern : new Item[]{Items.LANTERN, Items.SOUL_LANTERN}) {
            Identifier lanternId = BuiltInRegistries.ITEM.getKey(lantern);
            event.addLantern(new WallLantern(
                    WallLanternOptions
                            .create()
                            .cutout()
                            .lanternModel(lanternId.withPrefix("block/")),
                    lanternId,
                    lantern
            ));
        }

        List<Tuple<Item, Optional<Item>>> copperLanterns = List.of(
                new Tuple<>(Items.COPPER_LANTERN.unaffected(), Optional.empty()),
                new Tuple<>(Items.COPPER_LANTERN.exposed(), Optional.empty()),
                new Tuple<>(Items.COPPER_LANTERN.weathered(), Optional.empty()),
                new Tuple<>(Items.COPPER_LANTERN.oxidized(), Optional.empty()),
                new Tuple<>(Items.COPPER_LANTERN.waxed(), Optional.of(Items.COPPER_LANTERN.unaffected())),
                new Tuple<>(Items.COPPER_LANTERN.waxedExposed(), Optional.of(Items.COPPER_LANTERN.exposed())),
                new Tuple<>(Items.COPPER_LANTERN.waxedWeathered(), Optional.of(Items.COPPER_LANTERN.weathered())),
                new Tuple<>(Items.COPPER_LANTERN.waxedOxidized(), Optional.of(Items.COPPER_LANTERN.oxidized()))
        );

        for (int i = 0; i < copperLanterns.size(); i++) {
            WeatheringCopper.WeatherState copperState = WeatheringCopper.WeatherState.BY_ID.apply(i % 4);
            String copperStateString = copperState.getSerializedName() + "_";
            if (copperState == WeatheringCopper.WeatherState.UNAFFECTED) {
                copperStateString = "";
            }

            Tuple<Item, Optional<Item>> lantern = copperLanterns.get(i);
            WallLanternOptions options = WallLanternOptions
                    .create()
                    .cutout()
                    .weatheringState(copperState)
                    .lanternModel(BuiltInRegistries.ITEM.getKey(lantern.getB().orElse(lantern.getA()))
                            .withPrefix("block/"));
            event.addLantern(new WallLantern(
                    options.lanternHolderModel(Identifier.fromNamespaceAndPath(
                            WallLanterns.MOD_ID,
                            "block/" + copperStateString + "copper_wall_lantern_holder"
                    )),
                    BuiltInRegistries.ITEM.getKey(lantern.getA()),
                    lantern.getA()
            ));
        }
    }

    public static void addHolderModel(RuntimeResourcePack pack, String pathPiece) {
        String blockPath = "block/" + pathPiece + (!pathPiece.isEmpty() ? "_" : "") + "wall_lantern_holder";
        pack.addModel(
                Identifier.fromNamespaceAndPath(MOD_ID, blockPath),
                new JModel()
                        .parent("walllanterns:block/wall_lantern_holder_template")
                        .renderType("minecraft:cutout")
                        .textures(
                                new JTextures()
                                        .add("lantern_holder", MOD_ID + ":" + blockPath)
                        )
        );
    }

    public static Identifier dynamicIdentifier(Identifier resourceLocation) {
        return dynamicIdentifier(resourceLocation.getPath());
    }

    public static Identifier dynamicIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID + "_dynamic", "wall_" + path);
    }
}
