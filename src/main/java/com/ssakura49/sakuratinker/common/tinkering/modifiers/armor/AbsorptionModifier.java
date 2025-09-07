package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;

public class AbsorptionModifier extends BaseModifier {

    public static final String ABSORPTION_KEY = SakuraTinker.MODID + ":absorption_amount";

    public AbsorptionModifier(){
    }
    public static int getTotalModifierLevel(LivingEntity entity, Modifier modifier) {
        return ToolUtil.getModifierArmorAllLevel(entity, modifier);
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            tooltip.add(Component.translatable("modifier.sakuratinker.absorption.max")
                    .append(Component.literal(": " + getTotalModifierLevel(player,this) * player.getMaxHealth())));
        }
    }
}
