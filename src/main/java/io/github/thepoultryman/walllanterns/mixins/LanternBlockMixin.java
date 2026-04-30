package io.github.thepoultryman.walllanterns.mixins;

import io.github.thepoultryman.walllanterns.WallLantern;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LanternBlock.class)
public abstract class LanternBlockMixin extends Block {
    public LanternBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    private void walllanterns$getStateForPlacement(BlockPlaceContext blockPlaceContext,
            CallbackInfoReturnable<BlockState> cir)
    {
        if (blockPlaceContext.getClickedFace().getAxis().isHorizontal()) {
            Identifier key = BuiltInRegistries.BLOCK.getKey(((LanternBlock) (Object) this));
            if (WallLanterns.WALL_LANTERNS.hasLantern(key)) {
                cir.setReturnValue(WallLanterns.LANTERN_WRAPPERS.get(key).getWallLantern()
                        .getStateForPlacement(blockPlaceContext));
            } else {
                cir.setReturnValue(
                        WallLanterns.COMPATIBILITY_WALL_LANTERN.get().getStateForPlacement(blockPlaceContext));
            }
        }
    }
}
