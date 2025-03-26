package io.github.thepoultryman.walllanterns.mixins;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LanternBlock.class)
public class LanternBlockAccessor {
    @Accessor("HANGING_AABB")
    public static VoxelShape getHangingShape() {
        throw new AssertionError();
    }

    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    private void walllanterns$getStateForPlacement(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<BlockState> cir) {
        if (blockPlaceContext.getClickedFace().getAxis().isHorizontal()) {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(((LanternBlock)(Object)this));
            if (WallLanterns.WALL_LANTERNS.contains(key)) {
                cir.setReturnValue(WallLanterns.LANTERN_WRAPPERS.get(key).getWallLantern().getStateForPlacement(blockPlaceContext));
            }
        }
    }
}
