//? if neoforge {
package io.github.thepoultryman.walllanterns.neoforge;

import io.github.thepoultryman.walllanterns.WallLanterns;
import io.github.thepoultryman.walllanterns.block.CompatibilityLanternRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = WallLanterns.MOD_ID, dist = Dist.CLIENT)
public class WallLanternsClientNeoForge {
    public WallLanternsClientNeoForge(IEventBus modBus) {
        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> event.registerBlockEntityRenderer(
                WallLanterns.LANTERN_ENTITY_TYPE.get(), CompatibilityLanternRenderer::new));
    }
}
//? }
