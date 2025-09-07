package com.ssakura49.sakuratinker.common.tinkering.modifiers.mining;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BreakSpeedModifier extends BaseModifier {
    @Override
    public void modifierAfterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        LivingEntity entity = context.getLiving();
        Level level = context.getWorld();
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, modifier.getLevel()-1,true,true));
        }
    }
}
