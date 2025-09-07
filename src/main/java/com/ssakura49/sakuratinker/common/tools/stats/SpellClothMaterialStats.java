package com.ssakura49.sakuratinker.common.tools.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public record SpellClothMaterialStats(float baseDamage, float spellPower, float spellReduce, float castTime) implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(SakuraTinker.MODID, "spell_cloth");
    public static final MaterialStatType<SpellClothMaterialStats> TYPE;
    private static final String BASE_SPELL_DAMAGE;
    private static final String SPELL_POWER_PREFIX;
    private static final String SPELL_REDUCE_PREFIX;
    private static final String CAST_TIME_PREFIX;
    private static final List<Component> DESCRIPTION;

    public SpellClothMaterialStats(float baseDamage, float spellPower, float spellReduce, float castTime) {
        this.baseDamage = baseDamage;
        this.spellPower = spellPower;
        this.spellReduce = spellReduce;
        this.castTime = castTime;
    }

    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(IToolStat.formatColoredBonus(BASE_SPELL_DAMAGE, (float) this.baseDamage));
        info.add(IToolStat.formatColoredPercentBoost(SPELL_POWER_PREFIX, (float) this.spellPower));
        info.add(IToolStat.formatColoredPercentBoost(SPELL_REDUCE_PREFIX, (float) this.spellReduce));
        info.add(IToolStat.formatColoredPercentBoost(CAST_TIME_PREFIX, (float) this.castTime));
        return info;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    public void apply(ModifierStatsBuilder builder, float scale) {
        STToolStats.BASE_SPELL_DAMAGE.update(builder, (float) (this.baseDamage * scale));
        STToolStats.SPELL_POWER.update(builder, (float) this.spellPower * scale);
        STToolStats.SPELL_REDUCE.update(builder, (float) this.spellReduce * scale);
        STToolStats.CAST_TIME.update(builder, (float) (this.castTime * scale));
    }

    public int durability() {
        return 0;
    }

    public float baseDamage() {
        return this.baseDamage;
    }

    public float spellPower() {
        return this.spellPower;
    }

    public float spellReduce() {
        return this.spellReduce;
    }

    public float castTime() {
        return this.castTime;
    }

    static {
        TYPE = new MaterialStatType<>(ID, new SpellClothMaterialStats(0.0F,0.0F, 0.0F, 0.0F), RecordLoadable.create(
                FloatLoadable.ANY.defaultField("base_spell_damage", 0.0F, true, SpellClothMaterialStats::baseDamage),
                FloatLoadable.ANY.defaultField("spell_power", 0.0F, true, SpellClothMaterialStats::spellPower),
                FloatLoadable.ANY.defaultField("spell_reduce", 0.0F, true, SpellClothMaterialStats::spellReduce),
                FloatLoadable.ANY.defaultField("cast_time", 0.0F, true, SpellClothMaterialStats::castTime), SpellClothMaterialStats::new));
        BASE_SPELL_DAMAGE = IMaterialStats.makeTooltipKey(SakuraTinker.location("base_spell_damage"));
        SPELL_POWER_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.location("spell_power"));
        SPELL_REDUCE_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.location("spell_reduce"));
        CAST_TIME_PREFIX = IMaterialStats.makeTooltipKey(SakuraTinker.location("cast_time"));
        DESCRIPTION = ImmutableList.of(
                IMaterialStats.makeTooltip(SakuraTinker.location("base_spell_damage.description")),
                IMaterialStats.makeTooltip(SakuraTinker.location("spell_power.description")),
                IMaterialStats.makeTooltip(SakuraTinker.location("spell_reduce.description")),
                IMaterialStats.makeTooltip(SakuraTinker.location("cast_time.description"))
        );
    }
}
