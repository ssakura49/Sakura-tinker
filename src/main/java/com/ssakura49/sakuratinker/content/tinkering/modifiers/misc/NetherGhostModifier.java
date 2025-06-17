package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class NetherGhostModifier extends BaseModifier {
    public NetherGhostModifier() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDeath);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    private void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide) {
            DamageSource source = event.getSource();
            if (source.getEntity() instanceof Player player) {
                ItemStack weapon = player.getMainHandItem();
                ToolStack tool = ToolStack.from(weapon);
                int level = tool.getModifierLevel(this.getId());
                if (level > 0) {
                    FoodData foodData = player.getFoodData();
                    foodData.eat(2, 0.5F);
                    int newFood = foodData.getFoodLevel();
                    if (newFood == 20) {
                        player.heal(2.0F);
                    }
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (isSelected && !level.isClientSide && holder instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 40, 5, false, false));
        }
    }
}
