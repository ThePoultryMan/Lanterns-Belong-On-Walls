package thepoultryman.walllanterns.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
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
    private static final BooleanProperty ON_WALL = BooleanProperty.of("on_wall");

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
        boolean returnValue = false;
        Direction oppositeDirection = state.get(Properties.HORIZONTAL_FACING).getOpposite();
        Direction attachedDirection = attachedDirection(state).getOpposite();

        if (!world.getBlockState(pos.offset(attachedDirection)).isSideSolidFullSquare(world, pos.offset(attachedDirection), attachedDirection) && !world.getBlockState(pos.offset(attachedDirection)).isIn(BlockTags.UNSTABLE_BOTTOM_CENTER)) {
            returnValue = Block.sideCoversSmallSquare(world, pos.offset(attachedDirection(state).getOpposite()), attachedDirection(state));
            if (world.getBlockState(pos.offset(oppositeDirection)).getBlock() != Blocks.AIR) {
                returnValue = true;
            }
        }

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
