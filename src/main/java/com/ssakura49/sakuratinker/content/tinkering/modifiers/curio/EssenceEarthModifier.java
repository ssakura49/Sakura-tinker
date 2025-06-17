package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EssenceEarthModifier extends CurioModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onDamageTargetPre(IToolStackView curio, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (target != null) {
            if (!(attacker instanceof Player)) {
                return;
            }
            float playerMaxHealth = attacker.getMaxHealth();
            float targetMaxHealth = target.getMaxHealth();
            float healthRatio = targetMaxHealth / playerMaxHealth;
            healthRatio = Math.max(1.0f, Math.min(healthRatio, 5.0f));
            float damage = event.getAmount();
            float newDamage = damage * healthRatio;
            event.setAmount(newDamage);
        }
    }
}
