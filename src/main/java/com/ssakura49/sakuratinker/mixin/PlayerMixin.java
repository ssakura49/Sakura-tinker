package com.ssakura49.sakuratinker.mixin;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerMixin {
    @Accessor("abilities")
    Abilities getAbilities();
}
