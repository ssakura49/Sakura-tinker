package com.ssakura49.sakuratinker.library.logic.context;

import com.ssakura49.sakuratinker.client.component.ChatFormattingContext;
import net.minecraft.world.item.Rarity;

public class ItemRarityContext {
    private static Rarity DREAM;
    private static Rarity ORIGIN;
    public static Rarity DREAM() {
        if (DREAM == null)
            DREAM = Rarity.create("DREAM", ChatFormattingContext.ST_DP());
        return DREAM;
    }
    public static Rarity ORIGIN() {
        if (ORIGIN == null)
            ORIGIN = Rarity.create("ORIGIN", ChatFormattingContext.SAKURA_ORIGIN());
        return ORIGIN;
    }
}
