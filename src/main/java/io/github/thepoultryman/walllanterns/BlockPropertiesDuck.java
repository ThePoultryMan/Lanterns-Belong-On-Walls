package io.github.thepoultryman.walllanterns;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

public interface BlockPropertiesDuck {
    BlockBehaviour.Properties walllanterns$overrideLootTable(ResourceKey<LootTable> lootTable);
}
