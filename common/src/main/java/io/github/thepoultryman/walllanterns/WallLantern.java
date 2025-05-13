package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WallLantern {
    private final Type type;
    private final ResourceLocation resourceLocation;
    private final Item blockItem;

    public WallLantern(ResourceLocation resourceLocation, Item blockItem) {
        this(Type.Standard, resourceLocation, blockItem);
    }

    public WallLantern(Type type, ResourceLocation resourceLocation, Item blockItem) {
        this.type = type;
        this.resourceLocation = resourceLocation;
        this.blockItem = blockItem;
    }

    public Type getType() {
        return this.type;
    }

    public Item getBlockItem() {
        return this.blockItem;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public enum Type {
        Standard,
        StandardCutout
    }
}
