package io.github.thepoultryman.walllanterns;

import java.util.function.Supplier;

public class WallLanternWrapper {
    private final Supplier<WallLanternBlock> wallLanternSupplier;
    private final boolean useCutoutLayer;

    public WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier) {
        this(wallLanternSupplier, false);
    }

    public WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier, boolean useCutoutLayer) {
        this.wallLanternSupplier = wallLanternSupplier;
        this.useCutoutLayer = useCutoutLayer;
    }

    public WallLanternBlock getWallLantern() {
        return this.wallLanternSupplier.get();
    }

    public boolean useCutoutLayer() {
        return this.useCutoutLayer;
    }
}
