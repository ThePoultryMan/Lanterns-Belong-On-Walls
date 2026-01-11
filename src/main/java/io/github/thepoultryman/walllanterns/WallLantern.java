package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public record WallLantern(WallLanternOptions options, Identifier identifier, Item blockItem) {
}
