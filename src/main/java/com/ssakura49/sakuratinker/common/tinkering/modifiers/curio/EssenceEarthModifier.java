package com.ssakura49.sakuratinker.common.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EssenceEarthModifier extends CurioModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
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
