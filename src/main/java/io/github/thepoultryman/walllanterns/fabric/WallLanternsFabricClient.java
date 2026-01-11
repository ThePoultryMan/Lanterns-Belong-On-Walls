//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@Environment(EnvType.CLIENT)
public class WallLanternsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(WallLanterns.LANTERN_ENTITY_TYPE.get(), CompatibilityLanternRenderer::new);
        WallLanterns.LANTERN_WRAPPERS.forEach(
                (identifier, wallLanternWrapper) -> {
                    if (wallLanternWrapper.options().isCutoutLayer()) {
                        BlockRenderLayerMap.putBlock(wallLanternWrapper.getWallLantern(), ChunkSectionLayer.CUTOUT);
                    }
                }
        );
    }
}
*///?}
