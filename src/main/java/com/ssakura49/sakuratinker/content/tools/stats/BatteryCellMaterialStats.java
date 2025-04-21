package com.ssakura49.sakuratinker.content.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record BatteryCellMaterialStats(int energyCapacity, float durability) implements IMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "battery_cell");
    public static final MaterialStatType<BatteryCellMaterialStats> TYPE;
    private static final String ENERGY_CAPACITY_PREFIX;
    private static final String DURABILITY_PREFIX;
    private static final List<Component> DESCRIPTION;

    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    public @NotNull List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(IToolStat.formatColoredBonus(ENERGY_CAPACITY_PREFIX, (float)this.energyCapacity));
        info.add(IToolStat.formatColoredPercentBoost(DURABILITY_PREFIX, this.durability));
        return info;
    }

    public @NotNull List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        STToolStats.ENERGY_STORE.add(builder, (double)(this.energyCapacity));
        ToolStats.DURABILITY.percent(builder, (double)(this.durability));
    }

    static {
        TYPE = new MaterialStatType<>(ID, new BatteryCellMaterialStats(0, 0.0F), RecordLoadable.create(
                        IntLoadable.FROM_ZERO.defaultField("energy_capacity", 0, true, BatteryCellMaterialStats::energyCapacity),
                        FloatLoadable.ANY.defaultField("durability", 1.0F, true, BatteryCellMaterialStats::durability), BatteryCellMaterialStats::new));
        ENERGY_CAPACITY_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.location("energy_capacity"));
        DURABILITY_PREFIX = IMaterialStats.makeTooltipKey(TConstruct.getResource("durability"));
        DESCRIPTION = ImmutableList.of(IMaterialStats.makeTooltip(SakuraTinker.location("battery_cell.energy_capacity.description")), IMaterialStats.makeTooltip(TConstruct.getResource("handle.durability.description")));
    }
}
