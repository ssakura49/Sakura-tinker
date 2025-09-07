package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.utils.CommonRGBUtil;
import com.ssakura49.sakuratinker.utils.component.DynamicComponentUtil;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class SoulDevourerModifier extends CurioModifier {
    private static final ResourceLocation DAMAGE_BONUS_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "soul_devourer_damage_bonus");
    private static final float BONUS_PER_KILL = 0.0001f;

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void onKillLivingTarget(IToolStackView tool, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
        if (event.getSource().getEntity() == attacker) {
            float currentBonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY);
            tool.getPersistentData().putFloat(DAMAGE_BONUS_KEY, currentBonus + BONUS_PER_KILL * entry.getLevel());
        }
    }

    @Override
    public void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
        if (event.getSource().getEntity() == attacker) {
            float currentBonus = curio.getPersistentData().getFloat(DAMAGE_BONUS_KEY);
            curio.getPersistentData().putFloat(DAMAGE_BONUS_KEY, currentBonus + BONUS_PER_KILL * entry.getLevel());
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        float bonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY);
        return actualDamage * (1.0f + bonus);
    }

    @Override
    public void onModifierRemoved(IToolStackView tool) {
        tool.getPersistentData().remove(DAMAGE_BONUS_KEY);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        float bonus = tool.getPersistentData().getFloat(DAMAGE_BONUS_KEY) * 100;
        int level = modifierEntry.getLevel();
        String text = I18n.get("modifier.sakuratinker.soul_devourer.tooltip") + ": " + String.format("%.2f", bonus) + "("+ level + ")";

        tooltip.add(DynamicComponentUtil.WaveColorText.getColorfulText(
                text,
                null,
                CommonRGBUtil.darkBlue.getRGB(),
                40,
                1000,
                true
        ));
    }
}
