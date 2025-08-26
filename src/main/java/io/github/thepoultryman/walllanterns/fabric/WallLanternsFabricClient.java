//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class WallLanternsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WallLanterns.LANTERN_WRAPPERS.forEach(
                (resourceLocation, wallLanternWrapper) -> {
                    if (wallLanternWrapper.getOptions().isCutoutLayer()) {
                        BlockRenderLayerMap.putBlock(wallLanternWrapper.getWallLantern(), ChunkSectionLayer.CUTOUT);
                    }
                }
        );
    }
}
*///?}
