package io.github.thepoultryman.walllanterns;

import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.util.RegistryUtils;

public interface WallLanternsEntrypoint {
    void patchLanterns();

    /**
     * Adds the facing property to one or more lanterns, allowing the lanterns
     * to be placed on walls.
     * Block states are reordered after all entrypoints have been called.
     * @param ids The identifiers of the lanterns to be patched.
     */
    default void addLantern(Identifier... ids) {
        for (Identifier id : ids) {
            RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(
                    lantern -> {
                        StateRefresher.INSTANCE.addBlockProperty(lantern, Properties.FACING, Direction.UP);
                        WallLanterns.WALLABLE_LANTERNS.add(id.toString());
                    }
            );
        }
    }
}
