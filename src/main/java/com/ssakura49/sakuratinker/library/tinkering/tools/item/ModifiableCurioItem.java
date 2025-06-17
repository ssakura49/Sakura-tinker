package com.ssakura49.sakuratinker.library.tinkering.tools.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class ModifiableCurioItem extends ModifiableItem implements IModifiableDisplay, ICurioItem {
    public ModifiableCurioItem(Item.Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        for (ModifierEntry entry : tool.getModifierList()) {
            CurioBuilderHook hook = (CurioBuilderHook) entry.getHook(STHooks.CURIO_BUILDER);
            hook.onCurioTick(tool, slotContext, slotContext.entity(), entry.getLevel(), stack);
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        for (ModifierEntry entry : tool.getModifierList()) {
            CurioBuilderHook hook = (CurioBuilderHook) entry.getHook(STHooks.CURIO_BUILDER);
            hook.onCurioEquip(tool, slotContext, slotContext.entity(), entry.getLevel(), prevStack, stack);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        for (ModifierEntry entry : tool.getModifierList()) {
            CurioBuilderHook hook = (CurioBuilderHook) entry.getHook(STHooks.CURIO_BUILDER);
            hook.onCurioUnequip(tool, slotContext, slotContext.entity(), entry.getLevel(), newStack, stack);
        }
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        int fortune = ICurioItem.super.getFortuneLevel(slotContext, lootContext, stack);
        ToolStack tool = ToolStack.from(stack);
        for (ModifierEntry entry : tool.getModifierList()) {
            fortune = ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER))
                    .onCurioGetFortune(tool, slotContext, lootContext, stack, fortune, entry.getLevel());
        }
        return fortune;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int looting = ICurioItem.super.getLootingLevel(slotContext, source, target, baseLooting, stack);
        ToolStack tool = ToolStack.from(stack);
        for (ModifierEntry entry : tool.getModifierList()) {
            looting = ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER))
                    .onCurioGetLooting(tool, slotContext, source, target, stack, looting, entry.getLevel());
        }
        return looting;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return ICurioItem.super.canEquip(slotContext,stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ImmutableMultimap.of();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return ImmutableMultimap.of();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext context, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        ToolStack tool = ToolStack.from(stack);
        this.getCurioStatAttributes(map, tool, uuid);
        for (ModifierEntry entry : tool.getModifierList()) {
            CurioBuilderHook hook = (CurioBuilderHook) entry.getHook(STHooks.CURIO_BUILDER);
            hook.modifyCurioAttribute(tool, context, uuid, entry.getLevel(), map::put);
        }
        return map;
    }

    /**
     * Override this in subclasses to define default curio attribute bonuses
     */
    protected void getCurioStatAttributes(Multimap<Attribute, AttributeModifier> modifiers, ToolStack tool, UUID uuid) {
    }
}
