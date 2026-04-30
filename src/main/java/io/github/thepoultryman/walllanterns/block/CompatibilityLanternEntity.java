package io.github.thepoultryman.walllanterns.block;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
//? if > 1.21.1 {
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//? } else {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
*///? }

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

    //? if > 1.21.1 {
    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        if (this.mimickedLanternState != null) {
            output.store("mimicked_lantern_state", BlockState.CODEC, this.mimickedLanternState);
        }
        super.saveAdditional(output);
    }
    //? } else {
    /*@Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        if (this.mimickedLanternState != null) {
            compoundTag.put("mimicked_lantern_state", NbtUtils.writeBlockState(this.mimickedLanternState));
        }
        super.saveAdditional(compoundTag, provider);
    }
    *///? }

    //? if > 1.21.1 {
    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.mimickedLanternState = input.read("mimicked_lantern_state", BlockState.CODEC)
                .orElse(Blocks.LANTERN.defaultBlockState());
    }
    //? } else {
    /*@Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.mimickedLanternState = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), compoundTag);
    }
    *///? }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }
}
