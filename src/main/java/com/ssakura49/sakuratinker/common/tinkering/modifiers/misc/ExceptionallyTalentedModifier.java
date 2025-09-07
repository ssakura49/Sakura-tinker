package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class ExceptionallyTalentedModifier extends BaseModifier implements VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.VOLATILE_DATA);
    }
    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        int upgrade = volatileData.getSlots(SlotType.UPGRADE);
        int ability = volatileData.getSlots(SlotType.ABILITY);
        volatileData.setSlots(SlotType.UPGRADE, upgrade * 2);
        volatileData.setSlots(SlotType.ABILITY, ability * 2);
    }
}
