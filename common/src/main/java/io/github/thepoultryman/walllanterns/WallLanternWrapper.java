package io.github.thepoultryman.walllanterns;

import java.util.function.Supplier;

public class WallLanternWrapper {
    private final Supplier<WallLanternBlock> wallLanternSupplier;
    private final WallLanternOptions options;

    public WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier) {
        this(wallLanternSupplier, WallLanternOptions.create());
    }

    public WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier, WallLanternOptions options) {
        this.wallLanternSupplier = wallLanternSupplier;
        this.options = options;
    }

    public WallLanternBlock getWallLantern() {
        return this.wallLanternSupplier.get();
    }

    public WallLanternOptions getOptions() {
        return this.options;
    }
}
