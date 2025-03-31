package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;

public class WallLantern {
    private final Type type;
    private final ResourceLocation resourceLocation;

    public WallLantern(ResourceLocation resourceLocation) {
        this(Type.Standard, resourceLocation);
    }

    public WallLantern(Type type, ResourceLocation resourceLocation) {
        this.type = type;
        this.resourceLocation = resourceLocation;
    }

    public Type getType() {
        return this.type;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public enum Type {
        Standard,
        StandardCutout
    }
}
