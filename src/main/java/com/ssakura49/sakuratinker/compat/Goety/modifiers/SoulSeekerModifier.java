package com.ssakura49.sakuratinker.compat.Goety.modifiers;

import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.Polarice3.Goety.utils.SEHelper;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class SoulSeekerModifier extends BaseModifier {
    private static final float COST_REDUCTION_PER_LEVEL = 0.04f;
    private static final int CHECK_INTERVAL = 5;

    private static final String LAST_SOUL_KEY = "last_soul_energy";
    private static final ResourceLocation SOUL = new ResourceLocation(SakuraTinker.MODID, LAST_SOUL_KEY);

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!world.isClientSide && holder instanceof Player player &&
                holder.tickCount % CHECK_INTERVAL == 0 && isSelected) {
            ISoulEnergy soulEnergy = SEHelper.getCapability(player);
            if (soulEnergy != null) {
                ModDataNBT persistentData = tool.getPersistentData();
                int lastSoul = persistentData.getInt(SOUL);
                int currentSoul = soulEnergy.getSoulEnergy();

                if (currentSoul < lastSoul) {
                    int consumed = lastSoul - currentSoul;

                    if (isFromMagicSource(player)) {
                        int level = modifier.getLevel();
                        int refund = (int)(consumed * COST_REDUCTION_PER_LEVEL * level);

                        if (refund > 0) {
                            soulEnergy.increaseSE(refund);
                            SEHelper.sendSEUpdatePacket(player);
                        }
                    }
                }
                persistentData.putInt(SOUL, currentSoul);
            }
        }
    }

    private boolean isFromMagicSource(Player player) {
        return player.getMainHandItem().getItem() instanceof DarkWand || player.getOffhandItem().getItem() instanceof DarkWand;
    }
}
