package com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.helper.ApollyonReflectHelper;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.mixin.Eeeabsmobs.EntityImmortalAccessor;
import com.ssakura49.sakuratinker.mixin.Eeeabsmobs.EntityNamelessGuardianAccessor;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RealityPiercerModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target == null) return;

        int level = modifier.getLevel();
        int maxTicks = Math.min(20, level * 2);

        target.invulnerableTime = Math.max(0, target.invulnerableTime - maxTicks);

        if (target instanceof Apostle apostle) {
            apostle.moddedInvul -= maxTicks;
            if (apostle.moddedInvul < 0) {
                apostle.moddedInvul = 0;
            }
        }

        if (target instanceof Apostle apostle) {
            Object helper = apostle;
            Object apollyon2 = apostle;

            if (ApollyonReflectHelper.isApollyon(helper)) {

                int cd = ApollyonReflectHelper.getHitCooldown(helper);
                if (cd >= 1) {
                    cd -= maxTicks;
                    ApollyonReflectHelper.setHitCooldown(helper, cd <= 0 ? 1 : cd);
                }

                int time = ApollyonReflectHelper.getApollyonTime(helper);
                if (time >= 1) {
                    time -= maxTicks;
                    ApollyonReflectHelper.setApollyonTime(helper, time <= 0 ? 1 : time);
                }

                int cd2 = ApollyonReflectHelper.getAbilityHitCooldown(apollyon2);
                if (cd2 >= 1) {
                    cd2 -= maxTicks;
                    ApollyonReflectHelper.setAbilityHitCooldown(apollyon2, cd2 <= 0 ? 1 : cd2);
                }
            }
        }
        if (SafeClassUtil.EeeabsmobsLoaded) {
            if (target instanceof EntityImmortal entityImmortal) {
                EntityImmortalAccessor accessor = (EntityImmortalAccessor)entityImmortal;
                accessor.setTimeUntilBlock(accessor.timeUntilBlock() - maxTicks);
                if (accessor.timeUntilBlock() < 0) {
                    accessor.setTimeUntilBlock(0);
                }
            } else if (target instanceof EntityNamelessGuardian guardian) {
                EntityNamelessGuardianAccessor accessor = (EntityNamelessGuardianAccessor)guardian;
                accessor.setGuardianInvulnerableTime(accessor.guardianInvulnerableTime() - maxTicks);
                if (accessor.guardianInvulnerableTime() < 0) {
                    accessor.setGuardianInvulnerableTime(0);
                }
            }
        }
    }
}
