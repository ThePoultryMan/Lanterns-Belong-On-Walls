package io.github.thepoultryman.walllanterns.mixins;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanternBlock.class)
public interface LanternBlockAccessor {
    //? if > 1.21.1 {
    @Accessor("SHAPE_STANDING")
    //? } else
    //@Accessor("AABB")
    static VoxelShape getStandingShape() {
        throw new AssertionError();
    }
}
