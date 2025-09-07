package com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers;

import com.Polarice3.Goety.utils.ItemHelper;
import com.ssakura49.sakuratinker.compat.Goety.api.SoulDiscountProvider;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.helper.ApollyonReflectHelper;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.init.GRModifiers;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.component.RomanNumberalUtil;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;

public class ApocalyptiumModifier extends BaseModifier implements SoulDiscountProvider {

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @org.jetbrains.annotations.Nullable LivingEntity holder) {
        return Math.min(amount, 20);
    }


    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level,
                                        LivingEntity holder, int itemSlot, boolean isSelected,
                                        boolean isCorrectSlot, ItemStack itemStack) {
        if (level.isClientSide && holder == null) return;
        ItemHelper.repairTick(itemStack, holder, isCorrectSlot);
        removeNegativeEffects(holder);
        if (isCorrectSlot && itemStack.getEquipmentSlot() == EquipmentSlot.CHEST && holder.tickCount % 5 == 0) {
            holder.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false));
            holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
        }

        if (ToolUtil.hasModifierInAllArmor(holder, this) && holder.tickCount % 20 == 0 && holder.getHealth() > 0.0F) {
            holder.setHealth(holder.getHealth() + 2.0F);
            ForgeEventFactory.onLivingHeal(holder, 2.0F);
        }
    }

    private void removeNegativeEffects(LivingEntity entity) {
        entity.removeEffect(MobEffects.BLINDNESS);
        entity.removeEffect(MobEffects.DARKNESS);
        entity.removeEffect(MobEffects.CONFUSION); // nausea
    }

    public static void onLivingHurtLegs(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack legsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (!ToolUtil.checkTool(legsStack)) return;
        ToolStack legs = ToolStack.from(legsStack);
        if (!legs.isBroken() && legs.getModifierLevel(GRModifiers.Apocalyptium.get()) > 0 && entity.getRandom().nextBoolean()) {
            if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
                event.setCanceled(true);
                projectile.setDeltaMovement(projectile.getDeltaMovement().scale(2.0));
            }
        }
    }

    public static void onLivingHurtFeet(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack feetStack = entity.getItemBySlot(EquipmentSlot.FEET);
        if (!ToolUtil.checkTool(feetStack)) return;
        ToolStack feet = ToolStack.from(feetStack);
        if (!feet.isBroken() && feet.getModifierLevel(GRModifiers.Apocalyptium.get()) > 0 ) {
            if (event.getSource().is(DamageTypeTags.IS_FALL)) {
                event.setCanceled(true);
            }
        }
    }

    public static void damageSourceBlock(LivingHurtEvent event){
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
            return;
        }
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            event.setCanceled(true);
            return;
        }
        if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            event.setCanceled(true);
        }
    }

    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (!ToolUtil.checkTool(stack)) return;
        ToolStack tool = ToolStack.from(stack);
        if (!tool.isBroken() && tool.getModifierLevel(GRModifiers.Apocalyptium.get()) > 0) {
            if (entity.getRandom().nextFloat() < 0.15F) {
                event.setCanceled(true);
                entity.setHealth(1F);
                entity.heal(7.0F);
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 4));
                entity.level().broadcastEntityEvent(entity, ApollyonReflectHelper.getReviveEvent());
            }
        }
    }


    @Override
    public int getSoulDiscount(LivingEntity living) {
        int level = Math.min(8, ToolUtil.getModifierArmorAllLevel(living, GRModifiers.Apocalyptium.get()));
        return 10 * level;
    }
//    public static int getSoulDiscount(LivingEntity entity) {
//        int level = Math.min(8, ToolUtil.getSingleModifierArmorAllLevel(entity, GRModifiers.Apocalyptium.get()));
//        return 10 * level;
//    }

    public static int getApocalyptiumTitleId() {
        return (int) (System.currentTimeMillis() / 20000 % 3); // 每20
    }


    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player,
                           List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int level = Math.min(8, ToolUtil.getModifierArmorAllLevel(player, GRModifiers.Apocalyptium.get()));
        if (level <= 0) return;
        tooltip.add(Component.literal("灵魂消耗降低: ")
                .append(Component.literal((10 * level) + "%").withStyle(ChatFormatting.GREEN)));
        if (player != null && ToolUtil.hasModifierInAllArmor(player, this)) {
            tooltip.add(Component.literal("完全免疫:").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            tooltip.add(Component.literal(" - 火焰").withStyle(ChatFormatting.RED));
            tooltip.add(Component.literal(" - 爆炸").withStyle(ChatFormatting.DARK_RED));
            tooltip.add(Component.literal(" - 魔法").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.literal(" - 黑暗、失明").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(" - 反胃").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        int titleId = ApocalyptiumModifier.getApocalyptiumTitleId();
        String key;
        TextColor color;
        switch (titleId) {
            case 0 -> {
                key = "modifier.sakuratinker.apocalyptium.title.immortal"; // 不灭重生
                color = TextColor.fromRgb(0xFF4444); // 红色
            }
            case 1 -> {
                key = "modifier.sakuratinker.apocalyptium.title.horror"; // 可怖之物
                color = TextColor.fromRgb(0x8844FF); // 紫色
            }
            case 2 -> {
                key = "modifier.sakuratinker.apocalyptium.title.glory"; // 荣耀之名
                color = TextColor.fromRgb(0xFFD700); // 金色
            }
            default -> {
                key = this.getTranslationKey();
                color = TextColor.fromRgb(0xFFFFFF); // 白色
            }
        }
        return Component.translatable(key)
                .append(Component.literal(" " + RomanNumberalUtil.toRoman(level)))
                .withStyle(style -> style.withColor(color).withItalic(false));
    }

}
