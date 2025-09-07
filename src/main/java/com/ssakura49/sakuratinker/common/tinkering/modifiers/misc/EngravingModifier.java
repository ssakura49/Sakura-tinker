package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class EngravingModifier extends BaseModifier {
    @Override
    public void addVolatileData(IToolContext context, ModifierEntry entry, ToolDataNBT nbt){
        int level = entry.getLevel();
        nbt.addSlots(SlotType.UPGRADE, level);
    }
}
