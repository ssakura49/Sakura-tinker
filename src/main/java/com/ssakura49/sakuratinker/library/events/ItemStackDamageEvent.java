package com.ssakura49.sakuratinker.library.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ItemStackDamageEvent extends Event {
    private final LivingEntity entity;
    private final ItemStack stack;
    private int amount;

    public ItemStackDamageEvent(LivingEntity entity, ItemStack stack, int amount) {
        this.entity = entity;
        this.stack = stack;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}