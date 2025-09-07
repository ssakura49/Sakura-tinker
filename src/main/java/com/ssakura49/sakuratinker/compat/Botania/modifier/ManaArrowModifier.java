package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Predicate;

public class ManaArrowModifier extends Modifier implements BowAmmoModifierHook {
    private static final int MANA_COST = 200;
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.BOW_AMMO);
    }

    @Override
    public @NotNull ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, ItemStack toolStack, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player && hasEnoughMana(player)) {
            return MANA_ARROW.copy();
        }
        return ItemStack.EMPTY;
    }
    @Override
    public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
        if (shooter instanceof Player player) {
            if (isManaArrow(ammo)) {
                ManaItemHandler.INSTANCE.requestManaExactForTool(new ItemStack(BotaniaItems.terraSword), player, MANA_COST * needed, true);
                return;
            }
        }
        BowAmmoModifierHook hook = getHook(ModifierHooks.BOW_AMMO);
        hook.shrinkAmmo(tool, modifier, shooter, ammo, needed);
    }

    private boolean isManaArrow(ItemStack ammo) {
        if (ammo.getTag() != null) {
            return ammo.hasTag() && ammo.getTag().getBoolean("ManaArrow");
        }
        return false;
    }

    private boolean hasEnoughMana(Player player) {
        return ManaItemHandler.INSTANCE.requestManaExactForTool(new ItemStack(BotaniaItems.terraSword), player, MANA_COST, false);
    }
    public static final ItemStack MANA_ARROW = createManaArrow();
    private static ItemStack createManaArrow() {
        ItemStack stack = new ItemStack(Items.ARROW);
        stack.getOrCreateTag().putBoolean("ManaArrow", true);
        return stack;
    }
}
