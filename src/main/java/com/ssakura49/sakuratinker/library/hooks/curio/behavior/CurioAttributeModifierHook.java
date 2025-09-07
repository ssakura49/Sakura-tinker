package com.ssakura49.sakuratinker.library.hooks.curio.behavior;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface CurioAttributeModifierHook {
    default void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
    }
    public static record AllMerger(Collection<CurioAttributeModifierHook> modules) implements CurioAttributeModifierHook {
        public AllMerger(Collection<CurioAttributeModifierHook> modules) {
            this.modules = modules;
        }

        public void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
            for(CurioAttributeModifierHook module : this.modules) {
                module.modifyCurioAttribute(curio,entry, context, uuid, consumer);
            }

        }

        public Collection<CurioAttributeModifierHook> modules() {
            return this.modules;
        }
    }
}
