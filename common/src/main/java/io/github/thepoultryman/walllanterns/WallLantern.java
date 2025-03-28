package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;

public class WallLantern {
    private final Type type;
    private final ResourceLocation resourceLocation;

    public WallLantern(ResourceLocation block) {
        this.type = Type.Standard;
        this.resourceLocation = block;
    }

    public Type getType() {
        return this.type;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public enum Type {
        Standard
    }
}
