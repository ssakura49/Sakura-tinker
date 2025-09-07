package com.ssakura49.sakuratinker.common.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public class GreedyModifier extends CurioModifier {
    public GreedyModifier() {
    }

    @Override
    public void onCurioEquip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack prevStack, ItemStack stack) {
        if (entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, curio.getModifierLevel(this) + 1));
        }
    }
    @Override
    public void onCurioUnequip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack newStack, ItemStack stack) {
        if (entity instanceof Player player) {
            player.removeEffect(MobEffects.DAMAGE_BOOST);
        }
    }
}
