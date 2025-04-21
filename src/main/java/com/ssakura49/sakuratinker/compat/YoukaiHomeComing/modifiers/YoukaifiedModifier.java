package com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class YoukaifiedModifier extends BaseModifier {
    public static MobEffect getYoukaifiedEffect() {
        ResourceLocation effectId = new ResourceLocation("youkaishomecoming", "youkaified");
        return ForgeRegistries.MOB_EFFECTS.getValue(effectId);
    }
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (isArmorSlot(context)) {
            updateEffects(context.getEntity(), true);
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (isArmorSlot(context)) {
            updateEffects(context.getEntity(), false);
        }
    }

    private boolean isArmorSlot(EquipmentChangeContext context) {
        return context.getChangedSlot().getType() == EquipmentSlot.Type.ARMOR;
    }
    private void updateEffects(LivingEntity entity, boolean isEquip) {
        if (!(entity instanceof Player player)) return;
        MobEffect youkaifiedEffect = YoukaifiedModifier.getYoukaifiedEffect();
        if (youkaifiedEffect == null || !player.hasEffect(youkaifiedEffect)) return;
        int totalLevel = calculateTotalLevel(player);
        if (isEquip) {
            applyEffects(player, totalLevel);
        } else {
            removeEffects(player);
            int newTotalLevel = calculateTotalLevel(player);
            if (newTotalLevel > 0) {
                applyEffects(player, newTotalLevel);
            }
        }
    }
    private int calculateTotalLevel(Player player) {
        int totalLevel = 0;
        for (ItemStack armor : player.getArmorSlots()) {
            IToolStackView armorTool = ToolStack.from(armor);
            totalLevel += armorTool.getModifiers().getLevel(this.getId());
        }
        return totalLevel;
    }
    private void applyEffects(Player player, int totalLevel) {
        int effectLevel = Math.max(1, totalLevel / 2);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, effectLevel - 2, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, Integer.MAX_VALUE, effectLevel - 1, false, false, true));
    }
    private void removeEffects(Player player) {
        player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        player.removeEffect(MobEffects.ABSORPTION);
    }

}
