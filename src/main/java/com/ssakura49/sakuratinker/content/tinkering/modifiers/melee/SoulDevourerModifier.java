package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class SoulDevourerModifier extends BaseModifier {
    private static final ResourceLocation DAMAGE_BONUS_KEY = new ResourceLocation(SakuraTinker.MODID,  "soul_devourer_damage_bonus");
    private static final float BONUS_PER_KILL = 0.0001f;

    @Override
    public void onModifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource source, float amount, boolean isDirectDamage) {
        if (entity.getHealth() <= amount && source.getEntity() instanceof Player) {
            float currentBonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY);
            tool.getPersistentData().putFloat(DAMAGE_BONUS_KEY, currentBonus + BONUS_PER_KILL);
        }
    }

    @Override
    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        float bonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY);
        return baseDamage * (1 + bonus);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        float bonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY) * 100;
        tooltip.add(Component.translatable("modifier.sakuratinker.soul_devourer.tooltip", String.format("%.2f", bonus)));
    }
}
