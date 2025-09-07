package com.ssakura49.sakuratinker.common.entity.base;

import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface EntityInteraction {
    boolean apply(ItemStack yoyoStack, Player player, InteractionHand hand, YoyoEntity yoyo, Entity target);
}

