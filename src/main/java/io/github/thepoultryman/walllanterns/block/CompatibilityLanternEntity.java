package io.github.thepoultryman.walllanterns.block;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class CompatibilityLanternEntity extends BlockEntity {
    private BlockState mimickedLanternState;
    private boolean forceLanternUpdate = true;

    public CompatibilityLanternEntity(BlockPos pos, BlockState state) {
        super(WallLanterns.LANTERN_ENTITY_TYPE.get(), pos, state);
    }

    public BlockState getMimickedLanternState() {
        return this.mimickedLanternState;
    }

    public void setMimickedLanternState(BlockState mimickedLanternState) {
        this.mimickedLanternState = mimickedLanternState;
    }

    public boolean shouldForceLanternUpdate() {
        return this.forceLanternUpdate;
    }

    public void forceLanternUpdate(boolean force) {
        this.forceLanternUpdate = force;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        if (this.mimickedLanternState != null) {
            output.store("mimicked_lantern_state", BlockState.CODEC, this.mimickedLanternState);
        }
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.mimickedLanternState = input.read("mimicked_lantern_state", BlockState.CODEC)
                .orElse(Blocks.LANTERN.defaultBlockState());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }
}
