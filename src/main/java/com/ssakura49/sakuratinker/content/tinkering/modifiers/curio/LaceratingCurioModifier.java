package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class LaceratingCurioModifier extends CurioModifier {
    public LaceratingCurioModifier(){}

    @Override
    public void onCurioToDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (attacker instanceof Player player) {
            if (EntityUtil.isFullChance(player) && RANDOM.nextBoolean()) {
                applyEffect(target, level);
            }
        }
    }

    private static void applyEffect(LivingEntity target, int level) {
        TinkerEffects.bleeding.get().apply(target, 1 + 20 * (2 + (RANDOM.nextInt(level + 3))), level - 1, true);
    }
}
