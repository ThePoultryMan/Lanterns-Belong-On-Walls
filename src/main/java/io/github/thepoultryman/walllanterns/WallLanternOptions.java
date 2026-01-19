package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallLanternOptions {
    private boolean cutout;
    private VoxelShape shape;
    private Identifier lanternModel = Identifier.withDefaultNamespace("block/lantern");
    private Identifier lanternHolderModel = Identifier.fromNamespaceAndPath(WallLanterns.MOD_ID, "block/wall_lantern_holder");

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
}
