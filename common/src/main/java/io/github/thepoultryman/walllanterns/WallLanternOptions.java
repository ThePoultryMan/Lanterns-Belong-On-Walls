package io.github.thepoultryman.walllanterns;

public class WallLanternOptions {
    private boolean cutout;

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
}
