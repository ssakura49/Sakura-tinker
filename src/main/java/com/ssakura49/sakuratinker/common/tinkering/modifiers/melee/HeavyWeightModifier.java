package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HeavyWeightModifier extends BaseModifier {
    private static final float MIN_KNOCK = 0.5f;
    private static final float MAX_KNOCK = 2.0f;
    private static final int BASE_EFFECT_DURATION = 100; // 5秒 (20 ticks/秒)

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (!attacker.level().isClientSide() && target != null && attacker instanceof Player) {
            float damageRatio = actualDamage / target.getMaxHealth();
            float knockupStrength = Math.min(MAX_KNOCK, Math.max(MIN_KNOCK, damageRatio * 3f));
            Vec3 knockup = new Vec3(0, knockupStrength, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockup));
            int effectDuration = (int)(BASE_EFFECT_DURATION * (1 + damageRatio));
            int effectAmplifier = (int)(damageRatio * 2);
            target.addEffect(new MobEffectInstance(STEffects.GRAVITY.get(), effectDuration, effectAmplifier));
        }
        return actualDamage;
    }
}
