package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;

public class GaiaGuardianModifier extends BaseModifier {
    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,
                                    DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (!(entity instanceof Player player)) {
            return amount;
        }
        int level = ToolUtil.getHeadModifierLevel(entity, this)
                + ToolUtil.getChestModifierLevel(entity, this)
                + ToolUtil.getLegsModifierLevel(entity, this)
                + ToolUtil.getFeetModifierLevel(entity, this);

        if (level <= 0) {
            return amount;
        }
        float maxReduce = 50f + level * 50f;
        float absorb = Math.min(amount, maxReduce);
        int manaCost = (int)(absorb * 100.0F);
        if (ManaItemHandler.instance().requestManaExactForTool(player.getItemBySlot(slotType), player, manaCost, true)) {
            return amount - absorb;
        }
        return amount;
    }
}
