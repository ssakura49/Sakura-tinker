package com.ssakura49.sakuratinker.mixin.tinker;

import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableArrowItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.modules.ranged.TrickQuiverModule;

@Mixin(TrickQuiverModule.class)
public abstract class TrickQuiverModuleMixin {
    @Final
    @Shadow(remap = false)
    private static ResourceLocation SELECTED_SLOT;

    /**
     * 替换 TrickQuiverModule.shrinkAmmo 中 ammo.shrink(needed);
     * 改成对匠魂箭耐久扣除，不减少数量
     */
    @Redirect(method = "shrinkAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void redirectTrickQuiverShrink(ItemStack ammo, int needed, IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammoStack, int needed2) {
        if (ammo.getItem() instanceof ModifiableArrowItem) {
            ToolDamageUtil.damageAnimated(ToolStack.from(ammo), needed, shooter);
        } else {
            ammo.shrink(needed);
        }
        modifier.getHook(ToolInventoryCapability.HOOK).setStack(tool, modifier, tool.getPersistentData().getInt(SELECTED_SLOT), ammo);

    }
}
