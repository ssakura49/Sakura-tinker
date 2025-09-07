package com.ssakura49.sakuratinker.common.tinkering.modifiers.special;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.tools.item.RepairKitItem;

import java.util.List;

public class PolishingModifier extends PolishModifier implements InventoryTickModifierHook, MeleeHitModifierHook, ProjectileLaunchModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_LAUNCH);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.isClientSide || !(holder instanceof Player player)) return;

        int level = modifier.getLevel();
        if (level <= 0) return;

        if (player.tickCount % 20 != 0) return;

        if (!isSelected && !isCorrectSlot) return;
        int capacity = getPolishCapacity(tool, modifier);
        int currentPolish = getPolish(tool);
        int polishAmount = 10;
        if (currentPolish >= capacity) return;
        if (capacity - currentPolish <= polishAmount) return;
        MaterialNBT materialNBT = tool.getMaterials();
        List<MaterialId> materialIds = materialNBT.getList()
                .stream()
                .map(variant -> variant.getId().getId())
                .toList();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack invStack = player.getInventory().getItem(i);
            if (invStack.getItem() instanceof RepairKitItem kit) {
                MaterialId material = (MaterialId) kit.getMaterial(invStack);
                if (materialIds.contains(material)) {
                    invStack.shrink(1);
//                    int gain = capacity / 10;
                    this.addPolish(tool, modifier, polishAmount);
                    break;
                }
            }
        }
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (primary) {
            this.addPolish(tool, modifier, -1);
        }
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        this.addPolish(tool, modifier, -1);
    }
}

