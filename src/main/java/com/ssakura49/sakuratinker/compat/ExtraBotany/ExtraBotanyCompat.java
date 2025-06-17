package com.ssakura49.sakuratinker.compat.ExtraBotany;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.Botania.modifier.FirstFractal;
import com.ssakura49.sakuratinker.compat.ExtraBotany.modifiers.BodyModifier;
import com.ssakura49.sakuratinker.content.tools.stats.PhantomCoreMaterialStats;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;

public class ExtraBotanyCompat {
    public static ModifierDeferredRegister EXBOT_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);
    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

    public static final ItemObject<ToolPartItem> phantom_core = TINKER_ITEMS.register("phantom_core", () -> new ToolPartItem(PartItem, PhantomCoreMaterialStats.ID));
    public static final ItemObject<ModifiableSwordItem> first_fractal = TINKER_ITEMS.register("first_fractal", () -> new com.ssakura49.sakuratinker.content.tools.item.FirstFractal(ToolItem));
    public static final ItemObject<PartCastItem> phantom_core_cast = TINKER_ITEMS.register("phantom_core_cast", () -> new PartCastItem(CastItem, phantom_core));
    public static final ItemObject<PartCastItem> phantom_core_red_sand_cast = TINKER_ITEMS.register("phantom_core_red_sand_cast", () -> new PartCastItem(CastItem, phantom_core));
    public static final ItemObject<PartCastItem> phantom_core_sand_cast = TINKER_ITEMS.register("phantom_core_sand_cast", () -> new PartCastItem(CastItem, phantom_core));

    public static StaticModifier<BodyModifier> Body;
    public static StaticModifier<FirstFractal> FirstFractal;

    static {
        Body = EXBOT_MODIFIERS.register("body", BodyModifier::new);
        FirstFractal = EXBOT_MODIFIERS.register("first_fractal", FirstFractal::new);
    }
}
