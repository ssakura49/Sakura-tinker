package com.ssakura49.sakuratinker.library.logic.context;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;

public class ReplaceContext {
    private ItemStack toolStack;
    private ITinkerStationContainer inv;
    private Component errorMsg;

    public ReplaceContext(ItemStack toolStack, ITinkerStationContainer inv){
        this.toolStack = toolStack;
        this.inv = inv;
        this.errorMsg = Component.translatable("recipe.sakuratinker.replace_not_allowed");
    }

    public ItemStack getToolStack() {
        return toolStack;
    }

    public ITinkerStationContainer getInv() {
        return inv;
    }

    public ItemStack getInputStack(int index){
        return inv.getInput(index);
    }

    public Component getErrorMsg() {
        return errorMsg;
    }

    public void setToolStack(ItemStack toolStack) {
        this.toolStack = toolStack;
    }

    public void setInv(ITinkerStationContainer inv) {
        this.inv = inv;
    }

    public void setErrorMsg(Component errorMsg) {
        this.errorMsg = errorMsg;
    }
}
