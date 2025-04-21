package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HeavyModifier extends BaseModifier {
    private static final float ATTACK_BONUS = 0.10f;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Integer.MAX_VALUE, 0, false, true, false));
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player) {
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        float armorValue = attacker.getArmorValue();
        damage = baseDamage + armorValue * ATTACK_BONUS;
        return damage;
    }
}
