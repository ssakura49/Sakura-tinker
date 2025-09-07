package com.ssakura49.sakuratinker.common.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.common.recipes.SoulSakuraSealRecipe;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.component.DynamicComponentUtil;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public class SealTooltipModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey keys, TooltipFlag flag) {
        ModDataNBT persistentData = tool.getPersistentData();
        if (persistentData.contains(SoulSakuraSealRecipe.SEAL_TOOLTIP_KEY, Tag.TAG_ANY_NUMERIC) &&
                persistentData.getBoolean(SoulSakuraSealRecipe.SEAL_TOOLTIP_KEY)) {

            ListTag modifiersTag = persistentData.get(
                    SoulSakuraSealRecipe.SEAL_MODIFIER_LIST,
                    (compound, key) -> compound.getList(key, Tag.TAG_STRING)
            );

            if (!modifiersTag.isEmpty()) {
                StringBuilder modifiersBuilder = new StringBuilder();
                for (int i = 0; i < modifiersTag.size(); i++) {
                    if (i > 0) modifiersBuilder.append(", ");
                    modifiersBuilder.append(modifiersTag.getString(i));
                }

                int[] colors = new int[]{
                        0xFF5555,
                        0xFFAA00,
                        0x55FF55,
                        0x5555FF
                };

                Component colorfulText = DynamicComponentUtil.ScrollColorfulText.getColorfulText(
                        "已刻印: ",
                        modifiersBuilder.toString(),
                        colors,
                        60,
                        50,
                        false
                );

                tooltip.add(colorfulText);
            }
        }
    }
}
