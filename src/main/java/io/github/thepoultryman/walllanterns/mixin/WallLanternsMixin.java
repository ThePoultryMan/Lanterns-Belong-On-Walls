package io.github.thepoultryman.walllanterns.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LanternBlock.class)
public abstract class WallLanternsMixin extends Block {
    @Shadow @Final public static BooleanProperty HANGING;

    @Shadow @Final public static BooleanProperty WATERLOGGED;

    @Shadow @Final protected static VoxelShape HANGING_SHAPE;

    @Shadow @Final protected static VoxelShape STANDING_SHAPE;
    // Voxel Shapes
    private static final VoxelShape ON_WALL_SHAPE_NORTH;
    private static final VoxelShape ON_WALL_SHAPE_EAST;
    private static final VoxelShape ON_WALL_SHAPE_SOUTH;
    private static final VoxelShape ON_WALL_SHAPE_WEST;

    public WallLanternsMixin(Settings settings) {
        super(settings);
    }

    // Blockstate Stuff

    @Inject(at = @At("TAIL"), method = "<init>")
    private void injectMethod(AbstractBlock.Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    @Inject(at = @At("TAIL"), method = "appendProperties")
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.FACING);
    }

    @Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Direction direction = state.get(Properties.FACING);
        boolean returnValue = Block.sideCoversSmallSquare(world, pos.offset(direction.getOpposite()), direction);

        cir.setReturnValue(returnValue);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return world.getBlockState(pos.offset(state.get(Properties.FACING).getOpposite())).getBlock() == Blocks.AIR ? Blocks.AIR.getDefaultState() : state;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .with(Properties.FACING, ctx.getSide());
        return blockState.with(HANGING, ctx.getSide() == Direction.DOWN);
    }

    // Visuals

    @Inject(at = @At("HEAD"), method = "getOutlineShape", cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
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
