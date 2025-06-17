package com.ssakura49.sakuratinker.content.tinkering.modules;

import com.ssakura49.sakuratinker.library.tinkering.modules.StatOperation;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.mapping.MapLoadable;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.*;

import java.util.List;
import java.util.Map;

public record EnvironmentalAdaptationModule(INumericToolStat<?> stat, Map<ResourceLocation, BiomeBoost> biomeBoosts, StatOperation operation, float amount, boolean multiplier) implements ModifierModule, ToolStatsModifierHook, InventoryTickModifierHook {
    public static final RecordLoadable<EnvironmentalAdaptationModule> LOADER;
    public static final ResourceLocation BIOME_BOOST_KEY = new ResourceLocation("biome_boost", "biome");
    public record BiomeBoost(float multiplier) {
        public static final RecordLoadable<BiomeBoost> LOADER = RecordLoadable.create(
                FloatLoadable.ANY.defaultField("multiplier", 1.0f, BiomeBoost::multiplier),
                BiomeBoost::new
        );
    }
    public EnvironmentalAdaptationModule(INumericToolStat<?> stat, Map<ResourceLocation, BiomeBoost> biomeBoosts, StatOperation operation, float amount, boolean multiplier)  {
        this.stat = stat;
        this.biomeBoosts = biomeBoosts;
        this.operation = operation;
        this.amount = amount;
        this.multiplier = multiplier;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isSelected && !world.isClientSide) {
            world.getBiome(holder.blockPosition()).unwrapKey().ifPresent(biomeKey -> tool.getPersistentData().putString(BIOME_BOOST_KEY, biomeKey.location().toString()));
        }
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        String biomeIdStr = context.getPersistentData().getString(BIOME_BOOST_KEY);
        if (!biomeIdStr.isEmpty()) {
            ResourceLocation biomeId = new ResourceLocation(biomeIdStr);
            BiomeBoost boost = biomeBoosts.get(biomeId); // Map<ResourceLocation, BiomeBoost>
            float baseValue = ToolUtil.getStatValue(builder, this.stat, this.multiplier);
            if (boost != null) {
                if (stat instanceof FloatToolStat toolStat) {
                    this.operation.apply(builder, toolStat, baseValue * this.amount * modifier.getLevel());
                }
            }
        }
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.TOOL_STATS, ModifierHooks.INVENTORY_TICK);
    }

    static {
        LOADER = RecordLoadable.create(
                ToolStats.NUMERIC_LOADER.requiredField("stat", EnvironmentalAdaptationModule::stat),
                new MapLoadable<>(Loadables.RESOURCE_LOCATION, BiomeBoost.LOADER, 0)
                        .requiredField("biome_boosts", EnvironmentalAdaptationModule::biomeBoosts),
                StatOperation.LOADER.requiredField("operation", EnvironmentalAdaptationModule::operation),
                FloatLoadable.ANY.requiredField("amount", EnvironmentalAdaptationModule::amount),
                BooleanLoadable.DEFAULT.defaultField("multiplier", true, EnvironmentalAdaptationModule::multiplier),
                EnvironmentalAdaptationModule::new
        );
    }
}
