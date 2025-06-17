package com.ssakura49.sakuratinker.compat.Goety.modifiers;

import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class DevourSoulModifier extends BaseModifier {
    private static final int SOUL_PER_KILL = 10;

    @Override
    public void onKillLivingTarget(IToolStackView tool, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
        if (attacker instanceof Player player && !player.level().isClientSide()) {
            ISoulEnergy soulEnergy = SEHelper.getCapability(player);
            if (soulEnergy != null) {
                int soulsToAdd = SOUL_PER_KILL * level;
                soulEnergy.increaseSE(soulsToAdd);
                SEHelper.sendSEUpdatePacket(player);
                if (player.level() instanceof ServerLevel serverLevel) {
                    ServerParticleUtil.addParticlesAroundMiddleSelf(serverLevel, ParticleTypes.SOUL, player);
                }
            }
        }
    }
}
