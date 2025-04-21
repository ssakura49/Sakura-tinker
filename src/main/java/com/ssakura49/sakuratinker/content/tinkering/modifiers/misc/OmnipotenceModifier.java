package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class OmnipotenceModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    public OmnipotenceModifier() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (source.getEntity() instanceof Player player) {
            ItemStack item = player.getMainHandItem();
            if (this.getLevel(item) > 0) {
                target.setHealth(0);
                target.die(source);
            }
        }
    }
    private int getLevel(ItemStack stack) {
        if (stack.getItem() instanceof IModifiable) {
            IToolStackView tool = ToolStack.from(stack);
            return tool.getModifierLevel(this);
        }
        return 0;
    }
}

