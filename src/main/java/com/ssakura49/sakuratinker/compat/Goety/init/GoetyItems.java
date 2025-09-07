package com.ssakura49.sakuratinker.compat.Goety.init;

import com.Polarice3.Goety.api.magic.SpellType;
import com.ssakura49.sakuratinker.common.tools.stats.SoulGathererMaterialStats;
import com.ssakura49.sakuratinker.compat.Goety.item.TinkerWandItem;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableWandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;

public class GoetyItems {
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);
    public static final ItemObject<ToolPartItem> soul_gatherer = TINKER_ITEMS.register("soul_gatherer", () -> new ToolPartItem(PartItem, SoulGathererMaterialStats.ID));
    public static final ItemObject<ModifiableWandItem> tinker_wand = TINKER_ITEMS.register("tinker_wand", () -> new TinkerWandItem(ToolItem, SpellType.NONE));
    public static final CastItemObject soulGathererCast = TINKER_ITEMS.registerCast("soul_gatherer", CastItem);

    public static void register(IEventBus bus) {
        TINKER_ITEMS.register(bus);
    }
}
