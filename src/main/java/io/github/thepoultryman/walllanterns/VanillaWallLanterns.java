package io.github.thepoultryman.walllanterns;

import net.minecraft.util.Identifier;

public class VanillaWallLanterns implements WallLanternsEntrypoint {
    @Override
    public void patchLanterns() {
        this.addLantern(new Identifier("lantern"), new Identifier("soul_lantern"));
    }
}
