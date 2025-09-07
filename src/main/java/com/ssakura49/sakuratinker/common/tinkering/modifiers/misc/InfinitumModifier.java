package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class InfinitumModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (holder instanceof Player && modifier.getLevel() > 0) {
            return 0;
        }
        return amount;
    }
}
