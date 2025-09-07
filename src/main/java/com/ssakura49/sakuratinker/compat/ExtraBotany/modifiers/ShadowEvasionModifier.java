package com.ssakura49.sakuratinker.compat.ExtraBotany.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ShadowEvasionModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                    EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        int level = modifier.getLevel();
        if (entity.getRandom().nextFloat() < 0.3f) {
            int durabilityDamage = Math.max(1, Mth.ceil(amount / level));
            ToolDamageUtil.damageAnimated(tool, durabilityDamage, entity, slotType);
            return 0f;
        }
        return amount;
    }

}
