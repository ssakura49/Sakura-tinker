package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class StrengthenDecompositionModifier extends BaseModifier {
    public StrengthenDecompositionModifier() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @SubscribeEvent
    public void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        ItemStack itemStack = event.getItem();
        ToolStack tool = ToolStack.from(itemStack);
        LivingEntity entity = event.getEntity();
        if (itemStack.getUseAnimation() == UseAnim.EAT && entity instanceof Player player) {
            if (tool.getModifierLevel(this) > 0) {
                var foodProperties = itemStack.getFoodProperties(player);
                if (foodProperties != null) {
                    FoodData foodData = player.getFoodData();
                    int nutrition = foodProperties.getNutrition();
                    float saturationModifier = foodProperties.getSaturationModifier();
                    foodData.eat(nutrition * 2, saturationModifier);
                }
            }
        }
    }
}
