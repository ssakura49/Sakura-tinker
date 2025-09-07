package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.ArmorLootingModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.LootingModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockHarvestModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.LootingContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class SuperLuckModifier extends NoLevelsModifier implements LootingModifierHook, ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.WEAPON_LOOTING,ModifierHooks.PROCESS_LOOT);
    }
    @Override
    public int updateLooting(IToolStackView tool, ModifierEntry modifier, LootingContext context, int looting) {
        return looting + 10;
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> loot, LootContext context) {
        List<ItemStack> copy = new ArrayList<>(loot);
        for (int i = 0; i < 9; i++) {
            for (ItemStack stack : copy) {
                loot.add(stack.copy());
            }
        }
    }

}
