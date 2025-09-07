package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class HydraWillModifier extends BaseModifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> HYDRA_WILL_LEVEL = TConstruct.createKey("hydra_will");
    private static final ResourceLocation DEATH_IMMUNITY_COOLDOWN = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "death_immunity_cooldown");
    private static final int COOLDOWN_TICKS = 2400;
    private static final float RESURRECT_HEALTH_PERCENTAGE = 0.2f;
    private static final int INVULNERABILITY_TICKS = 60;

    public HydraWillModifier() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDeath);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(HYDRA_WILL_LEVEL, false, null));
    }

    @Override
    public void onModifierRemoved(IToolStackView tool) {
        tool.getPersistentData().remove(DEATH_IMMUNITY_COOLDOWN);
    }

    private void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(holder -> {
            int level = holder.get(HYDRA_WILL_LEVEL, 0);
            if (level > 0 && entity instanceof Player player) {
                for (ItemStack equipment : player.getInventory().armor) {
                    if (equipment.getItem() instanceof ModifiableArmorItem) {
                        ToolStack tool = ToolStack.from(equipment);
                        ModDataNBT toolData = tool.getPersistentData();
                        int currentCooldown = toolData.getInt(DEATH_IMMUNITY_COOLDOWN);
                        if (currentCooldown == 0 && tool.getModifierLevel(this) > 0) {
                            toolData.putInt(DEATH_IMMUNITY_COOLDOWN, COOLDOWN_TICKS);
                            event.setCanceled(true);
                            player.deathTime = -2;
                            player.fallDistance = 0;
                            player.setHealth(player.getMaxHealth() * RESURRECT_HEALTH_PERCENTAGE);
                            player.invulnerableTime = INVULNERABILITY_TICKS;
                            player.sendSystemMessage(
                                    Component.translatable("message.sakuratinker.death_immunity")
                                            .withStyle(ChatFormatting.AQUA)
                            );
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder instanceof Player) {
            ModDataNBT modDataNBT = tool.getPersistentData();
            int currentCooldown = modDataNBT.getInt(DEATH_IMMUNITY_COOLDOWN);
            if (currentCooldown > 0) {
                modDataNBT.putInt(DEATH_IMMUNITY_COOLDOWN, currentCooldown - 1);
            }
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry,
                           @Nullable Player player, List<Component> tooltips,
                           TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ModDataNBT modDataNBT = tool.getPersistentData();
            int currentCooldown = modDataNBT.getInt(DEATH_IMMUNITY_COOLDOWN);

            if (currentCooldown > 0) {
                tooltips.add(applyStyle(
                        Component.translatable("modifier.sakuratinker.tooltip.dead_immunity_cd")
                                .append(": ")
                                .append(String.valueOf(currentCooldown / 20)).withStyle(ChatFormatting.AQUA)
                ));
            } else {
                tooltips.add(applyStyle(
                        Component.translatable("modifier.sakuratinker.tooltip.dead_immunity_done").withStyle(ChatFormatting.AQUA)
                ));
            }
        }
    }


}
