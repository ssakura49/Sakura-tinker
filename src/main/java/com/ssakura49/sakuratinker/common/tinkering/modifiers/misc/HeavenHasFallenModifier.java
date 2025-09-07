package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class HeavenHasFallenModifier extends BaseModifier {
    public static final ResourceLocation HEAVEN_FALLEN = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "heaven_fallen");

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float actualDamage) {
        if (!context.isExtraAttack() && !tool.getPersistentData().getBoolean(HEAVEN_FALLEN)) {
            LivingEntity attacker = context.getAttacker();
            LivingEntity target = context.getLivingTarget();
            if (target != null) {
                for (ModifierEntry toolEntry : tool.getModifierList()) {
                    var hook = toolEntry.getHook(ModifierHooks.MELEE_HIT);
                    hook.beforeMeleeHit(tool, modifierEntry, context, actualDamage, 0, 0);
                }
                for (ModifierEntry toolEntry : tool.getModifierList()) {
                    var hook = toolEntry.getHook(ModifierHooks.MELEE_HIT);
                    hook.afterMeleeHit(tool, modifierEntry, context, actualDamage);
                }
                if(target.getHealth() <= 0){
                    return 0;
                }
                float currentHealth = target.getHealth() - actualDamage;
                if (target.level() instanceof ServerLevel serverLevel) {
                    target.setHealth(currentHealth);
                    if(currentHealth <= 0f){
//                        serverLevel.broadcastEntityEvent(target, (byte) 3);
                        target.setPos(Double.NaN, Double.NaN, Double.NaN);

                        LegacyDamageSource source = new LegacyDamageSource(attacker).setBypassArmor().setBypassInvulnerability();
//                        target.die(source);
//                        target.dropAllDeathLoot(source);
                        EntityUtil.die(target, source);
                    }
                }
            }
            return 0;
        }
        return actualDamage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        for (ModifierEntry toolEntry : modifiers.getModifiers()) {
            if (!toolEntry.matches(this) && !persistentData.getBoolean(HEAVEN_FALLEN)) {
                toolEntry.getHook(ModifierHooks.PROJECTILE_HIT).onProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target);
            }
        }
        return false;
    }
}
