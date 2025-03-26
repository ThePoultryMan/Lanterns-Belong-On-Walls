package io.github.thepoultryman.walllanterns;

import java.util.function.Supplier;

public class WallLanternWrapper {
    private final Supplier<WallLanternBlock> wallLanternSupplier;

    public WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier) {
        this.wallLanternSupplier = wallLanternSupplier;
    }

    public WallLanternBlock getWallLantern() {
        return this.wallLanternSupplier.get();
    }
}
