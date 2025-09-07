package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class NeutronCondenseModifier extends BaseModifier {
    private static final ResourceLocation USAGE_COUNT = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "usage_count");
    private static final int MAX_GROWTH = 50;
    private static final float DAMAGE_PER_USE = 0.5f;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int usageCount = context.getPersistentData().getInt(USAGE_COUNT);
        if (usageCount > 0) {
            float bonus = Math.min(usageCount, MAX_GROWTH) * DAMAGE_PER_USE;
            ToolStats.ATTACK_DAMAGE.add(builder, bonus);
        }
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (holder instanceof Player && holder.getRandom().nextFloat() < 0.5f) {
            return 0;
        }
        return amount;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey key, TooltipFlag flag) {
        int usageCount = tool.getPersistentData().getInt(USAGE_COUNT);
        float bonusDamage = Math.min(usageCount, MAX_GROWTH) * DAMAGE_PER_USE;

        tooltip.add(Component.literal("已凝聚: " + usageCount + "/" + MAX_GROWTH));
        tooltip.add(Component.literal("当前伤害加成: +" + bonusDamage));
    }
}
