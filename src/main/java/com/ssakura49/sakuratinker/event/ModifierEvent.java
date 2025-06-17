package com.ssakura49.sakuratinker.event;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.armor.AbsorptionModifier;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID)
public class ModifierEvent {
    public ModifierEvent(){
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || player.level().isClientSide) return;
        if (player.tickCount % STConfig.COMMON.tickInterval.get() != 0) return;
        float regen =  STConfig.COMMON.absorptionPerTick.get().floatValue();
        CompoundTag persistentData = player.getPersistentData();
        float modAbsorption = persistentData.getFloat(AbsorptionModifier.ABSORPTION_KEY);
        float totalAbsorption = player.getAbsorptionAmount();
        int totalLevel = AbsorptionModifier.getTotalModifierLevel(player,STModifiers.Absorption.get());
        if (totalLevel > 0) {
            float maxAbsorption = player.getMaxHealth() * totalLevel;
            float absorptionToAdd = Math.min(regen, maxAbsorption - modAbsorption);
            if (absorptionToAdd > 0) {
                modAbsorption += absorptionToAdd;
                persistentData.putFloat(AbsorptionModifier.ABSORPTION_KEY, modAbsorption);
                player.setAbsorptionAmount(totalAbsorption + absorptionToAdd);
            }
        } else if (modAbsorption > 0) {
            player.setAbsorptionAmount(Math.max(0, totalAbsorption - modAbsorption));
            persistentData.putFloat(AbsorptionModifier.ABSORPTION_KEY, 0);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (!(source.getEntity() instanceof Player player)) return;
        for (IToolStackView tool : ToolUtil.getAllEquippedToolStacks(player)) {
            if (tool.getModifierLevel(STModifiers.Omnipotence.get()) > 0) {
                target.setHealth(0.0F);
                target.dropAllDeathLoot(source);
                EntityUtil.die(target, source);
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        int level = ToolUtil.getSingleModifierArmorAllLevel(player, STModifiers.Null_Almighty.get());
//        int level = NullAlmightyModifier.getNullAlmightyLevel(player);
        if (level >= 4) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        int level = ToolUtil.getSingleModifierArmorAllLevel(player, STModifiers.Null_Almighty.get());
//        int level = NullAlmightyModifier.getNullAlmightyLevel(player);
        DamageSource source = event.getSource();
        if (level >= 4 && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            event.setCanceled(true);
            player.hurtTime = 0;
            return;
        }
        float reduction = 0.25f * level;
        if (reduction > 0) {
            float original = event.getAmount();
            float reduced = original * (1 - Math.min(reduction, 1.0f));
            event.setAmount(reduced);
        }
    }
}
