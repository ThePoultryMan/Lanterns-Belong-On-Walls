package io.github.thepoultryman.walllanterns.block;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CompatibilityLanternState extends BlockEntityRenderState {
    private BlockState mimickedLanternState;
    private List<BlockModelPart> modelParts;
    private ModelFeatureRenderer.CrumblingOverlay overlayTexture;
    private Level level;

    public BlockState getMimickedLanternState() {
        return this.mimickedLanternState;
    }

    public void setMimickedLanternState(BlockState mimickedLanternState) {
        this.mimickedLanternState = mimickedLanternState;
    }

    public List<BlockModelPart> getModelParts() {
        return this.modelParts;
    }

    public void setModelParts(List<BlockModelPart> modelParts) {
        this.modelParts = modelParts;
    }

    public ModelFeatureRenderer.CrumblingOverlay getOverlayTexture() {
        return this.overlayTexture;
    }

    public void setOverlayTexture(ModelFeatureRenderer.CrumblingOverlay overlayTexture) {
        this.overlayTexture = overlayTexture;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
