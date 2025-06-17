package com.ssakura49.sakuratinker.compat.Goety.modifiers;

import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class SoulIntakeModifier extends BaseModifier {
    private static final int SOUL_COST_PER_DURABILITY = 5;
    private static final int CHECK_INTERVAL = 20;

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide() && holder instanceof Player player && holder.tickCount % CHECK_INTERVAL == 0) {
            if (tool.getDamage() > 0) {
                ISoulEnergy soulEnergy = SEHelper.getCapability(player);
                if (soulEnergy != null && soulEnergy.getSoulEnergy() > 0) {
                    int damageToRepair = Math.min(tool.getDamage(), soulEnergy.getSoulEnergy() / SOUL_COST_PER_DURABILITY);
                    if (damageToRepair > 0) {
                        tool.setDamage(tool.getDamage() - damageToRepair);

                        soulEnergy.decreaseSE(damageToRepair * SOUL_COST_PER_DURABILITY);
                        SEHelper.sendSEUpdatePacket(player);

                        if (level instanceof ServerLevel serverLevel) {
                            ServerParticleUtil.addParticlesAroundMiddleSelf(serverLevel, ParticleTypes.SOUL_FIRE_FLAME, player);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey key, TooltipFlag flag) {
        tooltip.add(Component.translatable("modifier.goety.soul_siphon.tooltip", SOUL_COST_PER_DURABILITY));
    }
}
