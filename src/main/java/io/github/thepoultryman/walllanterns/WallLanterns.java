package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.walllanterns.block.CompatibilityLanternEntity;
import io.github.thepoultryman.walllanterns.block.CompatibilityWallLantern;
//? if neoforge {
import io.github.thepoultryman.walllanterns.neoforge.WallLanternsEvent;
//? } else
//import io.github.thepoultryman.walllanterns.fabric.WallLanternsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

        //? if >= 1.21.9 {
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
        //? }
    }

    public static Identifier wallId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, "wall_" + path);
    }
}
