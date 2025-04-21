package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.ForgeEnergyModifier;
import com.ssakura49.sakuratinker.utils.ToolEnergyUtil;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnergyInfusionModifier extends ForgeEnergyModifier {
    @Override
    public int getCapacity(ModifierEntry entry) {
        return 100000 * entry.getLevel();
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        amount = amount - ToolEnergyUtil.extractEnergy(tool, amount*200,false)/200;
        return amount;
    }
}
