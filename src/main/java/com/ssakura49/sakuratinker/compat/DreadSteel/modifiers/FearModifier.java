package com.ssakura49.sakuratinker.compat.DreadSteel.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

public class FearModifier  extends BaseModifier {
    private static final float FEAR_CHANCE = 0.5f;
    private static final int FEAR_DURATION = 100;
    private static final Random RANDOM = new Random();
    public FearModifier(){
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerHeal);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierTakeDamagePre(IToolStackView armor, LivingHurtEvent event, AttackData data, int level) {
        LivingEntity wearer = data.entity();
        LivingEntity attacker = data.getAttacker();

        if (attacker != null && RANDOM.nextFloat() < FEAR_CHANCE) {
            attacker.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    FEAR_DURATION,
                    3,
                    false,
                    true,
                    true
            ));
        }

        if (event.getSource().is(DamageTypes.MAGIC) && event.getSource().getMsgId().equals("wither")) {
            event.setAmount(0);
        }
    }

    public void onPlayerHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            float amount = event.getAmount();
            event.setAmount(amount * 2);
        }
    }
}
