package com.ssakura49.sakuratinker.library.logic.context;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record AttributeData(ItemStack stack, UUID uuid, Multimap<Attribute, AttributeModifier> map) {
    public AttributeData(ItemStack stack, UUID uuid, Multimap<Attribute, AttributeModifier> map) {
        this.stack = stack;
        this.uuid = uuid;
        this.map = map;
    }

    public String getId(Attribute attr) {
        String var10000 = this.stack.getDescriptionId();
        return var10000 + attr.getDescriptionId();
    }

    public void addAttr(Attribute attribute, double value) {
        this.map.put(attribute, new AttributeModifier(this.uuid, this.getId(attribute), value, Operation.ADDITION));
    }

    public void mulBaseAttr(Attribute attribute, double value) {
        this.map.put(attribute, new AttributeModifier(this.uuid, this.getId(attribute), value, Operation.MULTIPLY_BASE));
    }

    public void mulTotalAttr(Attribute attribute, double value) {
        this.map.put(attribute, new AttributeModifier(this.uuid, this.getId(attribute), value, Operation.MULTIPLY_TOTAL));
    }

    public ItemStack stack() {
        return this.stack;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public Multimap<Attribute, AttributeModifier> map() {
        return this.map;
    }
}
