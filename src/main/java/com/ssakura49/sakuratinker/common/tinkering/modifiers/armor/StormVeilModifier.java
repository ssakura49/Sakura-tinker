package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class StormVeilModifier extends BaseModifier {
    public StormVeilModifier() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, LivingHurtEvent.class, this::onPlayerHurt);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    private void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getPersistentData().putLong("LastHurtTime", player.level().getGameTime());
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!(entity instanceof Player player) || !isCorrectSlot) return;

        long currentTime = level.getGameTime();
        long lastHurtTime = player.getPersistentData().getLong("LastHurtTime");
        long lastTriggerTime = player.getPersistentData().getLong("LastStormVeilTrigger");

        boolean canTrigger = (currentTime - lastHurtTime >= 600) && (currentTime - lastTriggerTime >= 600);

        if (canTrigger) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 4, false, false, true));
            player.getPersistentData().putLong("LastStormVeilTrigger", currentTime);
        }
    }
}
