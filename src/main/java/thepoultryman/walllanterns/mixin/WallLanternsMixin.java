package thepoultryman.walllanterns.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thepoultryman.walllanterns.WallLanterns;

@Mixin(LanternBlock.class)
public abstract class WallLanternsMixin extends Block {
    private static final BooleanProperty ON_WALL = BooleanProperty.of("on_wall");

    @Shadow @Final public static BooleanProperty HANGING;

    @Shadow @Final public static BooleanProperty WATERLOGGED;

    public WallLanternsMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void injectMethod(AbstractBlock.Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(ON_WALL, false));
    }

    @Inject(at = @At("TAIL"), method = "appendProperties")
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.HORIZONTAL_FACING, ON_WALL);
    }

    @Inject(at = @At("RETURN"), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        boolean returnValue = false;
        Direction direction = state.get(Properties.HORIZONTAL_FACING);

        if (direction.getAxis() == Direction.Axis.Z)
            if (world.getBlockState(pos.add(0, 0, getDirectionalInt(direction))).getBlock() != Blocks.AIR)
                returnValue = true;
        if (direction.getAxis() == Direction.Axis.X)
            if (world.getBlockState(pos.add(getDirectionalInt(direction), 0, 0)).getBlock() != Blocks.AIR)
                returnValue = true;

        cir.setReturnValue(returnValue);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos.Mutable blockPos = ctx.getBlockPos().mutableCopy();
        BlockState blockState;
        Block upBlock = ctx.getWorld().getBlockState(blockPos.setY(blockPos.getY() + 1)).getBlock();
        Block downBlock = ctx.getWorld().getBlockState(blockPos.setY(blockPos.getY() - 2)).getBlock();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = this.getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                        .with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());

                if (downBlock != Blocks.AIR) {
                   return blockState.with(ON_WALL, false).with(HANGING, false);
                } else if (upBlock != Blocks.AIR) {
                    return blockState.with(ON_WALL, false).with(HANGING, true);
                } else {
                    return  blockState.with(ON_WALL, true).with(HANGING, true);
                }
            }
        }

        return null;
    }

    private int getDirectionalInt(Direction direction) {
        return switch (direction) {
            case NORTH, WEST -> 1;
            case SOUTH, EAST -> -1;
            default -> 0;
        };
    }
}
