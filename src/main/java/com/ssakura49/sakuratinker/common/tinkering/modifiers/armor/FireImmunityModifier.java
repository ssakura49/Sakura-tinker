package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FireImmunityModifier extends BaseModifier {
    public FireImmunityModifier(){
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerHurt);
    }

    @Override
    public boolean isNoLevels(){
        return true;
    }
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide && entity instanceof Player player && isCorrectSlot) {
            if (player.isOnFire()) {
                player.clearFire();
            }
        }
    }

    private static boolean isDamageOfType(DamageSource source, ResourceKey<DamageType> damageTypeTag) {
        return source.is(damageTypeTag);
    }

    public void onPlayerHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity instanceof Player player) {
            int level = ToolUtil.getModifierArmorAllLevel(player, this);
            if (level > 0) {
                if (isDamageOfType(source, DamageTypes.IN_FIRE) || isDamageOfType(source, DamageTypes.LAVA) || isDamageOfType(source, DamageTypes.ON_FIRE)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
