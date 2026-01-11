package io.github.thepoultryman.walllanterns.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompatibilityLanternRenderer implements BlockEntityRenderer<@NotNull CompatibilityLanternEntity, @NotNull CompatibilityLanternState> {
    private final BlockRenderDispatcher renderDispatcher;
    private final MultiBufferSource.BufferSource crumblingBuffer;

    public CompatibilityLanternRenderer(BlockEntityRendererProvider.Context context) {
        this.renderDispatcher = context.blockRenderDispatcher();
        this.crumblingBuffer = Minecraft.getInstance().renderBuffers().crumblingBufferSource();
    }

    @Override
    public CompatibilityLanternState createRenderState() {
        return new CompatibilityLanternState();
    }

    @Override
    public void extractRenderState(@NotNull CompatibilityLanternEntity lanternEntity,
            @NotNull CompatibilityLanternState state, float partialTick, @NotNull Vec3 cameraPos,
            ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderState.extractBase(lanternEntity, state, crumblingOverlay);
        state.setOverlayTexture(crumblingOverlay);
        state.setLevel(lanternEntity.getLevel());
        state.setMimickedLanternState(Blocks.LANTERN.defaultBlockState());

        if (lanternEntity.shouldForceLanternUpdate() || (state.getModelParts() == null || state.getModelParts()
                .isEmpty())) {
            lanternEntity.forceLanternUpdate(false);
            state.setMimickedLanternState(
                    lanternEntity.getMimickedLanternState() != null ? lanternEntity.getMimickedLanternState() : Blocks.LANTERN.defaultBlockState());
            state.setModelParts(this.renderDispatcher.getBlockModel(state.getMimickedLanternState())
                    .collectParts(RandomSource.create()));
        }
    }

    @Override
    public void submit(CompatibilityLanternState state, @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState)
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
}
