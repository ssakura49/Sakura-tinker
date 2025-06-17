package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

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
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        return PercentageBypassInvulnerableTime.any(source.typeHolder(),target,attacker,0.3f);
    }
}
