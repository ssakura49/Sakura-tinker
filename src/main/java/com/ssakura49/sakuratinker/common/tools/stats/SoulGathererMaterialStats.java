package com.ssakura49.sakuratinker.common.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public record SoulGathererMaterialStats(float soulPower, float soulIncrease) implements IMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "soul_gatherer.json");
    public static final MaterialStatType<SoulGathererMaterialStats> TYPE;
    private static final String SOUL_POWER;
    private static final String SOUL_INCREASE;
    private static final List<Component> DESCRIPTION;

    public SoulGathererMaterialStats(float soulPower, float soulIncrease) {
        this.soulPower = soulPower;
        this.soulIncrease = soulIncrease;
    }

    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(IToolStat.formatColoredBonus(SOUL_POWER, this.soulPower));
        info.add(IToolStat.formatColoredPercentBoost(SOUL_INCREASE, this.soulIncrease));
        return info;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        STToolStats.SOUL_POWER.update(builder, this.soulPower * scale);
        STToolStats.SOUL_INCREASE.update(builder, this.soulIncrease * scale);
    }

    public float soulPower(){
        return this.soulPower;
    }
    public float soulIncrease(){
        return this.soulIncrease;
    }

    static {
        TYPE = new MaterialStatType<>(ID, new SoulGathererMaterialStats(0, 0), RecordLoadable.create(
                FloatLoadable.ANY.defaultField("soul_power", 0F, SoulGathererMaterialStats::soulPower),
                FloatLoadable.ANY.defaultField("soul_increase", 0F, SoulGathererMaterialStats::soulIncrease), SoulGathererMaterialStats::new
        ));
        SOUL_POWER = IMaterialStats.makeTooltipKey(SakuraTinker.location("soul_power"));
        SOUL_INCREASE = IMaterialStats.makeTooltipKey(SakuraTinker.location("soul_increase"));
        DESCRIPTION = ImmutableList.of(
                IMaterialStats.makeTooltip(SakuraTinker.location("soul_power")),
                IMaterialStats.makeTooltip(SakuraTinker.location("soul_increase"))
        );
    }
}
