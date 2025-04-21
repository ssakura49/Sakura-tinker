package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

public class ColorizationModifier extends CurioModifier {
    private static final float MAX_DODGE_CHANCE = 0.80f;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onCurioTakeDamagePre(IToolStackView curio, LivingHurtEvent event, LivingEntity entity, DamageSource source, int level) {
        float armorValue = entity.getArmorValue();
        float dodgeChance = Math.min(armorValue * 0.01f, MAX_DODGE_CHANCE);
        Random random = new Random();
        if (random.nextFloat() < dodgeChance) {
            event.setCanceled(true);
        }
    }
}
