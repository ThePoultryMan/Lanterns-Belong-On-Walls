package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WallLanternList {
    private final List<WallLantern> list = new ArrayList<>();

    public boolean hasLantern(ResourceLocation resourceLocation) {
        for (WallLantern wallLantern : list) {
            if (wallLantern.getResourceLocation().equals(resourceLocation)) {
                return true;
            }
        }
        return false;
    }

    public void add(WallLantern wallLantern) {
        this.list.add(wallLantern);
    }

    public void forEach(Consumer<WallLantern> wallLanternConsumer) {
        this.list.forEach(wallLanternConsumer);
    }
}
