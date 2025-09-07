package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RootednessModifier extends BaseModifier {
    private static final float DAMAGE_INCREASE_PER_LEVEL = 0.06F;
    private static final float INVULNERABILITY_PER_LEVEL = 0.15F;
    private static final float MAX_DAMAGE_INCREASE = 1.0F;

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                   EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (!(entity instanceof Player player) || entity.level().isClientSide()) {
            return amount;
        }

        int totalLevel = ToolUtil.getModifierArmorAllLevel(player, this);
        if (totalLevel <= 0) {
            return amount;
        }

        float iDamage = Math.min(DAMAGE_INCREASE_PER_LEVEL * totalLevel, MAX_DAMAGE_INCREASE);

        float increasedDamage = amount * (1 + iDamage);

        int maxLevel = Math.min(totalLevel, 8);

        player.invulnerableTime = Math.max(
                player.invulnerableTime,
                (int) (INVULNERABILITY_PER_LEVEL * maxLevel * 10)
        );

        return increasedDamage;
    }
}
