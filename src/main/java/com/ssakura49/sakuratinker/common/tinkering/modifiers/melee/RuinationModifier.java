package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.CommonRGBUtil;
import com.ssakura49.sakuratinker.utils.component.DynamicComponentUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class RuinationModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.level().isClientSide) {
            float damageFactor = STConfig.Common.RUINATION_DAMAGE_FACTOR.get().floatValue();
            float extraDamage = target.getHealth() * (damageFactor * modifier.getLevel());
            target.hurt(target.damageSources().generic(), extraDamage);
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        float a = STConfig.Common.RUINATION_DAMAGE_FACTOR.get().floatValue();
        int level = modifierEntry.getLevel();
        float bonus = a * level;
        tooltip.add(
                Component.translatable("modifier.sakuratinker.ruination.tooltip")
                        .append(": " + String.format("%.2f%%", bonus * 100))
        );
    }
}
