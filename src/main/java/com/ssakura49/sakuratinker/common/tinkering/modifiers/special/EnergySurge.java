package com.ssakura49.sakuratinker.common.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class EnergySurge extends BaseModifier {
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey keys, TooltipFlag flag) {
        tooltip.add(Component.literal("传输速度: +" + entry.getLevel() * 5000 + " FE/t"));
    }
}
