package com.ssakura49.sakuratinker.compat.IronSpellBooks.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.library.hooks.curio.behavior.CurioAttributeModifierHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableSpellBookItem;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import com.ssakura49.sakuratinker.utils.tinker.TooltipUtil;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class TinkerSpellBook extends ModifiableSpellBookItem {
    private int maxSpellSlots;
    public static final ToolDefinition TINKER_SPELL_BOOK = ToolDefinition.create(ISSCompat.tinker_spell_book);
    public TinkerSpellBook(int maxSpellSlots ,Item.Properties properties, SpellRarity spellRarity) {
        super(10 ,properties, spellRarity, TINKER_SPELL_BOOK);
        this.maxSpellSlots = maxSpellSlots;
    }

    public void setMaxSpellSlots(int maxSpellSlots) {
        this.maxSpellSlots = maxSpellSlots;
    }

    @Override
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        return this.getSpellBookStats(tool, player, tooltips, key, tooltipFlag);
    }

    public List<Component> getSpellBookStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        builder.add(ToolStats.ATTACK_DAMAGE);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.BASE_SPELL_DAMAGE);
        TooltipUtil.addPerToolStatTooltip(builder, tool, STToolStats.SPELL_POWER);
        TooltipUtil.addPerToolStatTooltip(builder, tool, STToolStats.SPELL_REDUCE);
        TooltipUtil.addPerToolStatTooltip(builder, tool, STToolStats.CAST_TIME);
        builder.addAllFreeSlots();

        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }

        return tooltips;
    }
}
