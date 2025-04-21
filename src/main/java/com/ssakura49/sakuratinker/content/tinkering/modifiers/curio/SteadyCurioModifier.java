package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SteadyCurioModifier extends CurioModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onCurioTakeHeal(IToolStackView curio, LivingHealEvent event, LivingEntity entity, int level) {
        if (entity != null) {
            if (entity instanceof Player player) {
                float bonus = event.getAmount() * 2.0f;
                event.setAmount(bonus);
            }
        }
    }
}
