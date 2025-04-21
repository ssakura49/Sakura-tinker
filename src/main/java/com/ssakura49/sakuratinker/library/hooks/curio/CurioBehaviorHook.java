package com.ssakura49.sakuratinker.library.hooks.curio;

import com.ssakura49.sakuratinker.library.events.ItemStackDamageEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioBehaviorHook {
//    CurioBehaviorHook EMPTY = new CurioBehaviorHook() {
//    };
    //工具受到损坏时触发
    default void onCurioGetToolDamage(IToolStackView curio, LivingEntity entity, ItemStackDamageEvent event, int level) {
    }
    //实体受到治疗时触发
    default void onCurioTakeHeal(IToolStackView curio, LivingHealEvent event, LivingEntity entity, int level) {
    }
    //玩家破坏方块时触发
    default void onCurioBreakSpeed(IToolStackView curio, PlayerEvent.BreakSpeed event, Player player, int level) {
    }

    public static record AllMerger(Collection<CurioBehaviorHook> modules) implements CurioBehaviorHook {
        public AllMerger(Collection<CurioBehaviorHook> modules) {
            this.modules = modules;
        }

        public void onCurioGetToolDamage(IToolStackView curio, LivingEntity entity, ItemStackDamageEvent event, int level) {
            for(CurioBehaviorHook module : this.modules) {
                module.onCurioGetToolDamage(curio, entity, event, level);
            }

        }

        public void onCurioTakeHeal(IToolStackView curio, LivingHealEvent event, LivingEntity entity, int level) {
            for(CurioBehaviorHook module : this.modules) {
                module.onCurioTakeHeal(curio, event, entity, level);
            }

        }

        public void onCurioBreakSpeed(IToolStackView curio, PlayerEvent.BreakSpeed event, Player player, int level) {
            for(CurioBehaviorHook module : this.modules) {
                module.onCurioBreakSpeed(curio, event, player, level);
            }

        }

        public Collection<CurioBehaviorHook> modules() {
            return this.modules;
        }
    }
}
