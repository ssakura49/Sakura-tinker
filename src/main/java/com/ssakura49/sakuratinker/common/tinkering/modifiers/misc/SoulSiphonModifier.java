package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class SoulSiphonModifier extends BaseModifier {
    private static final ResourceLocation SOUL_ENERGY_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "soul_energy");
    private static final int MAX_SOUL_ENERGY = 100;

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        Component component = Component
                .translatable("tooltip.sakuratinker.soul_energy_storage")
                .append(":")
                .append(Component.literal(String.valueOf(getSoulEnergy(tool))));
        list.add(component);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private int getSoulEnergy(IToolStackView tool) {
        return tool.getPersistentData().getInt(SOUL_ENERGY_KEY);
    }

    private void setSoulEnergy(IToolStackView tool, int energy) {
        tool.getPersistentData().putInt(SOUL_ENERGY_KEY, Math.min(energy, MAX_SOUL_ENERGY));
    }

    public void addSoulEnergy(IToolStackView tool, int amount) {
        int currentEnergy = getSoulEnergy(tool);
        setSoulEnergy(tool, currentEnergy + amount);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.isAlive()) {
            addSoulEnergy(tool, 10);
        }
        int soulEnergy = getSoulEnergy(tool);
        if (soulEnergy >= MAX_SOUL_ENERGY) {
            if (target != null) {
                float maxHealth = target.getMaxHealth();
                float bonusDamage = maxHealth * 0.2f;
                target.hurt(new DamageSource(target.getCommandSenderWorld().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD)), damageDealt + bonusDamage);
                setSoulEnergy(tool, 0);
            }
        }
    }
}
