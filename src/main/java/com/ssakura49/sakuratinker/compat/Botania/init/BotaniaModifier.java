package com.ssakura49.sakuratinker.compat.Botania.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.Botania.modifier.*;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class BotaniaModifier {
    public static ModifierDeferredRegister BOTANIA_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);
    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);


    public static StaticModifier<BotanyIsTechModifier> BotanyIsTech;
    public static StaticModifier<ManaRayModifier> ManaRay;
    public static StaticModifier<TerraMagicalModifier> TerraMagical;
    public static StaticModifier<PixieModifier> Pixie;
    public static StaticModifier<PixieArmorModifier> PixieArmor;
    public static StaticModifier<GaiaWrathModifier> GaiaWrath;
    public static StaticModifier<GaiaSoulModifier> GaiaSoul;
    public static StaticModifier<GaiaGuardianModifier> GaiaGuardian;
    public static StaticModifier<ManaArrowModifier> ManaArrow;
    public static StaticModifier<ManaTransformModifier> ManaTransform;

    static {
        BotanyIsTech = BOTANIA_MODIFIERS.register("botany_is_tech", BotanyIsTechModifier::new);
        ManaRay = BOTANIA_MODIFIERS.register("mana_ray", ManaRayModifier::new);
        TerraMagical = BOTANIA_MODIFIERS.register("terra_magical",TerraMagicalModifier::new);
        Pixie = BOTANIA_MODIFIERS.register("pixie", PixieModifier::new);
        PixieArmor = BOTANIA_MODIFIERS.register("pixie_armor",PixieArmorModifier::new);
        GaiaWrath = BOTANIA_MODIFIERS.register("gaia_wrath",GaiaWrathModifier::new);
        GaiaSoul = BOTANIA_MODIFIERS.register("gaia_soul",GaiaSoulModifier::new);
        GaiaGuardian = BOTANIA_MODIFIERS.register("gaia_guardian",GaiaGuardianModifier::new);
        ManaArrow = BOTANIA_MODIFIERS.register("mana_arrow", ManaArrowModifier::new);
        ManaTransform = BOTANIA_MODIFIERS.register("mana_transform", ManaTransformModifier::new);
    }
}
