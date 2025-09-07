package com.ssakura49.sakuratinker.common.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public record YoYoRingMaterialStats(float maxCollected)implements IMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "yoyo_ring");
    public static final MaterialStatType<YoYoRingMaterialStats> TYPE;
    private static final String MAX_COLLECTED;
    private static final List<Component> DESCRIPTION;

    public YoYoRingMaterialStats(float maxCollected) {
        this.maxCollected = maxCollected;
    }

    public @NotNull MaterialStatType<?> getType() {
        return TYPE;
    }

    public @NotNull List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(IToolStat.formatColoredBonus(MAX_COLLECTED, this.maxCollected));
        return info;
    }

    public @NotNull List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        STToolStats.TIME.update(builder, this.maxCollected * scale);
    }

    public float maxCollected(){
        return this.maxCollected;
    }

    static {
        TYPE = new MaterialStatType<>(ID, new YoYoRingMaterialStats(0), RecordLoadable.create(
                FloatLoadable.ANY.defaultField("max_collected", 0F, YoYoRingMaterialStats::maxCollected), YoYoRingMaterialStats::new
        ));
        MAX_COLLECTED = IMaterialStats.makeTooltipKey(SakuraTinker.location("max_collected"));
        DESCRIPTION = ImmutableList.of(
                IMaterialStats.makeTooltip(SakuraTinker.location("max_collected"))
        );
    }
}
