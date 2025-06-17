package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class SoulModifier extends BaseModifier implements VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.VOLATILE_DATA);
    }
    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        int level = modifier.getLevel();
        volatileData.addSlots(SlotType.UPGRADE, 2 * level);
        if (context.hasTag(TinkerTags.Items.ARMOR)) {
            volatileData.addSlots(SlotType.DEFENSE, 2 * level);
        } else {
            volatileData.addSlots(SlotType.ABILITY, 2 * level);
        }
    }
}
