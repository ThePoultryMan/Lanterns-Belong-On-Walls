//? if fabric {
/*package io.github.thepoultryman.walllanterns.fabric;

import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//? if >= 26.1 {
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
//? } else
//import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
//? if > 1.21.1 && < 26.1 {
/^import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
^///? } else if < 26.1 {
/^import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
^///? }

@Environment(EnvType.CLIENT)
public class WallLanternsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if >= 26.1 {
        BlockEntityRenderers.register(WallLanterns.LANTERN_ENTITY_TYPE.get(), CompatibilityLanternRenderer::new);
        //? } else {
        /^//BlockEntityRendererRegistry.register(WallLanterns.LANTERN_ENTITY_TYPE.get(), CompatibilityLanternRenderer::new);
        WallLanterns.LANTERN_WRAPPERS.forEach(
                (identifier, wallLanternWrapper) -> {
                    if (wallLanternWrapper.options().isCutoutLayer()) {
                        //? if > 1.21.1 {
                         BlockRenderLayerMap
                        //? } else
                        //BlockRenderLayerMap.INSTANCE
                                .putBlock(
                                        wallLanternWrapper.getWallLantern(),
                                        //? if > 1.21.1 {
                                        ChunkSectionLayer.CUTOUT
                                        //? } else
                                        //RenderType.cutout()
                                );
                    }
                }
        );
        ^///? }
    }
}
*///?}
