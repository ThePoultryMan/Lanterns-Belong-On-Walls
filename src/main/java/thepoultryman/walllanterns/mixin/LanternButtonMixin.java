package thepoultryman.walllanterns.mixin;

import com.github.spaceman.LanternButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static thepoultryman.walllanterns.OnWallProperty.ON_WALL;

@Mixin(LanternButtonBlock.class)
public abstract class LanternButtonMixin {
    @Inject(at = @At("TAIL"), method = "appendProperties")
    protected void walllanterns$appendWallProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.HORIZONTAL_FACING, ON_WALL);
    }
}
