package com.ssakura49.sakuratinker.compat.Goety.item;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.magic.SpellType;
import com.ssakura49.sakuratinker.compat.Goety.init.GoetyItems;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableWandItem;
import com.ssakura49.sakuratinker.utils.tinker.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class TinkerWandItem extends ModifiableWandItem implements ISoulRepair {
    public static final ToolDefinition TINKER_WAND = ToolDefinition.create(GoetyItems.tinker_wand);
    public TinkerWandItem(Item.Properties properties, SpellType spellType) {
        super(properties, spellType, TINKER_WAND, 1);
    }

    private List<Component> getTinkerDarkWandStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        builder.add(ToolStats.ATTACK_DAMAGE);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.SOUL_POWER);
        TooltipUtil.addPerToolStatTooltip(builder, tool, STToolStats.SOUL_INCREASE);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    @Override
    public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        return this.getTinkerDarkWandStats(tool, player, tooltips, key, flag);
    }
}
