package com.ssakura49.sakuratinker.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitherHeartItem extends Item {
    public WitherHeartItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip ,flag);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.sakuratinker.wither_heart")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.sakuratinker.item_source")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }


    }
}
