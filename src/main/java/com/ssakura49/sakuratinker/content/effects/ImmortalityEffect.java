package com.ssakura49.sakuratinker.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;

public class ImmortalityEffect extends MobEffect  {
    private static final String SOURCE_KEY = TConstruct.prefix("immortality");
    public ImmortalityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xBC8047);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (player.getHealth() <= 0 && !player.level().isClientSide) {
                player.removeEffect(this);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
