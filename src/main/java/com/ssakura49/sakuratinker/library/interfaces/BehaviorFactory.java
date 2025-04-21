package com.ssakura49.sakuratinker.library.interfaces;

import java.util.function.BiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface BehaviorFactory extends AttributesModifierHook, ToolDamageModifierHook, BreakSpeedModifierHook {
    default void initBehaviorHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.ATTRIBUTES);
        builder.addHook(this, ModifierHooks.TOOL_DAMAGE);
        builder.addHook(this, ModifierHooks.BREAK_SPEED);
    }
    /**
     *添加attribute修饰符
     */
    default void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
    }
    /**
     *武器耐久损耗
     */
    default int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity entity) {
        return amount;
    }
    /**
     *挖掘速度
     */
    default void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction sidHit, boolean isEffective, float miningSpeedModifier) {
    }
}
