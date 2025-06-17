package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableArrowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.function.Predicate;

public class ArrowAmmo extends BaseModifier {
    public ArrowAmmo(){}

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int getPriority() {
        return 9999;
    }

    @Override
    public @NotNull ItemStack findAmmo(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, ItemStack itemStack, Predicate<ItemStack> predicate) {
        if (livingEntity instanceof Player player) {
            Inventory inventory = player.getInventory();
            int multiShotLevel = tool.getModifierLevel(TinkerModifiers.multishot.getId());

            ItemStack mainHandAmmo = getValidArrowStack(player.getMainHandItem(), multiShotLevel);
            if (!mainHandAmmo.isEmpty()) return mainHandAmmo;

            ItemStack offHandAmmo = getValidArrowStack(player.getOffhandItem(), multiShotLevel);
            if (!offHandAmmo.isEmpty()) return offHandAmmo;

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = getValidArrowStack(inventory.getItem(i), multiShotLevel);
                if (!stack.isEmpty()) return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getValidArrowStack(ItemStack stack, int multiShotLevel) {
        if (stack.getItem() instanceof ModifiableArrowItem && !ToolStack.from(stack).isBroken()) {
            if (multiShotLevel > 0) {
                ItemStack copy = stack.copy();
                copy.setCount(1 + multiShotLevel * 2);
                return copy;
            }
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
        if (!(shooter instanceof Player player)) {
            return;
        }
        Inventory inventory = player.getInventory();
        if (ammo.getItem() instanceof ModifiableArrowItem) {
            if (ammo.getCount() > 1) {
                ItemStack arrow = ammo.copy();
                arrow.setCount(1);
                if (arrow.getItem() == player.getMainHandItem().getItem() && !ToolStack.from(player.getMainHandItem()).isBroken()) {
                    IToolStackView tools = ToolStack.from(player.getMainHandItem());
                    ToolDamageUtil.damageAnimated(tools, needed, shooter);
                } else if (arrow.getItem() == player.getOffhandItem().getItem() && !ToolStack.from(player.getOffhandItem()).isBroken()) {
                    IToolStackView tools = ToolStack.from(player.getOffhandItem());
                    ToolDamageUtil.damageAnimated(tools, needed, shooter);
                } else {
                    for (int i = 0; i < inventory.getContainerSize(); ++i) {
                        ItemStack itemstack = inventory.getItem(i);
                        if (arrow.getItem() == itemstack.getItem() && !ToolStack.from(itemstack).isBroken()) {
                            IToolStackView tools = ToolStack.from(itemstack);
                            ToolDamageUtil.damageAnimated(tools, needed, shooter);
                            break;
                        }
                    }
                }
            } else {
                IToolStackView tools = ToolStack.from(ammo);
                ToolDamageUtil.damageAnimated(tools, needed, shooter);
            }
        }
    }
}
