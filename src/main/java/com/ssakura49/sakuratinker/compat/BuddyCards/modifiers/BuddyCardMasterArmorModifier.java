package com.ssakura49.sakuratinker.compat.BuddyCards.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.UUID;

public class BuddyCardMasterArmorModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (!(context.getEntity() instanceof Player player)) return amount;

        BuddycardCollectionSaveData data = BuddycardCollectionSaveData.get((ServerLevel) player.level());
        UUID uuid = player.getUUID();

        float reduction = 0f;
        for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
            BuddycardCollectionSaveData.Fraction f = data.checkPlayerSetCompletion(uuid, set);
            reduction += f.top * 0.5f;
        }

        return Math.max(0, amount - reduction);
    }

    @Override
    public int getPriority() {
        return 75;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player,
                           List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null && player.level() instanceof ServerLevel serverLevel) {
            BuddycardCollectionSaveData data = BuddycardCollectionSaveData.get(serverLevel);
            UUID uuid = player.getUUID();

            int totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                totalCards += data.checkPlayerSetCompletion(uuid, set).top;
            }

            float reduction = totalCards * 0.5f;
            tooltip.add(Component.translatable("modifier.sakuratinker.card_collector.tooltip",
                            totalCards, reduction)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("modifier.sakuratinker.card_collector.tooltip.none")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
