package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class FusionBurnModifier extends BaseModifier {
    private static final int MAX_AMPLIFIER = 5;
    private static final int BASE_DURATION = 200;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity entity = context.getLivingTarget();
        if (entity != null) {
            applyFusionBurn(context.getAttacker(), entity, modifier.getLevel());
        }
    }

    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (target != null) {
            applyFusionBurn(attacker, target, level);
        }
    }

    private void applyFusionBurn(LivingEntity attacker, LivingEntity target, int modifierLevel) {
        if (target.level().isClientSide()) return;
        MobEffectInstance currentEffect = target.getEffect(STEffects.FusionBurn.get());
        int newAmplifier = 0;
        if (currentEffect != null) {
            newAmplifier = Math.min(currentEffect.getAmplifier() + 1, MAX_AMPLIFIER);
        }
        target.addEffect(new MobEffectInstance(
                STEffects.FusionBurn.get(),
                BASE_DURATION,
                newAmplifier,
                false,
                true,
                true
        ));
    }
}
