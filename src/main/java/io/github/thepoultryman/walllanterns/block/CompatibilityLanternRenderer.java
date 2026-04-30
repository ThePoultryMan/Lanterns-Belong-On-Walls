package io.github.thepoultryman.walllanterns.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;
//? if >= 1.21.9 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
//? }

//? if >= 26.1 {
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.state.level.CameraRenderState;
//? } else if >= 1.21.9 {
/*import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.state.CameraRenderState;
*///? } else if <= 1.21.6 {
/*import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
*///? }

//? if < 26.1
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;


public class CompatibilityLanternRenderer
        implements BlockEntityRenderer<@NonNull CompatibilityLanternEntity/*? if >= 1.21.9 {*/, @NonNull CompatibilityLanternState/*? }*/> {
    //? if < 26.1
    //private final BlockRenderDispatcher renderDispatcher;

    public CompatibilityLanternRenderer(BlockEntityRendererProvider.Context context) {
        //? if < 26.1
        //this.renderDispatcher = context.blockRenderDispatcher();
    }

    //? if >= 1.21.9
    @Override
    public CompatibilityLanternState createRenderState() {
        return new CompatibilityLanternState();
    }

    //? if >= 1.21.9
    @Override
    public void extractRenderState(@NonNull CompatibilityLanternEntity lanternEntity,
                                   @NonNull CompatibilityLanternState state, float partialTick, @NonNull Vec3 cameraPos
                                   //? if >= 1.21.9
            , ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
    ) {
        //? if >= 1.21.9 {
        BlockEntityRenderState.extractBase(lanternEntity, state, crumblingOverlay);
        state.setOverlayTexture(crumblingOverlay);
        //? } else {
        //? }
        state.setLevel(lanternEntity.getLevel());
        state.setMimickedLanternState(Blocks.LANTERN.defaultBlockState());

        var model = /*? if > 1.21.1 {*/state.getModelParts()/*? } else {*//*state.getBakedModel()*//*? }*/;
        boolean modelIsMissing = model == null /*? if > 1.21.1 {*/ || model.isEmpty() /*? }*/;
        if (lanternEntity.shouldForceLanternUpdate() || modelIsMissing) {
            lanternEntity.forceLanternUpdate(false);
            state.setMimickedLanternState(
                    lanternEntity.getMimickedLanternState() != null ? lanternEntity.getMimickedLanternState() : Blocks.LANTERN.defaultBlockState());
            //? if >= 26.1 {
            List<BlockStateModelPart> parts = new ArrayList<>();
            Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state.getMimickedLanternState())
                    .collectParts(state.getLevel().getRandom(), parts);
            state.setModelParts(parts);
            //? } else if > 1.21.1 {
            /*state.setModelParts(this.renderDispatcher.getBlockModel(state.getMimickedLanternState())
                    .collectParts(RandomSource.create()));
            *///? } else {
            /*state.setBakedModel(model);
             *///? }
        }
    }

    //? if >= 1.21.9 {
    @Override
    public void submit(CompatibilityLanternState state, @NonNull PoseStack poseStack,
                       @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState) {

        poseStack.pushPose();
        //? if >= 26.1 {
        submitNodeCollector.submitBlockModel(poseStack, RenderTypes.cutoutMovingBlock(), state.getModelParts(),
                new int[]{}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0
        );
        //? } else {
        /*submitNodeCollector.submitBlock(poseStack, state.getMimickedLanternState(), state.lightCoords,
                OverlayTexture.NO_OVERLAY, 0
        );
        *///? }
        poseStack.popPose();
    }
    //? } else {
    /*@Override
    public void render(@NonNull CompatibilityLanternEntity blockEntity, float f, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int i, int j) {
        CompatibilityLanternState state = new CompatibilityLanternState();
        this.extractRenderState(blockEntity, state, f, Vec3.ZERO);

        var model = /^? if > 1.21.1 {^/state.getModelParts()/^? } else {^//^state.getBakedModel()^//^? }^/;
        Level level = blockEntity.getLevel();
        if (level != null) {
            this.renderDispatcher.getModelRenderer().tesselateWithoutAO(state.getLevel(), model,
                    state.getMimickedLanternState(), blockEntity.getBlockPos(),
                    poseStack,
                    multiBufferSource.getBuffer(RenderType.solid()),
                    false,
                    //? if <= 1.21.1 {
                    /^level.random,
                    // This should probably be fetched from somewhere
                    248239,
                    ^///? }
                    0
            );
        }
        poseStack.popPose();
    }
    *///? }
}
