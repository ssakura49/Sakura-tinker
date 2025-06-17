package com.ssakura49.sakuratinker.utils.tinker;

import java.text.DecimalFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.utils.Util;

public class TooltipUtil {
    public static final DecimalFormat VALUE_FORMAT = new DecimalFormat("#.##");
    private static final String TOOLTIP_PREFIX = "tooltip.sakuratinker.";

    private TooltipUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static void addStatTooltip(TooltipBuilder builder, IToolStackView tool, FloatToolStat stat, DecimalFormat format) {
        builder.add(Component.translatable("tool_stat." + stat.getName().toLanguageKey())
                .append(Component.literal(format.format(tool.getStats().get(stat))))
                .withStyle(style -> style.withColor(stat.getColor())));
    }

    public static void addToolStatTooltip(TooltipBuilder builder, IToolStackView tool, FloatToolStat stat) {
        addStatTooltip(builder, tool, stat, VALUE_FORMAT);
    }

    public static void addPerToolStatTooltip(TooltipBuilder builder, IToolStackView tool, FloatToolStat stat) {
        addStatTooltip(builder, tool, stat, Util.PERCENT_FORMAT);
    }

    public static ItemStack getToolStack(LivingEntity entity, Modifier modifier){
        ItemStack mainHandStack = entity.getMainHandItem();
        if(mainHandStack.getItem() instanceof IModifiable && ToolStack.from(mainHandStack).getModifierLevel(modifier) > 0){
            return mainHandStack;
        } else {
            ItemStack offHandStack = entity.getOffhandItem();
            if(offHandStack.getItem() instanceof IModifiable && ToolStack.from(offHandStack).getModifierLevel(modifier) > 0){
                return offHandStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static Component addTooltip(String comp) {
        return Component.translatable(TOOLTIP_PREFIX + comp);
    }

    public static Component addTooltipWithValue(String comp, int val) {
        return Component.translatable(TOOLTIP_PREFIX + comp)
                .append(Component.literal(String.valueOf(val)));
    }

    public static Component addTooltipWithRes(String comp, String res) {
        return Component.translatable(TOOLTIP_PREFIX + comp)
                .append(res != null ? res : "");
    }

    public static Component addTooltipWithPos(String comp, int x, int y, int z) {
        return Component.translatable(TOOLTIP_PREFIX + comp)
                .append(Component.literal(String.format("%d, %d, %d", x, y, z)));
    }
}
