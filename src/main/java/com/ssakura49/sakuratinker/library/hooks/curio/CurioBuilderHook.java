package com.ssakura49.sakuratinker.library.hooks.curio;

import com.ssakura49.sakuratinker.library.logic.context.AttributeData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface CurioBuilderHook {
    default void onCurioTick(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack stack) {
    }
    default void modifyCurioAttribute(IToolStackView curio, SlotContext context, UUID uuid, int level, BiConsumer<Attribute, AttributeModifier> consumer) {
    }
    default void onCurioEquip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack prevStack, ItemStack stack) {
    }
    default void onCurioUnequip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack newStack, ItemStack stack) {
    }
    default int onCurioGetFortune(IToolStackView curio, SlotContext slotContext, LootContext lootContext, ItemStack stack, int fortune, int level) {
        return fortune;
    }
    default int onCurioGetLooting(IToolStackView curio, SlotContext slotContext, DamageSource source, LivingEntity target, ItemStack stack, int Looting, int level) {
        return Looting;
    }

    public static record AllMerger(Collection<CurioBuilderHook> modules) implements CurioBuilderHook {
        public AllMerger(Collection<CurioBuilderHook> modules) {
            this.modules = modules;
        }

        public void onCurioTick(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack stack) {
            for(CurioBuilderHook module : this.modules) {
                module.onCurioTick(curio, context, entity, level, stack);
            }

        }

        public void modifyCurioAttribute(IToolStackView curio, SlotContext context, UUID uuid, int level, BiConsumer<Attribute, AttributeModifier> consumer) {
            for(CurioBuilderHook module : this.modules) {
                module.modifyCurioAttribute(curio, context, uuid, level, consumer);
            }

        }

        public void onCurioEquip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack prevStack, ItemStack stack) {
            for(CurioBuilderHook module : this.modules) {
                module.onCurioEquip(curio, context, entity, level, prevStack, stack);
            }

        }

        public void onCurioUnequip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack newStack, ItemStack stack) {
            for(CurioBuilderHook module : this.modules) {
                module.onCurioUnequip(curio, context, entity, level, newStack, stack);
            }

        }

        public int onCurioGetFortune(IToolStackView curio, SlotContext slotContext, LootContext lootContext, ItemStack stack, int fortune, int level) {
            for(CurioBuilderHook module : this.modules) {
                module.onCurioGetFortune(curio, slotContext, lootContext, stack, fortune, level);
            }

            return fortune;
        }

        public int onCurioGetLooting(IToolStackView curio, SlotContext slotContext, DamageSource source, LivingEntity target, ItemStack stack, int Looting, int level) {
            for(CurioBuilderHook module : this.modules) {
                module.onCurioGetLooting(curio, slotContext, source, target, stack, Looting, level);
            }

            return Looting;
        }

        public Collection<CurioBuilderHook> modules() {
            return this.modules;
        }
    }
}