package com.ssakura49.sakuratinker.compat.Goety.modifiers;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.ssakura49.sakuratinker.generic.BaseModifier;
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

public class SoulErosionModifier extends BaseModifier {

    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (target != null && level > 0) {
            target.addEffect(new MobEffectInstance(GoetyEffects.SAPPED.get(), 100 * level, level - 1));
        }
    }

    @Override
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (target != null && level > 0) {
            target.addEffect(new MobEffectInstance(GoetyEffects.SAPPED.get(), 100 * level, level - 1));
        }
    }
}
