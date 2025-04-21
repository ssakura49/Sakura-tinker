package com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BlurredModifier extends BaseModifier {
    private static final int COOLDOWN = 300;
    public static MobEffect getUnconsciousEffect() {
        ResourceLocation effectId = new ResourceLocation("youkaishomecoming", "unconscious");
        return ForgeRegistries.MOB_EFFECTS.getValue(effectId);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack item) {
        if (!(entity instanceof Player player)) {
            return;
        }
        MobEffect unconscious = getUnconsciousEffect();
        if (unconscious == null) {
            return;
        }
        if (player.getCooldowns().isOnCooldown(item.getItem())) {
            return;
        }
        if (!player.hasEffect(unconscious) && isCorrectSlot) {
            player.addEffect(new MobEffectInstance(unconscious, 320, 0, false, false));
            player.getCooldowns().addCooldown(item.getItem(), COOLDOWN);
        }
    }

}
