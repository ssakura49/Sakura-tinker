package com.ssakura49.sakuratinker.generic;

import com.ssakura49.sakuratinker.library.interfaces.CuriosFactory;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioArrowHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBehaviorHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioCombatHook;
import com.ssakura49.sakuratinker.library.tools.STHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public abstract class CurioModifier extends BaseModifier implements CurioBuilderHook, CurioBehaviorHook, CurioCombatHook, CurioArrowHook, CuriosFactory {
    public CurioModifier(){}

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        this.initCuriosHooks(builder);
        builder.addHook(this,
                STHooks.CURIO_BUILDER,
                STHooks.CURIO_BEHAVIOR,
                STHooks.CURIO_COMBAT,
                STHooks.CURIO_ARROW);
    }

    @Override
    public void onCurioEquip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack prevStack, ItemStack stack) {
        this.onUseKeyEquip(entity, level, false);
    }

    @Override
    public void onCurioUnequip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack newStack, ItemStack stack) {
        this.onUseKeyUnequip(entity);
    }

    public TinkerDataCapability.TinkerDataKey<Integer> useKey() {
        return null;
    }

    public void onUseKeyEquip(LivingEntity entity, int level, boolean toAdd) {
        if (this.useKey() != null) {
            entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                int def = (Integer)holder.get(this.useKey(), 0);
                if (def < level && !toAdd) {
                    holder.put(this.useKey(), level);
                } else {
                    holder.put(this.useKey(), def + level);
                }
            });
        }
    }

    public void onUseKeyUnequip(LivingEntity entity) {
        if (this.useKey() != null) {
            entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> holder.remove(this.useKey()));
        }
    }
}
