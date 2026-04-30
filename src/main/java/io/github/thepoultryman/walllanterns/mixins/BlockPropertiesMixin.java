package io.github.thepoultryman.walllanterns.mixins;

import io.github.thepoultryman.walllanterns.BlockPropertiesDuck;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBehaviour.Properties.class)
public class BlockPropertiesMixin extends Object implements BlockPropertiesDuck {
    @Shadow
    ResourceKey<LootTable> drops;

    @Override
    public BlockBehaviour.Properties walllanterns$overrideLootTable(ResourceKey<LootTable> lootTable) {
        this.drops = lootTable;
        return (BlockBehaviour.Properties) ((Object) this);
    }
}
