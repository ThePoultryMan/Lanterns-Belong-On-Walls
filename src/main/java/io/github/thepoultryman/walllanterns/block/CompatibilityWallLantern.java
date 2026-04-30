package io.github.thepoultryman.walllanterns.block;

import io.github.thepoultryman.walllanterns.WallLanternOptions;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;

public class CompatibilityWallLantern extends WallLanternBlock implements EntityBlock {
    public CompatibilityWallLantern(Properties properties, WallLanternOptions options)
    {
        super(properties, Blocks.LANTERN.asItem(), options);
    }

    @Override
    public void setPlacedBy(Level level, @NonNull BlockPos pos, @NonNull BlockState state,
            @Nullable LivingEntity livingEntity, @NonNull ItemStack itemStack)
    {
        level.getBlockEntity(pos, WallLanterns.LANTERN_ENTITY_TYPE.get()).ifPresent(blockEntity -> {
            if (itemStack.getItem() instanceof BlockItem item) {
                blockEntity.setMimickedLanternState(item.getBlock().defaultBlockState());
                blockEntity.forceLanternUpdate(true);
            }
        });
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new CompatibilityLanternEntity(blockPos, blockState);
    }
}
