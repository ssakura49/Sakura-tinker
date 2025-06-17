package com.ssakura49.sakuratinker.client.renderer;

import com.ssakura49.sakuratinker.client.core.ClientTickHandler;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.util.Mth;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ColorHandler {
    public interface BlockHandlerConsumer {
        void register(BlockColor handler, Block... blocks);
    }

    public interface ItemHandlerConsumer {
        void register(ItemColor handler, ItemLike... items);
    }

    public static void submitBlocks(BlockHandlerConsumer blocks) {
        BlockColor vineColor = (state, world, pos, tint) -> world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor();
    }

    public static void submitItems(ItemHandlerConsumer items) {
//        items.register((s, t) -> t == 0 ? Mth.hsvToRgb(ClientTickHandler.ticksInGame * 2 % 360 / 360F, 0.25F, 1.0F) : -1,
//                STItems.terra_prisma.get());
    }

    private ColorHandler() {}
}
