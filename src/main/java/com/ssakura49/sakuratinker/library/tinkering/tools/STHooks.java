package com.ssakura49.sakuratinker.library.tinkering.tools;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerDamagePreHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerDamageTakeHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerKnockBackHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerTakeHealHook;
import com.ssakura49.sakuratinker.library.hooks.builder.ReplaceMaterialModifierHook;
import com.ssakura49.sakuratinker.library.hooks.click.LeftClickModifierHook;
import com.ssakura49.sakuratinker.library.hooks.combat.*;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioArrowHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBehaviorHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioCombatHook;
import com.ssakura49.sakuratinker.library.hooks.click.ItemClickUsedHook;
import com.ssakura49.sakuratinker.library.hooks.combat.ModifyDamageSourceModifierHook;
import slimeknights.mantle.data.registry.IdAwareComponentRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class STHooks {
    public static final IdAwareComponentRegistry<ModuleHook<?>> LOADER = new IdAwareComponentRegistry<>("Modifier Hook");
    public static final ModuleHook<WearerDamagePreHook> WEARER_DAMAGE_PRE;
    public static final ModuleHook<WearerKnockBackHook> WEARER_KNOCK_BACK;
    public static final ModuleHook<WearerDamageTakeHook> WEARER_DAMAGE_TAKE;
    public static final ModuleHook<WearerTakeHealHook> WEARER_TAKE_HEAL;
    public static final ModuleHook<GenericCombatHook> GENERIC_COMBAT;
    public static final ModuleHook<MeleeCooldownHook> MELEE_COOLDOWN;
    public static final ModuleHook<HolderDamageTakeHook> HOLDER_DAMAGE_TAKE;
    public static final ModuleHook<ShieldBlockingHook> SHIELD_BLOCKING;
    public static final ModuleHook<CurioBuilderHook> CURIO_BUILDER;
    public static final ModuleHook<CurioBehaviorHook> CURIO_BEHAVIOR;
    public static final ModuleHook<CurioCombatHook> CURIO_COMBAT;
    public static final ModuleHook<CurioArrowHook> CURIO_ARROW;
    public static final ModuleHook<ItemClickUsedHook> ITEM_CLICK_USED;
    public static final ModuleHook<ModifyDamageSourceModifierHook> MODIFY_DAMAGE_SOURCE;
    public static final ModuleHook<LeftClickModifierHook> LEFT_CLICK;
    public static final ModuleHook<CriticalAttackModifierHook> CRITICAL_ATTACK;
    public static final ModuleHook<CauseDamageModifierHook> CAUSE_DAMAGE;
    public static final ModuleHook<ReplaceMaterialModifierHook> REPLACE_MATERIAL_MODIFIER;

    public STHooks(){}

    static {
        WEARER_DAMAGE_PRE = ModifierHooks.register(SakuraTinker.location("wearer_damage_pre"), WearerDamagePreHook.class, WearerDamagePreHook.AllMerger::new, new WearerDamagePreHook() {});
        WEARER_KNOCK_BACK = ModifierHooks.register(SakuraTinker.location("wearer_knock_back"), WearerKnockBackHook.class, WearerKnockBackHook.AllMerger::new, new WearerKnockBackHook() {});
        WEARER_DAMAGE_TAKE = ModifierHooks.register(SakuraTinker.location("wearer_damage_take"), WearerDamageTakeHook.class, WearerDamageTakeHook.AllMerger::new, new WearerDamageTakeHook() {});
        WEARER_TAKE_HEAL = ModifierHooks.register(SakuraTinker.location("wearer_take_heal"), WearerTakeHealHook.class, WearerTakeHealHook.AllMerger::new, new WearerTakeHealHook() {});
        GENERIC_COMBAT = ModifierHooks.register(SakuraTinker.location("generic_combat"), GenericCombatHook.class, GenericCombatHook.AllMerge::new, new GenericCombatHook() {});
        MELEE_COOLDOWN = ModifierHooks.register(SakuraTinker.location("melee_cooldown"), MeleeCooldownHook.class, MeleeCooldownHook.AllMerger::new, new MeleeCooldownHook() {});
        HOLDER_DAMAGE_TAKE = ModifierHooks.register(SakuraTinker.location("holder_damage_take"), HolderDamageTakeHook.class, HolderDamageTakeHook.AllMerge::new, new HolderDamageTakeHook() {});
        SHIELD_BLOCKING = ModifierHooks.register(SakuraTinker.location("shield_blocking"), ShieldBlockingHook.class, ShieldBlockingHook.AllMerge::new, new ShieldBlockingHook() {});
        CURIO_BUILDER = ModifierHooks.register(SakuraTinker.location("curio_builder"), CurioBuilderHook.class, CurioBuilderHook.AllMerger::new, new CurioBuilderHook() {});
        CURIO_BEHAVIOR = ModifierHooks.register(SakuraTinker.location("curio_behavior"), CurioBehaviorHook.class, CurioBehaviorHook.AllMerger::new, new CurioBehaviorHook() {});
        CURIO_COMBAT = ModifierHooks.register(SakuraTinker.location("curio_combat"), CurioCombatHook.class, CurioCombatHook.AllMerger::new, new CurioCombatHook() {});
        CURIO_ARROW = ModifierHooks.register(SakuraTinker.location("curio_arrow"), CurioArrowHook.class, CurioArrowHook.AllMerger::new, new CurioArrowHook() {});
        ITEM_CLICK_USED = ModifierHooks.register(SakuraTinker.location("item_click_used"), ItemClickUsedHook.class, ItemClickUsedHook.AllMerge::new, new ItemClickUsedHook() {});
        MODIFY_DAMAGE_SOURCE = ModifierHooks.register(SakuraTinker.location("modify_damage_source"), ModifyDamageSourceModifierHook.class, ModifyDamageSourceModifierHook.AllMerger::new, new ModifyDamageSourceModifierHook() {});
        LEFT_CLICK = ModifierHooks.register(SakuraTinker.location("left_click"), LeftClickModifierHook.class, LeftClickModifierHook.AllMerger::new, new LeftClickModifierHook() {});
        CRITICAL_ATTACK = ModifierHooks.register(SakuraTinker.location("critical_attack"), CriticalAttackModifierHook.class, CriticalAttackModifierHook.FirstMerger::new,(tool, entry, attacker, hand, target, sourceSlot, isFullyCharged, isExtraAttack, isCritical)->isCritical);
        CAUSE_DAMAGE = ModifierHooks.register(SakuraTinker.location("cause_damage"), CauseDamageModifierHook.class, CauseDamageModifierHook.AllMerger::new, (tool, modifier, event, attacker, target, baseDamage, currentDamage) -> currentDamage);
        REPLACE_MATERIAL_MODIFIER = ModifierHooks.register(SakuraTinker.location("replace_material_modifier"), ReplaceMaterialModifierHook.class, ReplaceMaterialModifierHook.AllMerger::new, ((context, inputIndex, secondary) -> false));
    }
}
