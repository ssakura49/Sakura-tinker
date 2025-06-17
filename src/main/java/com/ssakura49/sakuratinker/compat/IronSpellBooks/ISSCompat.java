package com.ssakura49.sakuratinker.compat.IronSpellBooks;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.*;
import com.ssakura49.sakuratinker.content.tools.item.TinkerSpellBook;
import com.ssakura49.sakuratinker.content.tools.stats.SpellClothMaterialStats;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableSpellBookItem;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

public class ISSCompat {
    public static ModifierDeferredRegister ISS_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static final ItemDeferredRegisterExtension TINKER_ISS_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);

    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

    public static final ItemObject<ToolPartItem> book_cover = TINKER_ISS_ITEMS.register("book_cover", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
    public static final ItemObject<ToolPartItem> spell_cloth = TINKER_ISS_ITEMS.register("spell_cloth", () -> new ToolPartItem(PartItem, SpellClothMaterialStats.ID));
    public static final ItemObject<ModifiableSpellBookItem> tinker_spell_book = TINKER_ISS_ITEMS.register("tinker_spell_book", () -> new TinkerSpellBook(10 ,ToolItem, SpellRarity.LEGENDARY));
    public static final ItemObject<PartCastItem> book_cover_cast = TINKER_ISS_ITEMS.register("book_cover_cast", () -> new PartCastItem(CastItem, book_cover));
    public static final ItemObject<PartCastItem> book_cover_sand_cast = TINKER_ISS_ITEMS.register("book_cover_sand_cast", () -> new PartCastItem(CastItem, book_cover));
    public static final ItemObject<PartCastItem> book_cover_red_sand_cast = TINKER_ISS_ITEMS.register("book_cover_red_sand_cast", () -> new PartCastItem(CastItem, book_cover));
    public static final ItemObject<PartCastItem> spell_cloth_cast = TINKER_ISS_ITEMS.register("spell_cloth_cast", () -> new PartCastItem(CastItem, spell_cloth));
    public static final ItemObject<PartCastItem> spell_cloth_red_sand_cast = TINKER_ISS_ITEMS.register("spell_cloth_red_sand_cast", () -> new PartCastItem(CastItem, spell_cloth));
    public static final ItemObject<PartCastItem> spell_cloth_sand_cast = TINKER_ISS_ITEMS.register("spell_cloth_sand_cast", () -> new PartCastItem(CastItem, spell_cloth));


    public static StaticModifier<MagicianModifier> Magician;
    public static StaticModifier<FountainMagicModifier> FountainMagic;
    public static StaticModifier<ElementalMasteryModifier> ElementalMastery;
    public static StaticModifier<MagicShieldModifier> MagicShield;
    public static StaticModifier<EnchantedBladeModifier> EnchantedBlade;
    public static StaticModifier<ArcaneConstructModifier> ArcaneTinkering;
    static {
        MagicShield = ISS_MODIFIERS.register("magic_shield", MagicShieldModifier::new);
        Magician = ISS_MODIFIERS.register("magician", MagicianModifier::new);
        FountainMagic = ISS_MODIFIERS.register("fountain_magic", FountainMagicModifier::new);
        ElementalMastery = ISS_MODIFIERS.register("elemental_mastery", ElementalMasteryModifier::new);
        EnchantedBlade = ISS_MODIFIERS.register("enchanted_blade", EnchantedBladeModifier::new);
        ArcaneTinkering = ISS_MODIFIERS.register("arcane_tinkering", ArcaneConstructModifier::new);
    }
}
