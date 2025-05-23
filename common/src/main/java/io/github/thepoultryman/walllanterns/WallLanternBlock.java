package io.github.thepoultryman.walllanterns;

import io.github.thepoultryman.walllanterns.mixins.LanternBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
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
    private final VoxelShape onWallShapeNorth;
    private final VoxelShape onWallShapeEast;
    private final VoxelShape onWallShapeSouth;
    private final VoxelShape onWallShapeWest;

    private final Item blockItem;

    public WallLanternBlock(Properties properties, Item blockItem, WallLanternOptions options) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        this.blockItem = blockItem;

        VoxelShape baseLanternShape = options.getShape() != null ? options.getShape() : LanternBlockAccessor.getStandingShape();
        this.onWallShapeNorth = Shapes.or(baseLanternShape,
                Shapes.join(
                        Shapes.join(Shapes.empty(), Shapes.box(0.375, 0.5625, 0.9375, 0.625, 0.875, 1), BooleanOp.OR),
                        Shapes.box(0.4375, 0.6875, 0.3125, 0.5625, 0.8125, 0.9375), BooleanOp.OR
                )
        );
        this.onWallShapeEast = Shapes.or(baseLanternShape,
                Shapes.join(
                        Shapes.join(Shapes.empty(), Shapes.box(0, 0.5625, 0.375, 0.0625, 0.875, 0.625), BooleanOp.OR),
                        Shapes.box(0.0625, 0.6875, 0.4375, 0.6875, 0.8125, 0.5625), BooleanOp.OR
                )
        );
        this.onWallShapeSouth = Shapes.or(baseLanternShape,
                Shapes.join(
                        Shapes.join(Shapes.empty(), Shapes.box(0.375, 0.5625, 0, 0.625, 0.875, 0.0625), BooleanOp.OR),
                        Shapes.box(0.4375, 0.6875, 0.0625, 0.5625, 0.8125, 0.6875), BooleanOp.OR
                )
        );
        this.onWallShapeWest = Shapes.or(baseLanternShape,
                Shapes.join(
                        Shapes.join(Shapes.empty(), Shapes.box(0.9375, 0.5625, 0.375, 1, 0.875, 0.625), BooleanOp.OR),
                        Shapes.box(0.3125, 0.6875, 0.4375, 0.9375, 0.8125, 0.5625), BooleanOp.OR
                )
        );
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
        return this.getStateDefinition().any()
                .setValue(BlockStateProperties.WATERLOGGED,
                blockPlaceContext.getLevel()
                        .getFluidState(blockPlaceContext.getClickedPos()).getType() == Fluids.WATER)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST -> onWallShapeEast;
            case SOUTH -> onWallShapeSouth;
            case WEST -> onWallShapeWest;
            default -> onWallShapeNorth;
        };
    }

    @Override
    public @NotNull Item asItem() {
        return this.blockItem;
    }
}
