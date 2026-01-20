package io.github.thepoultryman.walllanterns;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallLanternOptions {
    public static final MapCodec<WallLanternOptions> CODEC;

    private boolean cutout;
    private WeatheringCopper.WeatherState weatheringState;
    private VoxelShape shape;
    private Identifier lanternModel = Identifier.withDefaultNamespace("block/lantern");
    private Identifier lanternHolderModel = Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID, "block/wall_lantern_holder");

    public WallLanternOptions() {}

    public WallLanternOptions(boolean cutout, WeatheringCopper.WeatherState weatheringState, Object shape, Identifier lanternModel, Identifier lanternHolderModel) {
        this.cutout = cutout;
        this.weatheringState = weatheringState;
        this.shape = (VoxelShape) shape;
        this.lanternModel = lanternModel;
        this.lanternHolderModel = lanternHolderModel;
    }

    public static WallLanternOptions create() {
        return new WallLanternOptions();
    }

    public WallLanternOptions cutout() {
        this.cutout = true;
        return this;
    }

    public boolean isCutoutLayer() {
        return this.cutout;
    }

    public WallLanternOptions weatheringState(WeatheringCopper.WeatherState weatheringState) {
        this.weatheringState = weatheringState;
        return this;
    }

    public WeatheringCopper.WeatherState getWeatheringState() {
        return this.weatheringState;
    }

    public WallLanternOptions shape(VoxelShape shape) {
        this.shape = shape;
        return this;
    }

    public VoxelShape getShape() {
        return this.shape;
    }

    public WallLanternOptions lanternModel(Identifier lanternModel) {
        this.lanternModel = lanternModel;
        return this;
    }

    public Identifier getLanternModel() {
        return this.lanternModel;
    }

    public WallLanternOptions lanternHolderModel(Identifier lanternHolderModel) {
        this.lanternHolderModel = lanternHolderModel;
        return this;
    }

    public Identifier getLanternHolderModel() {
        return this.lanternHolderModel;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL
                        .fieldOf("cutout")
                        .forGetter(WallLanternOptions::isCutoutLayer),
                WeatheringCopper.WeatherState.CODEC
                        .fieldOf("weathering")
                        .forGetter(WallLanternOptions::getWeatheringState),
                ExtraCodecs.JAVA
                        .fieldOf("voxel_shape")
                        .forGetter(WallLanternOptions::getShape),
                Identifier.CODEC
                        .fieldOf("lantern_model")
                        .forGetter(WallLanternOptions::getLanternModel),
                Identifier.CODEC
                        .fieldOf("lantern_holder_model")
                        .forGetter(WallLanternOptions::getLanternHolderModel)
        ).apply(instance, WallLanternOptions::new));
    }
}
