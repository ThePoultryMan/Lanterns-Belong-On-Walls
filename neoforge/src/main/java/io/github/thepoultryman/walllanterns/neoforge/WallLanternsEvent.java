package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class WallLanternsEvent extends Event implements IModBusEvent {
    public void addLantern(WallLantern wallLantern) {
        WallLanterns.WALL_LANTERNS.add(wallLantern);
    }
}
