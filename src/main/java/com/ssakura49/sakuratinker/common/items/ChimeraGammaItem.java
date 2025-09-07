package com.ssakura49.sakuratinker.common.items;

import com.ssakura49.sakuratinker.client.component.LoreHelper;
import com.ssakura49.sakuratinker.client.component.STFont;
import com.ssakura49.sakuratinker.common.items.items.SimpleDescriptiveItem;
import com.ssakura49.sakuratinker.client.component.ChatFormattingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ChimeraGammaItem extends SimpleDescriptiveItem {
    public ChimeraGammaItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag){
        if (Screen.hasShiftDown()){
            tooltip.add(Component.translatable("item.sakuratinker.chimera_gamma.desc").withStyle(ChatFormatting.DARK_AQUA, ChatFormattingContext.SAKURA_ORIGIN()));
        } else {
            tooltip.add(Component.translatable("tooltip.sakuratinker.chimera_gamma_contributors_m").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
            LoreHelper.applyShiftShowMaterialsInfo(tooltip);
        }
        //tooltip.add(Component.translatable("item.sakuratinker.chimera_gamma_contributors1").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @Nullable Font getFont(ItemStack stack, FontContext context) {
                return STFont.INSTANCE;
            }
        });
        super.initializeClient(consumer);
    }
}
