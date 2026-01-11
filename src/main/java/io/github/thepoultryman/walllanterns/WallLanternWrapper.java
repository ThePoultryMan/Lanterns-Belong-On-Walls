package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.walllanterns.block.WallLanternBlock;

import java.util.function.Supplier;

public record WallLanternWrapper(Supplier<WallLanternBlock> wallLanternSupplier, WallLanternOptions options) {
    public WallLanternBlock getWallLantern() {
        return this.wallLanternSupplier.get();
    }
}
