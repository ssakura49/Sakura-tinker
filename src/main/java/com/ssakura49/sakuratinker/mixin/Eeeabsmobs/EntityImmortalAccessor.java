package com.ssakura49.sakuratinker.mixin.Eeeabsmobs;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({EntityImmortal.class})
public interface EntityImmortalAccessor {
    @Accessor(
            value = "timeUntilBlock",
            remap = false
    )
    void setTimeUntilBlock(int var1);

    @Accessor(
            value = "timeUntilBlock",
            remap = false
    )
    int timeUntilBlock();
}
