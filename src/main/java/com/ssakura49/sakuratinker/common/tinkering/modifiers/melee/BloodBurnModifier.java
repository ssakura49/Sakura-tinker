package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.events.AttackSpeedModifyEvent;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class BloodBurnModifier extends BaseModifier {
    private static final String BLOOD_BURN_KEY_ID = "key.sakuratinker.blood_burn";

    @Override
    public void onKeyPress(IToolStackView tool, ModifierEntry modifier, Player player, String key) {
        if (player.level().isClientSide) return;
        if (!BLOOD_BURN_KEY_ID.equals(key)) return;
        MobEffect bloodBurn = STEffects.BLOOD_BURN.get();
        if (player.hasEffect(bloodBurn)) {
            player.removeEffect(bloodBurn);
            player.displayClientMessage(Component.literal("§c血燃已关闭"), true);
        } else {
            int level = modifier.getLevel() - 1;
            player.addEffect(new MobEffectInstance(bloodBurn, Integer.MAX_VALUE, level, false, true, true));
            player.displayClientMessage(Component.literal("§c血燃已开启"), true);
        }
    }

    @Override
    public @Nullable String getKeyId(IToolStackView tool, ModifierEntry modifier) {
        return BLOOD_BURN_KEY_ID;
    }

    @Override
    public void modifyAttackCooldown(IToolStackView tool, ModifierEntry entry, Player player, AttackSpeedModifyEvent event) {
        MobEffectInstance effect = player.getEffect(STEffects.BLOOD_BURN.get());
        if (effect != null && player.hasEffect(STEffects.BLOOD_BURN.get())) {
            int level = effect.getAmplifier() + 1;
            float bonus = 1.0f + 0.15f * level;
            event.mulCooldownModifier(bonus);
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity entity = context.getAttacker();
        MobEffectInstance effect = entity.getEffect(STEffects.BLOOD_BURN.get());
        if (effect != null && entity instanceof Player player) {
            int level = effect.getAmplifier() + 1;
            float bonus = 0.25f * level;
            return damage * (1 + bonus);
        }
        return damage;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player == null) return;
        String keyName = getKeyDisplay(tool, entry);
        float damageBonus = 25f * entry.getLevel();
        float speedBonus = 15f * entry.getLevel();
        tooltip.add(Component.literal("按 [")
                .append(Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .append("] 开/关 燃血BUFF")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(String.format("§6+%.0f%% 近战伤害", damageBonus))
                .withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal(String.format("§6+%.0f%% 攻击速度", speedBonus))
                .withStyle(ChatFormatting.GOLD));
    }
}
