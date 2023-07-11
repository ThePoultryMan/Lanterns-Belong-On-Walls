package io.github.thepoultryman.walllanterns.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LanternBlock.class)
public abstract class WallLanternsMixin extends Block {
    @Shadow @Final public static BooleanProperty HANGING;

    @Shadow @Final public static BooleanProperty WATERLOGGED;

    @Shadow @Final protected static VoxelShape HANGING_SHAPE;

    @Shadow @Final protected static VoxelShape STANDING_SHAPE;

    // Voxel Shapes
    @Unique
    private static final VoxelShape ON_WALL_SHAPE_NORTH;
    @Unique
    private static final VoxelShape ON_WALL_SHAPE_EAST;
    @Unique
    private static final VoxelShape ON_WALL_SHAPE_SOUTH;
    @Unique
    private static final VoxelShape ON_WALL_SHAPE_WEST;

    public WallLanternsMixin(Settings settings) {
        super(settings);
        throw new IllegalStateException("This constructor should NEVER be called. Don't call this unless you know what you're doing.");
    }

    // Blockstate Stuff

    @Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        boolean returnValue;
        if (!state.contains(Properties.FACING)) {
            Direction direction = state.get(HANGING) ? Direction.UP : Direction.DOWN;
            returnValue = Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
        } else {
            Direction direction = state.get(Properties.FACING);
            returnValue = Block.sideCoversSmallSquare(world, pos.offset(direction.getOpposite()), direction);
        }

        cir.setReturnValue(returnValue);
    }

    @Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
    public void getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (!state.contains(Properties.FACING)) return;
        if (state.get(WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        cir.setReturnValue(
                world.getBlockState(pos.offset(state.get(Properties.FACING).getOpposite())).getBlock() == Blocks.AIR ? Blocks.AIR.getDefaultState() : state
        );
    }

    @Inject(at = @At("HEAD"), method = "getPlacementState", cancellable = true)
    public void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockState = this.getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        if (!this.getDefaultState().contains(Properties.FACING)) {
            blockState = blockState.with(HANGING, ctx.getSide() == Direction.DOWN);
        } else {
            blockState = blockState.with(Properties.FACING, ctx.getSide()).with(HANGING, ctx.getSide() == Direction.DOWN);
        }
        cir.setReturnValue(blockState);
    }

    // Visuals

    @Inject(at = @At("HEAD"), method = "getOutlineShape", cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!state.contains(Properties.FACING)) return;
        cir.setReturnValue(switch(state.get(Properties.FACING)) {
            case NORTH -> ON_WALL_SHAPE_NORTH;
            case EAST -> ON_WALL_SHAPE_EAST;
            case SOUTH -> ON_WALL_SHAPE_SOUTH;
            case WEST -> ON_WALL_SHAPE_WEST;
            case UP -> STANDING_SHAPE;
            default -> HANGING_SHAPE;
        });
    }

    static {
        ON_WALL_SHAPE_NORTH = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6.9D, 8D, 6D, 9.1D, 15D, 16D), Block.createCuboidShape(6.9D, 8D, 6D, 9.1, 14D, 15D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_EAST = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(0D, 8D, 6.9D, 10D, 15D, 9.1D), Block.createCuboidShape(1D, 8D, 6.9D, 10D, 14D, 9.1D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_SOUTH = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6.9D, 8D, 0D, 9.1D, 15D, 10D), Block.createCuboidShape(6.9D, 8D, 1D, 9.1D, 14D, 15D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_WEST = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6D, 8, 6.9D, 16D, 15D, 9.1D), Block.createCuboidShape(6D, 8D, 6.9D, 15D, 14D, 9.1D), BooleanBiFunction.ONLY_FIRST));
    }
}
