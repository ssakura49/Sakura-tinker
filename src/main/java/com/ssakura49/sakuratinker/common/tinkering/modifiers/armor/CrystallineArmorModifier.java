package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class CrystallineArmorModifier extends BaseModifier {
    private static final float MAX_DAMAGE_REDUCTION = 0.30f; // 30% maximum reduction
    private static final float MAX_REDUCTION_PER_PIECE = 0.10f; // Each piece can contribute up to 10%

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (tool.isBroken()) {
            return amount;
        }

        int currentDurability = tool.getCurrentDurability();
        int maxDurability = tool.getStats().getInt(ToolStats.DURABILITY);
        if (maxDurability <= 0) {
            return amount;
        }
        LivingEntity entity = context.getEntity();
        float durabilityRatio = (float) currentDurability / maxDurability;

        float pieceReduction = MAX_REDUCTION_PER_PIECE * modifier.getLevel() * durabilityRatio;

        float totalPotentialReduction = ToolUtil.getModifierArmorAllLevel(entity, this) * MAX_REDUCTION_PER_PIECE * durabilityRatio;

        float appliedReduction = Math.min(pieceReduction, MAX_DAMAGE_REDUCTION - (totalPotentialReduction - pieceReduction));

        appliedReduction = Math.max(0, Math.min(appliedReduction, MAX_DAMAGE_REDUCTION));

        return amount * (1.0f - appliedReduction);
    }
}
