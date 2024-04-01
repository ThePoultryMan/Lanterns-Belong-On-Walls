package io.github.thepoultryman.walllanterns.mixin;

import io.github.thepoultryman.walllanterns.WallLanterns;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.structure.StructureTemplateManager.class)
public class StructureTemplateManager {
    @Inject(at = @At("HEAD"), method = "createStructureFromNbt")
    public void walllanterns$createStructureFromNbt(NbtCompound nbt, CallbackInfoReturnable<Structure> cir) {
        if (nbt.contains("palette", NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbt.getList("palette", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); i++) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                NbtElement nameElement = nbtCompound.get("Name");
                if (nameElement == null || !WallLanterns.WALLABLE_LANTERNS.contains(nameElement.asString())) continue;
                WallLanterns.LOGGER.debug("Patching structure data...");
                NbtCompound properties = nbtCompound.getCompound("Properties");
                if (properties.contains("facing")) {
                    WallLanterns.LOGGER.debug("Skipping data (" + i + "), already contains facing property.");
                    continue;
                }
                boolean hanging = properties.getString("hanging").equals("true");
                if (hanging) {
                    properties.entries.put("facing", NbtString.of("down"));
                } else {
                    properties.entries.put("facing", NbtString.of("up"));
                }
                nbtCompound.entries.put("Properties", properties);
                nbtList.set(i, nbtCompound);
            }
        }
    }
}
