package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BodyImpactModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            if (attacker.getRandom().nextFloat() < 0.10f) {
                float armorD = 0;
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                        ItemStack armorStack = attacker.getItemBySlot(slot);
                        if (!armorStack.isEmpty()) {
                            armorD += attacker.getArmorValue();
                        }
                    }
                }
                if (armorD > 0) {
                    Level level = attacker.level();
                    DamageSource damageSource = level.damageSources().mobAttack(attacker);
                    target.hurt(damageSource, armorD);
                }
            }
        }
    }
}
