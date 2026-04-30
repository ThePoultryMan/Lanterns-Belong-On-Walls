package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WallLanternList {
    private final List<WallLantern> list = new ArrayList<>();

    public boolean hasLantern(Identifier resourceLocation) {
        return list.stream().anyMatch((lantern) -> lantern.identifier().equals(resourceLocation));
    }

    public void add(WallLantern wallLantern) {
        this.list.add(wallLantern);
    }

    public void forEach(Consumer<WallLantern> wallLanternConsumer) {
        this.list.forEach(wallLanternConsumer);
    }
}
