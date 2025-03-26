package io.github.thepoultryman.walllanterns.mixins;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanternBlock.class)
public interface LanternBlockAccessor {
    @Accessor("HANGING_AABB")
    static VoxelShape getHangingShape() {
        throw new AssertionError();
    }
}
