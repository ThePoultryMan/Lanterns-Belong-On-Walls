package io.github.thepoultryman.walllanterns.block;

//? if <= 1.21.1
//import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >= 1.21.9 {
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
//? if >= 26.1 {
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
//? } else
//import net.minecraft.client.renderer.block.model.BlockStateModelPart;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
//? }

import java.util.List;
public class CompatibilityLanternState /*? if >= 1.21.9 { */extends BlockEntityRenderState/*? }*/ {
    private BlockState mimickedLanternState;
    //? if > 1.21.1 {
    private List<BlockStateModelPart> modelParts;
    //? } else
    //private BakedModel bakedModel;
    //? if >= 1.21.9
    private ModelFeatureRenderer.CrumblingOverlay overlayTexture;
    private Level level;

    public BlockState getMimickedLanternState() {
        return this.mimickedLanternState;
    }

    public void setMimickedLanternState(BlockState mimickedLanternState) {
        this.mimickedLanternState = mimickedLanternState;
    }

    //? if > 1.21.1 {
    public List<BlockStateModelPart> getModelParts() {
        return this.modelParts;
    }

    public void setModelParts(List<BlockStateModelPart> modelParts) {
        this.modelParts = modelParts;
    }
    //? } else {
    /*public BakedModel getBakedModel() {
        return this.bakedModel;
    }

    public void setBakedModel(BakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }
    *///? }

    //? if >= 1.21.9 {
    public ModelFeatureRenderer.CrumblingOverlay getOverlayTexture() {
        return this.overlayTexture;
    }

    public void setOverlayTexture(ModelFeatureRenderer.CrumblingOverlay overlayTexture) {
        this.overlayTexture = overlayTexture;
    }
    //? }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
