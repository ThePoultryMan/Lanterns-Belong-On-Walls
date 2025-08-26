package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WallLantern {
    private final WallLanternOptions options;
    private final ResourceLocation resourceLocation;
    private final Item blockItem;

    public WallLantern(ResourceLocation resourceLocation, Item blockItem) {
        this(WallLanternOptions.create(), resourceLocation, blockItem);
    }

    public WallLantern(WallLanternOptions options, ResourceLocation resourceLocation, Item blockItem) {
        this.options = options;
        this.resourceLocation = resourceLocation;
        this.blockItem = blockItem;
    }

    public WallLanternOptions getOptions() {
        return this.options;
    }

    public Item getBlockItem() {
        return this.blockItem;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
}
