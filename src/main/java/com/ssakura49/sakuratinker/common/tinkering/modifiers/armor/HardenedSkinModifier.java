package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
public class HardenedSkinModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (context.getEntity() instanceof Player) {
            if (source.getDirectEntity() instanceof LivingEntity attacker) {
                if (attacker.getMainHandItem().isEmpty()) {
                    return amount * 0.5f;
                }
            }
        }
        return amount;
    }
}
