package io.github.thepoultryman.walllanterns;

import net.minecraft.world.phys.shapes.VoxelShape;

public class WallLanternOptions {
    private boolean cutout;
    private VoxelShape shape;

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
}
