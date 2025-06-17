package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class MucilaginousShellModifier extends BaseModifier {
    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (context.getEntity() instanceof Player player && amount > 0) {
            float totalShield = getAllShield(player);
            if (totalShield > 0) {
                float damageAbsorbed = Math.min(amount, totalShield);
                ModifierEntry entry = new ModifierEntry(TinkerModifiers.overslime.get(), 1);
                consumeShield(player, entry, damageAbsorbed);
                return amount - damageAbsorbed;
            }
        }
        return amount;
    }

    private static float getAllShield(Player player) {
        OverslimeModifier overSlime = TinkerModifiers.overslime.get();
        float total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof IModifiable) {
                    IToolStackView tool = ToolStack.from(stack);
                    total += overSlime.getShield(tool);
                }
            }
        }
        return total;
    }

    private static void consumeShield(Player player, ModifierEntry entry, float amount) {
        OverslimeModifier overSlime = TinkerModifiers.overslime.get();
        float totalShield = getAllShield(player);
        if (totalShield <= 0) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof IModifiable) {
                    IToolStackView tool = ToolStack.from(stack);
                    float shield = overSlime.getShield(tool);
                    if (shield > 0) {
                        int consumeAmount = -(int)Math.ceil(amount * (shield / totalShield));
                        overSlime.addOverslime(tool, entry, consumeAmount);
                    }
                }
            }
        }
    }
}
