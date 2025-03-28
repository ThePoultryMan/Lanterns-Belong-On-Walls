package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.walllanterns.mixins.LanternBlockAccessor;
import io.github.thepoultryman.walllanterns.mixins.LanternBlockMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WallLanternBlock extends Block implements SimpleWaterloggedBlock {
    private static final VoxelShape ON_WALL_SHAPE_NORTH = Shapes.or(LanternBlockAccessor.getHangingShape(), Shapes.join(Block.box(6.9D, 8D, 6D, 9.1D, 15D, 16D), Block.box(6.9D, 8D, 6D, 9.1, 14D, 15D), BooleanOp.ONLY_SECOND));
    private static final VoxelShape ON_WALL_SHAPE_EAST = Shapes.or(LanternBlockAccessor.getHangingShape(), Shapes.join(Block.box(0D, 8D, 6.9D, 10D, 15D, 9.1D), Block.box(1D, 8D, 6.9D, 10D, 14D, 9.1D), BooleanOp.ONLY_SECOND));
    private static final VoxelShape ON_WALL_SHAPE_SOUTH = Shapes.or(LanternBlockAccessor.getHangingShape(), Shapes.join(Block.box(6.9D, 8D, 0D, 9.1D, 15D, 10D), Block.box(6.9D, 8D, 1D, 9.1D, 14D, 15D), BooleanOp.ONLY_SECOND));
    private static final VoxelShape ON_WALL_SHAPE_WEST = Shapes.or(LanternBlockAccessor.getHangingShape(), Shapes.join(Block.box(6D, 8, 6.9D, 16D, 15D, 9.1D), Block.box(6D, 8D, 6.9D, 15D, 14D, 9.1D), BooleanOp.ONLY_SECOND));

    public WallLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return Block.canSupportCenter(levelReader, blockPos.offset(facing.getOpposite().getUnitVec3i()), facing);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState blockState, LevelReader levelReader,
            ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction,
            BlockPos blockPos2, BlockState blockState2, RandomSource randomSource)
    {
        if (blockState.getValue(BlockStateProperties.WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
        }

        return levelReader.getBlockState(
                blockPos.offset(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().getUnitVec3i())
        ).getBlock() == Blocks.AIR ? Blocks.AIR.defaultBlockState() : blockState;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = this.getStateDefinition().any()
                .setValue(BlockStateProperties.WATERLOGGED,
                blockPlaceContext.getLevel()
                        .getFluidState(blockPlaceContext.getClickedPos()).getType() == Fluids.WATER)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getClickedFace());

        return blockState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST -> ON_WALL_SHAPE_EAST;
            case SOUTH -> ON_WALL_SHAPE_SOUTH;
            case WEST -> ON_WALL_SHAPE_WEST;
            default -> ON_WALL_SHAPE_NORTH;
        };
    }
}
