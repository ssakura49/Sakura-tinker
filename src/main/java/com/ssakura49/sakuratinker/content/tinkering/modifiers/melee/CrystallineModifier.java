package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class CrystallineModifier extends BaseModifier {
    private static final float MAX_DAMAGE_BONUS = 0.2f;

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (tool.isBroken()) return damage;

        int currentDurability = tool.getCurrentDurability();
        int maxDurability = tool.getStats().getInt(ToolStats.DURABILITY);
        if (maxDurability <= 0) {
            return damage;
        }
        float durabilityRatio = (float)currentDurability / maxDurability;
        float damageBonus = MAX_DAMAGE_BONUS * modifier.getLevel() * durabilityRatio;
        return damage * (1.0f + damageBonus);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (tool.isBroken()) {
            tooltip.add(applyStyle(Component.translatable("modifier.sakuratinker.crystalline.broken")));
        } else {
            int currentDurability = tool.getCurrentDurability();
            int maxDurability = tool.getStats().getInt(ToolStats.DURABILITY);

            if (maxDurability > 0) {
                float durabilityRatio = (float)currentDurability / maxDurability;
                float currentBonus = MAX_DAMAGE_BONUS * modifierEntry.getLevel() * durabilityRatio * 100;
                tooltip.add(applyStyle(Component.translatable("modifier.sakuratinker.crystalline.info", String.format("%.1f", currentBonus))));
            }
        }
    }
}
