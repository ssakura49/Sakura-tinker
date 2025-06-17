package com.ssakura49.sakuratinker.library.tinkering.modules;

import slimeknights.mantle.data.loadable.primitive.EnumLoadable;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public enum StatOperation {
    ADDITION("add") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            stat.add(builder, value);
        }
    },
    SUBTRACTION("subtract") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            stat.add(builder, (double)-value);
        }
    },
    MULTIPLY_BASE("multiply_base") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            stat.multiply(builder, (double)(1.0F + value));
        }
    },
    MULTIPLY_CONDITIONAL("multiply_conditional") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            builder.multiplier(stat, (double)(1.0F + value));
        }
    },
    MULTIPLY_ALL("multiply_all") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            stat.multiplyAll(builder, (double)(1.0F + value));
        }
    },
    PERCENT_OF_BASE("percent_of_base") {
        @Override
        public <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value) {
            if (stat instanceof FloatToolStat floatStat) {
                float baseValue = floatStat.getDefaultValue();
                floatStat.add(builder, baseValue * value);
            }
        }
    };

    private final String serializedName;
    public static final EnumLoadable<StatOperation> LOADER =
            new EnumLoadable<>(StatOperation.class);

    StatOperation(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public abstract <T extends Number> void apply(ModifierStatsBuilder builder, INumericToolStat<T> stat, float value);
}
