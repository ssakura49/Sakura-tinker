package com.ssakura49.sakuratinker.common.items;

import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.register.STTags;
import net.minecraft.world.item.ItemStack;

public class SpecialItemCheck {
    public static boolean isOutlineItem(ItemStack stack) {
        return stack.is(STTags.Items.OUTLINE_METAL);
    }

    public static boolean isSpItem(ItemStack stack) {
        return stack.is(STTags.Items.STAR_PARTICLE_ITEM);
    }

    public static boolean isSTItem(ItemStack stack) {
        return stack.is(STTags.Items.SAKURA_TINKER_METAL);
    }

    public static boolean isSpecial(ItemStack stack) {
        return isSpItem(stack);
    }

    public static boolean isNeverBeCleaned(ItemStack stack) {
        return isSpItem(stack);
    }
}
