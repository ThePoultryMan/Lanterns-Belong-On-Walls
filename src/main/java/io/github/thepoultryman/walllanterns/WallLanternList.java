package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WallLanternList {
    private final List<WallLantern> list = new ArrayList<>();

    public boolean hasLantern(Identifier resourceLocation) {
        for (WallLantern wallLantern : list) {
            if (wallLantern.identifier().equals(resourceLocation)) {
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
