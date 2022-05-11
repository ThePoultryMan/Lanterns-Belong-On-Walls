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
import net.minecraft.world.World;
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

import static io.github.thepoultryman.walllanterns.OnWallProperty.ON_WALL;

@Mixin(LanternBlock.class)
public abstract class WallLanternsMixin extends Block {


    @Shadow @Final public static BooleanProperty HANGING;

    @Shadow @Final public static BooleanProperty WATERLOGGED;

    @Shadow @Final protected static VoxelShape HANGING_SHAPE;

    @Shadow
    protected static Direction attachedDirection(BlockState state) {
        return state.get(HANGING) ? Direction.DOWN : Direction.UP;
    }

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
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(ON_WALL, false));
    }

    @Inject(at = @At("TAIL"), method = "appendProperties")
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.HORIZONTAL_FACING, ON_WALL);
    }

    @Inject(at = @At("RETURN"), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        boolean returnValue = Block.sideCoversSmallSquare(world, pos.offset(attachedDirection(state).getOpposite()), attachedDirection(state));

        for (Direction direction : DIRECTIONS) {
            if (world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(world, pos.offset(direction), direction)) {
                returnValue = true;
                break;
            }
        }

        cir.setReturnValue(returnValue);
    }

    @Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate", cancellable = true)
    public void walllanterns$destroyIfNoAvailableBlock(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (world.getBlockState(pos.offset(state.get(Properties.HORIZONTAL_FACING).getOpposite())).getBlock() == Blocks.AIR)
            cir.setReturnValue(Blocks.AIR.getDefaultState());
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState;

        for (Direction placementDirection : ctx.getPlacementDirections()) {
            if (placementDirection.getAxis() == Direction.Axis.Y) {
                World world = ctx.getWorld();
                BlockPos pos = ctx.getBlockPos();

                blockState = this.getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                        .with(Properties.HORIZONTAL_FACING, Direction.NORTH);

                if (Block.sideCoversSmallSquare(world, pos.offset(Direction.DOWN), Direction.UP)) {
                   return blockState.with(ON_WALL, false).with(HANGING, false);
                } else if (Block.sideCoversSmallSquare(world, pos.offset(Direction.UP), Direction.DOWN)) {
                    return blockState.with(ON_WALL, false).with(HANGING, true);
                } else if (world.getBlockState(pos.offset(ctx.getPlayerFacing())).isSideSolidFullSquare(world, pos.offset(ctx.getPlayerFacing()), ctx.getPlayerFacing())) {
                    return blockState.with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite()).with(ON_WALL, true).with(HANGING, true);
                } else {
                    for (Direction direction : DIRECTIONS) {
                        if (world.getBlockState(pos.offset(direction)).isSideSolidFullSquare(world, pos.offset(direction), direction)) {
                            return blockState.with(Properties.HORIZONTAL_FACING, direction.getOpposite()).with(ON_WALL, true).with(HANGING, true);
                        }
                    }
                }
            }
        }

        return null;
    }

    // Visuals

    @Inject(at = @At("RETURN"), method = "getOutlineShape", cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.get(ON_WALL)) {
            cir.setReturnValue(switch(state.get(Properties.HORIZONTAL_FACING)) {
                case NORTH -> ON_WALL_SHAPE_NORTH;
                case EAST -> ON_WALL_SHAPE_EAST;
                case SOUTH -> ON_WALL_SHAPE_SOUTH;
                case WEST -> ON_WALL_SHAPE_WEST;
                default -> HANGING_SHAPE;
            });
        }
    }

    static {
        ON_WALL_SHAPE_NORTH = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6.9D, 8D, 6D, 9.1D, 15D, 16D), Block.createCuboidShape(6.9D, 8D, 6D, 9.1, 14D, 15D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_EAST = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(0D, 8D, 6.9D, 10D, 15D, 9.1D), Block.createCuboidShape(1D, 8D, 6.9D, 10D, 14D, 9.1D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_SOUTH = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6.9D, 8D, 0D, 9.1D, 15D, 10D), Block.createCuboidShape(6.9D, 8D, 1D, 9.1D, 14D, 15D), BooleanBiFunction.ONLY_FIRST));
        ON_WALL_SHAPE_WEST = VoxelShapes.union(HANGING_SHAPE, VoxelShapes.combineAndSimplify(Block.createCuboidShape(6D, 8, 6.9D, 16D, 15D, 9.1D), Block.createCuboidShape(6D, 8D, 6.9D, 15D, 14D, 9.1D), BooleanBiFunction.ONLY_FIRST));
    }
}
