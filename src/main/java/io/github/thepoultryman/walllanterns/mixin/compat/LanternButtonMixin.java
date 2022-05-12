package io.github.thepoultryman.walllanterns.mixin.compat;

import com.github.spaceman.LanternButtonBlock;
import io.github.thepoultryman.walllanterns.OnWallProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanternButtonBlock.class)
public abstract class LanternButtonMixin {
    @Inject(at = @At("TAIL"), method = "appendProperties")
    protected void walllanterns$appendWallProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.HORIZONTAL_FACING, OnWallProperty.ON_WALL);
    }
}
