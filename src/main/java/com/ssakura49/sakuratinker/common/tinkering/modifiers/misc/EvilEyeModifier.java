package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class EvilEyeModifier extends BaseModifier {
    public EvilEyeModifier() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @SubscribeEvent
    public void onKill(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (ToolStack.isInitialized(mainHand)) {
                ToolStack toolStack = ToolStack.from(mainHand);
                if (toolStack.getModifierLevel(this) > 0) {
                    healPlayer(player);
                }
            }
        }
    }
    private void healPlayer(Player player) {
        float healAmount = player.getMaxHealth() * 0.05f;
        player.heal(healAmount);
    }
}
