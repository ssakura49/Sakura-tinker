package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.tinker.ToolEnergyUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;


public class EnergyInfusionModifier extends BaseModifier {
    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        float energy = level * 10000;
        STToolStats.ENERGY_STORAGE.update(builder, energy);
    }


    @Override
    public int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        amount -= ToolEnergyUtil.extractEnergy(tool, amount * 200, false) / 200;
        return amount;
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry entry) {
        if(entry.getLevel() == 1 && (tool.getModifierLevel(STModifiers.EnergyDisplay.get()) < 1)){
            return Component.translatable("tooltip.sakuratinker.modifier.requirement").append(" :").append(String.valueOf(STModifiers.EnergyDisplay.get()));
        }
        return null;
    }
}
