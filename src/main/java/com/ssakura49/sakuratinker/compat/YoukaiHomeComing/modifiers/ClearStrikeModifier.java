package com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ClearStrikeModifier extends BaseModifier {
    public static MobEffect getUnconsciousEffect() {
        return BlurredModifier.getUnconsciousEffect();
    }
    public ClearStrikeModifier() {

    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            LivingEntity attacker = context.getAttacker();
            Vec3 attackerLocation = attacker.position();
            if (isLookingBehindTarget(target, attackerLocation)) {
                damage *= 1.5f;
            }
            MobEffect unconsciousEffect = getUnconsciousEffect();
            if (unconsciousEffect != null && attacker.hasEffect(unconsciousEffect)) {
                damage *= 2.0f;
            }
        }
        return damage;
    }
    public static boolean isLookingBehindTarget(LivingEntity target, Vec3 attacker) {
        if (attacker != null) {
            Vec3 lookingVector = target.getViewVector(1.0F);
            Vec3 attackAngleVector = attacker.subtract(target.position()).normalize();
            attackAngleVector = new Vec3(attackAngleVector.x, 0.0D, attackAngleVector.z);
            return attackAngleVector.dot(lookingVector) < -0.5D;
        }
        return false;
    }
}
