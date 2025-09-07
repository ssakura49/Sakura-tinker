package com.ssakura49.sakuratinker.compat.BuddyCards;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.BuddyCards.modifiers.BuddyCardMasterArmorModifier;
import com.ssakura49.sakuratinker.compat.BuddyCards.modifiers.BuddyCardMasterModifier;
import com.ssakura49.sakuratinker.compat.BuddyCards.modifiers.PerfectionismModifier;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class BuddyCardCompat {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);
    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);


    public static StaticModifier<BuddyCardMasterModifier> BuddyCardMaster;
    public static StaticModifier<BuddyCardMasterArmorModifier> BuddyCardMasterArmor;
    public static StaticModifier<PerfectionismModifier> Perfectionism;

    static {
         BuddyCardMaster = MODIFIERS.register("buddy_card_master", BuddyCardMasterModifier::new);
         BuddyCardMasterArmor = MODIFIERS.register("buddy_card_master_armor", BuddyCardMasterArmorModifier::new);
         Perfectionism = MODIFIERS.register("perfectionism", PerfectionismModifier::new);
    }
}
