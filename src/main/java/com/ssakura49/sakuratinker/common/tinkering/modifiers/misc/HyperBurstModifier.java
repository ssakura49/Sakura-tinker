package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.data.ModDataUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class HyperBurstModifier extends BaseModifier {
    public static final ResourceLocation CRIT_CHARGE_KEY = ResourceLocation.fromNamespaceAndPath("sakuratinker", "crit_charge");
    public static final ResourceLocation LAST_UPDATE_KEY = ResourceLocation.fromNamespaceAndPath("sakuratinker", "crit_last_update");

    public static final float BASE_CHANCE = 0.10f;       // 10%
    private static final float CHANCE_GROWTH_PER_SECOND = 0.10f; // 10%/秒
    private static final float MAX_CHANCE_PER_LEVEL = 1.0f; // 每级增加100%暴击率上限
    private static final float BASE_CRIT_DAMAGE = 4.0f;  // 基础暴击伤害400%

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level,
                                LivingEntity holder, int slot, boolean isSelected, boolean isCorrect, ItemStack stack) {
        if (level.isClientSide() || !isSelected) return;

        ModDataNBT data = tool.getPersistentData();
        long currentTime = level.getGameTime();
        long lastUpdate = ModDataUtil.getLong(data, LAST_UPDATE_KEY);

        // 每20 ticks（1秒）更新一次
        if (currentTime - lastUpdate >= 20) {
            float currentCharge = data.getFloat(CRIT_CHARGE_KEY);
            float maxCharge = MAX_CHANCE_PER_LEVEL * modifier.getLevel();

            // 允许充能超过最大暴击率
            currentCharge = Math.min(currentCharge + CHANCE_GROWTH_PER_SECOND, maxCharge);
            data.putFloat(CRIT_CHARGE_KEY, currentCharge);
            ModDataUtil.putLong(data, LAST_UPDATE_KEY, currentTime);
        }
    }

//    @Override
//    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier,
//                                   EquipmentContext context, EquipmentSlot slot,
//                                   DamageSource source, float amount, boolean isDirect) {
//        // 受击时重置暴击充能为基础值
//        tool.getPersistentData().putFloat(CRIT_CHARGE_KEY, BASE_CHANCE);
//    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                     float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();

        if (attacker.level().isClientSide()) return actualDamage;

        ModDataNBT tag = tool.getPersistentData();
        float critCharge = tag.getFloat(CRIT_CHARGE_KEY);
        int level = modifier.getLevel();

        // 暴击判定（最多100%概率）
        boolean isCrit = attacker.getRandom().nextFloat() < Math.min(critCharge, 1.0f);

        if (isCrit) {
            // 计算额外暴击率（超过100%的部分）
            float extraCritRate = Math.max(0, critCharge - 1.0f);

            // 伤害计算：基础400% × (1 + (额外暴击率 × 等级))
            float damageMultiplier = BASE_CRIT_DAMAGE * (1 + extraCritRate * level);
            float finalDamage = actualDamage * damageMultiplier;

            // 粒子效果（根据额外暴击率）
            if (extraCritRate > 0.5f) {
                spawnBigCritParticles(target, extraCritRate);
            } else {
                spawnCritParticles(target);
            }

            // 客户端消息
            if (attacker instanceof Player player) {
                float totalMultiplier = damageMultiplier;
                player.displayClientMessage(
                        Component.literal(String.format("暴击! %.1f倍伤害", totalMultiplier))
                                .withStyle(ChatFormatting.GOLD), true);
            }

            return finalDamage;
        }

        return actualDamage;
    }

    // 粒子效果保持不变
    private void spawnCritParticles(LivingEntity target) {
        if (target.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = target.position();
            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    pos.x,
                    pos.y + target.getBbHeight() * 0.5,
                    pos.z,
                    20,
                    target.getBbWidth() * 0.5,
                    0.5,
                    target.getBbWidth() * 0.5,
                    0.3
            );
        }
    }

    private void spawnBigCritParticles(LivingEntity target, float extraCritRate) {
        if (target.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = target.position();
            int extraParticles = (int)(extraCritRate * 10);
            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    pos.x,
                    pos.y + target.getBbHeight() * 0.5,
                    pos.z,
                    15 + extraParticles,
                    target.getBbWidth() * 0.7,
                    0.7,
                    target.getBbWidth() * 0.7,
                    0.5
            );
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player,
                           List<Component> tooltip, TooltipKey key, TooltipFlag flag) {
        ModDataNBT tag = tool.getPersistentData();
        float currentCharge = tag.getFloat(CRIT_CHARGE_KEY);
        int level = modifier.getLevel();
        float maxCharge = MAX_CHANCE_PER_LEVEL * level;
        float effectiveCritRate = Math.min(currentCharge, 1.0f);
        float extraCritRate = Math.max(0, currentCharge - 1.0f);
        float damageBonus = extraCritRate * level * 100;
        tooltip.add(Component.literal(String.format("暴击概率: %.1f%%", effectiveCritRate * 100)).withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal(String.format("基础暴击伤害: %.0f%%", BASE_CRIT_DAMAGE * 100)).withStyle(ChatFormatting.RED));
        if (extraCritRate > 0) {
            tooltip.add(Component.literal(String.format("额外暴击率: %.1f%% - +%.0f%% 伤害增幅", extraCritRate * 100, damageBonus)).withStyle(ChatFormatting.YELLOW));
        }
        tooltip.add(Component.literal(String.format("最大暴击率: %.0f%%", maxCharge * 100)).withStyle(ChatFormatting.DARK_GREEN));
    }
}