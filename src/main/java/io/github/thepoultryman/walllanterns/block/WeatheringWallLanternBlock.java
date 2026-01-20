package io.github.thepoultryman.walllanterns.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.thepoultryman.walllanterns.WallLanternOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringWallLanternBlock extends WallLanternBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringWallLanternBlock> CODEC =
            RecordCodecBuilder.mapCodec(
                    (instance) -> instance
                            .group(
                                    propertiesCodec(),
                                    BuiltInRegistries.ITEM.byNameCodec()
                                            .fieldOf("block_item")
                                            .forGetter(WeatheringWallLanternBlock::asItem),
                                    WallLanternOptions.CODEC
                                            .fieldOf("wall_lantern_options")
                                            .forGetter((lantern) -> lantern.options),
                                    WeatherState.CODEC
                                            .fieldOf("weathering_state")
                                            .forGetter(WeatheringWallLanternBlock::getAge)
                            )
                            .apply(instance, WeatheringWallLanternBlock::new)
            );
    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringWallLanternBlock(Properties properties, Item blockItem, WallLanternOptions options, WeatheringCopper.WeatherState weatherState) {
        super(properties, blockItem, options);
        this.weatherState = weatherState;
    }

    public WeatheringWallLanternBlock(Properties properties, Item blockItem, WallLanternOptions options) {
        this(properties, blockItem, options, options.getWeatheringState());
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
