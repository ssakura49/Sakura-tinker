package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.library.damagesource.PercentageBypassInvulnerableTime;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RelentlessModifier extends BaseModifier {
    private static final float PERCENT_PER_LEVEL = 0.1f;

    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        int level = entry.getLevel();
        if (level > 0 && target instanceof LivingEntity living) {
            float bypassChance = PERCENT_PER_LEVEL * Math.min(level, 4);
            return PercentageBypassInvulnerableTime.any(source.typeHolder(), living, attacker, bypassChance);
        }
        return source;
    }
}
