package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class CrystallineArmorModifier extends BaseModifier {
    private static final float MAX_DAMAGE_REDUCTION = 0.30f;

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
        float durabilityRatio = (float)currentDurability / maxDurability;
        float damageReduction = MAX_DAMAGE_REDUCTION * modifier.getLevel() * durabilityRatio;
        return amount * (1.0f - damageReduction);
    }
}
