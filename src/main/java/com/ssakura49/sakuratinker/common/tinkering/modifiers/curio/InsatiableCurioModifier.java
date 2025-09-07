package com.ssakura49.sakuratinker.common.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.logic.context.ProjectileImpactContent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.stats.ToolType;

import java.util.List;

public class InsatiableCurioModifier extends CurioModifier {
    public static final ToolType[] TOOL_TYPE = new ToolType[]{ToolType.MELEE, ToolType.RANGED};;
    private static final int DURATION = 200;
    private static final int MAX_LEVEL = 7;

    public InsatiableCurioModifier() {}

    /** 根据当前效果计算伤害加成 */
    private static float getBonus(LivingEntity entity, int modifierLevel, ToolType type) {
        int effectLevel = TinkerEffect.getLevel(entity, TinkerModifiers.insatiableEffect.get(type));
        return (modifierLevel * effectLevel) / 4.0f;
    }

    /** 应用叠加效果 */
    private static void applyEffect(LivingEntity entity, int amount, ToolType type) {
        TinkerEffect effect = TinkerModifiers.insatiableEffect.get(type);
        int current = TinkerEffect.getAmplifier(entity, effect);
        effect.apply(entity, DURATION, Math.min(current + amount, MAX_LEVEL), true);
    }

    /** 攻击远程命中实体后叠加 Combo 效果 */
    @Override
    public void onCurioArrowHit(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, ProjectileImpactContent data) {
        if (entry.getLevel() > 0 && !shooter.level().isClientSide()) {
            applyEffect(shooter, 1, ToolType.RANGED);
        }
    }

    /** 造成伤害后触发效果叠加（补充处理） */
    @Override
    public void onCurioDamageTargetPost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
        if (entry.getLevel() > 0 && event.getAmount() > 0 && !attacker.level().isClientSide()) {
            applyEffect(attacker, 1, ToolType.MELEE);
        }
    }

    /** 处理伤害加成：在造成伤害之前提升数值 */
    @Override
    public void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
        float bonus = getBonus(attacker, entry.getLevel(), ToolType.MELEE);
        if (bonus > 0) {
            event.setAmount(event.getAmount() + bonus);
        }
    }

    /** 远程计算伤害时添加额外加成（用于弓箭伤害） */
    @Override
    public void onCurioCalculateDamage(IToolStackView curio, ModifierEntry entry, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target) {
        float bonus = getBonus(attacker, entry.getLevel(), ToolType.MELEE);
        if (bonus > 0 && event instanceof LivingCalculateAbsEvent.Armor armor) {
            armor.setDamageAfterArmor(armor.getDamageAfterArmor() + bonus);
        }
    }

    /** 在 Tooltip 显示当前加成 */
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int level = modifierEntry.getLevel();
        ToolType type = ToolType.from(tool.getItem(), TOOL_TYPE);
        float bonus = level * 2f;
        if (player != null && tooltipKey == TooltipKey.SHIFT) {
            bonus = getBonus(player, level,type);
        }
        if (bonus > 0f) {
            TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.ATTACK_DAMAGE), bonus, tooltip);
        }
    }
}
