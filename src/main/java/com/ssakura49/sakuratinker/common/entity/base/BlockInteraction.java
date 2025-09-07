package com.ssakura49.sakuratinker.common.entity.base;

import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockInteraction {
    boolean apply(ItemStack yoyoStack, Player player, BlockPos pos, BlockState state, Block block, YoyoEntity yoyo);
}
