package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class WallLanterns {
    public static final String MOD_ID = "walllanterns";

    public static final List<ResourceLocation> WALL_LANTERNS = new ArrayList<>();
    public static final HashMap<ResourceLocation, WallLanternWrapper> LANTERN_WRAPPERS = new HashMap<>();

    public static void init() {
        WALL_LANTERNS.add(ResourceLocation.parse("minecraft:lantern"));
        WALL_LANTERNS.add(ResourceLocation.parse("minecraft:soul_lantern"));
    }
}
