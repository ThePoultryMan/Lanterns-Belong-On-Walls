package io.github.thepoultryman.walllanterns.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;
//? if >= 1.21.9 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
//? } else if <= 1.21.6 {
/*import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
*///? }

public class CompatibilityLanternRenderer
        implements BlockEntityRenderer<@NonNull CompatibilityLanternEntity/*? if >= 1.21.9 {*/, @NonNull CompatibilityLanternState/*? }*/>
{
    private final BlockRenderDispatcher renderDispatcher;
    private final MultiBufferSource.BufferSource crumblingBuffer;

    public CompatibilityLanternRenderer(BlockEntityRendererProvider.Context context) {
        this.renderDispatcher = context.blockRenderDispatcher();
        this.crumblingBuffer = Minecraft.getInstance().renderBuffers().crumblingBufferSource();
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
    )
    {
        //? if >= 1.21.9 {
        BlockEntityRenderState.extractBase(lanternEntity, state, crumblingOverlay);
        state.setOverlayTexture(crumblingOverlay);
        //? } else {
        //? }
        state.setLevel(lanternEntity.getLevel());
        state.setMimickedLanternState(Blocks.LANTERN.defaultBlockState());

        var model = /*? if > 1.21.1 {*/state.getModelParts()/*? } else {*//*state.getBakedModel()*//*? }*/;
        boolean modelIsMissing = model == null /*? if > 1.21.1 {*/|| model.isEmpty() /*? }*/;
        if (lanternEntity.shouldForceLanternUpdate() || modelIsMissing) {
            lanternEntity.forceLanternUpdate(false);
            state.setMimickedLanternState(
                    lanternEntity.getMimickedLanternState() != null ? lanternEntity.getMimickedLanternState() : Blocks.LANTERN.defaultBlockState());
            //? if > 1.21.1 {
            state.setModelParts(this.renderDispatcher.getBlockModel(state.getMimickedLanternState())
                    .collectParts(RandomSource.create()));
            //? } else {
            /*state.setBakedModel(model);
            *///? }
        }
    }

    //? if >= 1.21.9 {
    @Override
    public void submit(CompatibilityLanternState state, @NonNull PoseStack poseStack,
            @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState)
    {
        int breakProgress = state.getOverlayTexture() != null ? state.getOverlayTexture().progress() : -1;
        // I am not 100% sure that the following is correct by any means, but it is similar to what
        // vanilla does, and it works.
        if (breakProgress >= 0) {
            poseStack.pushPose();

            VertexConsumer decalGenerator = new SheetedDecalTextureGenerator(
                    this.crumblingBuffer.getBuffer(ModelBakery.DESTROY_TYPES.get(breakProgress)), poseStack.last(),
                    1.0f
            );

            this.renderDispatcher.getModelRenderer()
                    .tesselateWithoutAO(state.getLevel(), state.getModelParts(), state.getMimickedLanternState(),
                            cameraRenderState.blockPos, poseStack, decalGenerator, false, 0
                    );
            poseStack.popPose();
        }

        poseStack.pushPose();
        submitNodeCollector.submitBlock(poseStack, state.getMimickedLanternState(), state.lightCoords,
                OverlayTexture.NO_OVERLAY, 0
        );
        poseStack.popPose();
    }
    //? } else {
    /*@Override
    public void render(@NonNull CompatibilityLanternEntity blockEntity, float f, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int i, int j) {
        CompatibilityLanternState state = new CompatibilityLanternState();
        this.extractRenderState(blockEntity, state, f, Vec3.ZERO);

//        int breakProgress = Minecraft.getInstance().levelRenderer.
//        if (blockEntity.getLevel() instanceof ClientLevel level) {
//
//        } else {
//
//        }

//        VertexConsumer decalGenerator = new SheetedDecalTextureGenerator(
//                this.crumblingBuffer.getBuffer(ModelBakery.DESTROY_TYPES.get(breakProgress)), poseStack.last(),
//                1.0f
//        );


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
