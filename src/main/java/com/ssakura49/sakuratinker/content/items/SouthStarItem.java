package com.ssakura49.sakuratinker.content.items;

import com.ssakura49.sakuratinker.utils.CommonRGBUtil;
import com.ssakura49.sakuratinker.utils.component.DynamicComponentUtil;
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

public class SouthStarItem extends Item {
    public SouthStarItem(Properties properties){
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip ,flag);
        if (Screen.hasShiftDown()) {
            tooltip.add(DynamicComponentUtil.BreathColorfulText.getColorfulText(
                    "tooltip.sakuratinker.south_star.line1",
                    null,
                    CommonRGBUtil.yellow.getRGB(),
                    40,
                    1000,
                    true
            ));
            tooltip.add(DynamicComponentUtil.BreathColorfulText.getColorfulText(
                    "tooltip.sakuratinker.south_star.line2",
                    null,
                    CommonRGBUtil.yellow.getRGB(),
                    40,
                    1000,
                    true
            ));
        } else {
            tooltip.add(Component.translatable("tooltip.sakuratinker.hold_shift")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }
}
