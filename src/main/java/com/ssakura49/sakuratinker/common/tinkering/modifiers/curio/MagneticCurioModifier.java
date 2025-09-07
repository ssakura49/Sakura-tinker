package com.ssakura49.sakuratinker.common.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.upgrades.general.MagneticModifier;
import top.theillusivec4.curios.api.SlotContext;

public class MagneticCurioModifier extends CurioModifier {
    public MagneticCurioModifier(){
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onCurioTick(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack stack) {
        if (entity.tickCount % 20 == 0) {
            MagneticModifier.applyMagnet(entity, entry.getLevel());
        }
    }
}
