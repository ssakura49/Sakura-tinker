package com.ssakura49.sakuratinker.content.tools.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.logic.context.AttributeData;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import com.ssakura49.sakuratinker.library.tools.STHooks;
import com.ssakura49.sakuratinker.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class TinkerCharm extends ModifiableItem implements ICurioItem {
    public TinkerCharm(Item.Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return ImmutableMultimap.of();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ImmutableMultimap.of();
    }

    private void getCurioStatAttribute(Multimap<Attribute, AttributeModifier> modifiers, ToolStack tool, UUID uuid) {
        StatsNBT toolStats = tool.getStats();
        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "charms_movement_speed_bonus", (double) (Float) toolStats.get(STToolStats.MOVEMENT_SPEED), Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "charms_max_health_bonus", (double) (float) toolStats.get(STToolStats.MAX_HEALTH), Operation.ADDITION));
        modifiers.put(Attributes.ARMOR, new AttributeModifier(uuid, "charms_armor_bonus", (double) (Float) toolStats.get(STToolStats.ARMOR), Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "charms_armor_toughness_bonus", (double) (Float) toolStats.get(STToolStats.ARMOR_TOUGHNESS), Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "charms_attack_damage_bonus", (double) (Float) toolStats.get(STToolStats.ATTACK_DAMAGE), Operation.MULTIPLY_BASE));
    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).onCurioTick(tool, slotContext, slotContext.entity(), entry.getLevel(), stack);
        }

    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        this.getCurioStatAttribute(modifiers, tool, uuid);

        for(ModifierEntry entry : tool.getModifierList()) {
            ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).addCurioAttribute(tool, slotContext, slotContext.entity(), entry.getLevel(), new AttributeData(stack, uuid, modifiers));
        }

        return modifiers;
    }

    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).onCurioEquip(tool, slotContext, slotContext.entity(), entry.getLevel(), prevStack, stack);
        }

    }

    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).onCurioUnequip(tool, slotContext, slotContext.entity(), entry.getLevel(), newStack, stack);
        }

    }

    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        int fortune = ICurioItem.super.getFortuneLevel(slotContext, lootContext, stack);
        ToolStack toolStack = ToolStack.from(stack);

        for(ModifierEntry entry : toolStack.getModifierList()) {
            fortune = ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).onCurioGetFortune(toolStack, slotContext, lootContext, stack, fortune, entry.getLevel());
        }

        return fortune;
    }

    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int looting = ICurioItem.super.getLootingLevel(slotContext, source, target, baseLooting, stack);
        ToolStack toolStack = ToolStack.from(stack);

        for(ModifierEntry entry : toolStack.getModifierList()) {
            looting = ((CurioBuilderHook)entry.getHook(STHooks.CURIO_BUILDER)).onCurioGetLooting(toolStack, slotContext, source, target, stack, looting, entry.getLevel());
        }

        return looting;
    }

    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), stack.getItem()).isEmpty();
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        return this.getCurioStats(tool, player, tooltips, key, tooltipFlag);
    }

    public List<Component> getCurioStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        TooltipUtils.addPerToolStatTooltip(builder, tool, STToolStats.MOVEMENT_SPEED);
        TooltipUtils.addToolStatTooltip(builder, tool, STToolStats.MAX_HEALTH);
        TooltipUtils.addPerToolStatTooltip(builder, tool, STToolStats.ARMOR);
        TooltipUtils.addPerToolStatTooltip(builder, tool, STToolStats.ARMOR_TOUGHNESS);
        TooltipUtils.addPerToolStatTooltip(builder, tool, STToolStats.ATTACK_DAMAGE);
        TooltipUtils.addPerToolStatTooltip(builder, tool, STToolStats.ARROW_DAMAGE);
        builder.addAllFreeSlots();

        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }

        return tooltips;
    }
}
