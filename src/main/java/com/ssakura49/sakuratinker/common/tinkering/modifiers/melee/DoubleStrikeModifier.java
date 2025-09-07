package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class DoubleStrikeModifier extends BaseModifier {

    public DoubleStrikeModifier() {
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target.isAlive() && attacker.getRandom().nextFloat() < 0.20f) {
            LegacyDamageSource damageSource;
            if (attacker instanceof Player) {
                damageSource = LegacyDamageSource.playerAttack((Player)attacker);
            } else {
                damageSource = LegacyDamageSource.mobAttack(attacker);
            }

            ToolAttackUtil.attackEntitySecondary(
                    damageSource,
                    damageDealt,
                    target,
                    attacker,
                    false
            );
            attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, attacker.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
