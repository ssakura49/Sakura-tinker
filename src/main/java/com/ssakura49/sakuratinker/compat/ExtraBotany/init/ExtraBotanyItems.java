package com.ssakura49.sakuratinker.compat.ExtraBotany.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.common.tools.stats.PhantomCoreMaterialStats;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class ExtraBotanyItems {
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);
    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

    public static final ItemObject<ToolPartItem> phantom_core = TINKER_ITEMS.register("phantom_core", () -> new ToolPartItem(PartItem, PhantomCoreMaterialStats.ID));
    public static final ItemObject<ModifiableSwordItem> first_fractal = TINKER_ITEMS.register("first_fractal", () -> new com.ssakura49.sakuratinker.common.tools.item.FirstFractal(ToolItem));
    public static final CastItemObject phantomCoreCast = TINKER_ITEMS.registerCast("phantom_core", CastItem);


}
