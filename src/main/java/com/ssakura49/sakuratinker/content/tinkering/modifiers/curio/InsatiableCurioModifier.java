package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.logic.context.ImpactData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class InsatiableCurioModifier extends CurioModifier {

    public InsatiableCurioModifier(){}

    private static final int MAX_COMBO = 8;
    private static final float DAMAGE_PER_LEVEL = 2.0f;
    private static final ResourceLocation COMBO_KEY = new ResourceLocation(SakuraTinker.MODID, "combo_count");

    @Override
    public void onCurioShootArrow(IToolStackView curio, LivingEntity shooter, AbstractArrow arrow, ModDataNBT persistentData, int level) {
        persistentData.putInt(COMBO_KEY, getComboCount(shooter));
    }

    @Override
    public void onCurioArrowHit(IToolStackView curio, LivingEntity shooter, ImpactData data, int level) {
        incrementCombo(shooter, level);
    }

    @Override
    public void onCurioToDamagePre(IToolStackView curio, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, int level) {
        float bonus = getDamageBonus(attacker, level);
        if (bonus > 0) {
            event.setAmount(event.getAmount() + bonus);
        }
    }

    @Override
    public void onCurioToDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (event.getAmount() > 0) {
            incrementCombo(attacker, level);
        }
    }

    @Override
    public void onCurioToKillTarget(IToolStackView curio, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
        resetCombo(attacker);
    }

    private int getComboCount(LivingEntity entity) {
        return entity.getPersistentData().getInt(COMBO_KEY + "_" + getId());
    }

    private void setComboCount(LivingEntity entity, int count) {
        entity.getPersistentData().putInt(COMBO_KEY + "_" + getId(), count);
    }

    private void incrementCombo(LivingEntity entity, int level) {
        int current = getComboCount(entity);
        if (current < MAX_COMBO * level) {
            setComboCount(entity, current + 1);
        }
    }

    private void resetCombo(LivingEntity entity) {
        setComboCount(entity, 0);
    }

    private float getDamageBonus(LivingEntity entity, int level) {
        int combo = getComboCount(entity);
        if (combo >= MAX_COMBO) {
            // Calculate bonus: +2 damage per level for every full combo (8 hits)
            int fullCombos = combo / MAX_COMBO;
            return fullCombos * DAMAGE_PER_LEVEL * level;
        }
        return 0;
    }

    // For ranged attacks, we need to apply the bonus before velocity calculation
    @Override
    public void onCurioCalculateDamage(IToolStackView curio, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
        float bonus = getDamageBonus(attacker, level);
        if (bonus < 0) return;
        if (event instanceof LivingCalculateAbsEvent.Armor armor) {
            float damage = armor.getDamageAfterArmor() + bonus;
            armor.setDamageAfterArmor(damage);
        }
    }
}
