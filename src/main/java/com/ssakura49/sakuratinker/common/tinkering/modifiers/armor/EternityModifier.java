package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mod.EventBusSubscriber(modid = "sakuratinker")
public class EternityModifier extends BaseModifier {

    public EternityModifier()   {
        super();
    }

    private boolean isEquipped(Player player, int index) {
        return index >= 0 && index < 4;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player) {
            if (isEquipped(player, index)) {
                if (!player.hasEffect(STEffects.IMMORTALITY.get())) {
                    player.addEffect(new MobEffectInstance(STEffects.IMMORTALITY.get(), 200, 0, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(STEffects.IMMORTALITY.get())) {
                float remainingHealth = player.getHealth() - event.getAmount();
                if (remainingHealth < 1) {
                    event.setAmount(player.getHealth() - 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(STEffects.IMMORTALITY.get())) {
                if (!event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    event.setCanceled(true);
                    player.setHealth(1);
                    player.removeEffect(STEffects.IMMORTALITY.get());
                }
            }
        }
    }
    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(STEffects.IMMORTALITY.get())) {
            event.setCanceled(true);
        }
    }
}
