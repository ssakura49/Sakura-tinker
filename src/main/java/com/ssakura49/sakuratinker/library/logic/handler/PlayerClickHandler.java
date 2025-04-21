package com.ssakura49.sakuratinker.library.logic.handler;

import com.ssakura49.sakuratinker.library.hooks.click.LeftClickModifierHook;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import slimeknights.tconstruct.library.tools.item.IModifiable;

public class PlayerClickHandler {
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        Player player = event.getEntity();
        if (player!=null&&player.level().isClientSide) {
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClick(stack,player,slot);
            }
        }
    }
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        if (player!=null) {
            BlockState state = player.level().getBlockState(pos);
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (stack.getItem() instanceof IModifiable) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                LeftClickModifierHook.handleLeftClickBlock(stack,player,slot,state,pos);
            }
        }
    }
}
