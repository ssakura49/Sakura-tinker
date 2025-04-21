package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.logic.context.ImpactData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WorldBottomModifier extends CurioModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    private static final int BASE_Y_LEVEL = -64;
    private static final int BLOCKS_PER_BONUS = 5;
    private static final float BONUS_PER_LEVEL = 0.01f;

    private float calculateDamageBonus(LivingEntity entity, int level) {
        double yPos = entity.getY();
        if (yPos <= BASE_Y_LEVEL) {
            return 1.0f;
        }
        double blocksAbove = yPos - BASE_Y_LEVEL;
        int bonusTiers = (int)(blocksAbove / BLOCKS_PER_BONUS);
        return 1.0f + (bonusTiers * level * BONUS_PER_LEVEL);
    }

    @Override
    public void onCurioCalculateDamage(IToolStackView curio, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
        float damageBonus = calculateDamageBonus(attacker, level);
        if (attacker instanceof Player player && target != null) {
            target.hurt(LegacyDamageSource.playerAttack(player), damageBonus);
        }
    }

    @Override
    public void onCurioArrowHit(IToolStackView curio, LivingEntity shooter, ImpactData data, int level) {
        float damageBonus = calculateDamageBonus(shooter, level);
        Entity entity = data.getTarget();
        if (entity != null && shooter instanceof Player player) {
            if (entity.isAlive()) {
                entity.hurt(LegacyDamageSource.playerAttack(player), damageBonus);
            }
        }
    }
}
