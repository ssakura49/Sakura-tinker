package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.items.BloodBallItem;
import com.ssakura49.sakuratinker.content.items.BloodBoundSteelItem;
import com.ssakura49.sakuratinker.content.items.BloodDropItem;
import com.ssakura49.sakuratinker.content.tools.definition.ToolDefinitions;
import com.ssakura49.sakuratinker.content.tools.item.*;
import com.ssakura49.sakuratinker.content.tools.stats.BatteryCellMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.CharmChainMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.LaserMediumMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.STExtraMaterialStats;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class STItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> YKHC_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> ISS_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> EL_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> REA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> TF_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> DE_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);
    public static final DeferredRegister<Item> IAF_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SakuraTinker.MODID);

    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);

    protected static List<RegistryObject<Item>> LIST_MICS = new ArrayList<>(List.of());
    protected static List<RegistryObject<Item>> LIST_MATERIAL = new ArrayList<>(List.of());
    protected static List<RegistryObject<Item>> LIST_TOOL = new ArrayList<>(List.of());

    private static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    private static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    private static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

    protected static List<RegistryObject<BlockItem>> LIST_SIMPLE_BLOCK = new ArrayList<>(List.of());
    protected static List<RegistryObject<Item>> LIST_MATERIAL_ITEM_MODEL = new ArrayList<>(List.of());

    public static List<RegistryObject<Item>> getListSimpleModel() {
        return List.copyOf(LIST_MATERIAL_ITEM_MODEL);
    }
    public static List<RegistryObject<BlockItem>> getListSimpleBlock() {
        return List.copyOf(LIST_SIMPLE_BLOCK);
    }

    public static RegistryObject<Item> registerMisc(DeferredRegister<Item> register, String string, Supplier<? extends Item> supplier, boolean simpleModel) {
        RegistryObject<Item> object = register.register(string, supplier);
        LIST_MICS.add(object);
        if (simpleModel) {
            LIST_MATERIAL_ITEM_MODEL.add(object);
        }
        return object;
    }
    public static RegistryObject<Item> registerMaterial(DeferredRegister<Item> register, String string, Supplier<? extends Item> supplier, boolean simpleModel) {
        RegistryObject<Item> object = register.register(string, supplier);
        LIST_MATERIAL.add(object);
        if (simpleModel) {
            LIST_MATERIAL_ITEM_MODEL.add(object);
        }
        return object;
    }
    public static RegistryObject<Item> registerToolOrPart(DeferredRegister<Item> register, String string, Supplier<? extends Item> supplier) {
        RegistryObject<Item> object = register.register(string, supplier);
        LIST_TOOL.add(object);
        return object;
    }
    public static RegistryObject<BlockItem> registerSimpleBlockItem(DeferredRegister<Item> register, RegistryObject<? extends Block> block) {
        RegistryObject<BlockItem> object = register.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        LIST_SIMPLE_BLOCK.add(object);
        return object;
    }

    public static final RegistryObject<BlockItem> nihilite_ore = registerSimpleBlockItem(ITEMS, STBlocks.NIHILITE_ORE);
    public static final RegistryObject<BlockItem> nihilite_ore_deepslate = registerSimpleBlockItem(ITEMS, STBlocks.NIHILITE_ORE_DEEPSLATE);
    public static final RegistryObject<BlockItem> eezo_ore = registerSimpleBlockItem(ITEMS, STBlocks.EEZO_ORE);

    public static final RegistryObject<Item> youkai_ingot = registerMaterial(YKHC_ITEMS,"youkai_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> soul_sakura = registerMaterial(ITEMS,"soul_sakura", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> fiery_crystal = registerMaterial(TF_ITEMS,"fiery_crystal", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> wither_heart = registerMaterial(ITEMS,"wither_heart", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> nihilite_raw_ore = registerMaterial(ITEMS,"nihilite_raw_ore", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> nihilite_ingot = registerMaterial(ITEMS,"nihilite_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> nihilite_nugget = registerMaterial(ITEMS,"nihilite_nugget", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> eezo_ingot = registerMaterial(ITEMS,"eezo_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> eezo_nugget = registerMaterial(ITEMS,"eezo_nugget", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> arcane_alloy = registerMaterial(ISS_ITEMS,"arcane_alloy", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> colorful_ingot = registerMaterial(REA_ITEMS,"colorful_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> coalescence_matrix = registerMaterial(DE_ITEMS, "coalescence_matrix", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> wyvern_ingot = registerMaterial(DE_ITEMS, "wyvern_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> draconic_ingot = registerMaterial(DE_ITEMS, "draconic_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> chaotic_ingot = registerMaterial(DE_ITEMS, "chaotic_ingot", () -> new Item(new Item.Properties()), true);
    public static final RegistryObject<Item> blood_bound_steel = registerMaterial(ITEMS, "blood_bound_steel", () -> new BloodBoundSteelItem(new Item.Properties()), true);
    public static final RegistryObject<Item> steady_alloy = registerMaterial(ITEMS, "steady_alloy", () -> new Item(new Item.Properties()), true);

    public static final RegistryObject<Item> ghost_knife = registerMisc(ITEMS, "ghost_knife", () -> new Item(new Item.Properties()), true);

    public static final RegistryObject<Item> blood_ball = registerMaterial(ITEMS, "blood_ball",() -> new BloodBallItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2f).alwaysEat().build())), true);
    public static final RegistryObject<Item> blood_drop = registerMaterial(ITEMS, "blood_drop", () -> new BloodDropItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2f).build())), true);

    public static final ItemObject<ToolPartItem> charm_chain = TINKER_ITEMS.register("charm_chain", () -> new ToolPartItem(PartItem, CharmChainMaterialStats.ID));
    public static final ItemObject<ToolPartItem> charm_core = TINKER_ITEMS.register("charm_core", () -> new ToolPartItem(PartItem, STExtraMaterialStats.CHARM_CORE.getIdentifier()));
    public static final ItemObject<ToolPartItem> swift_blade = TINKER_ITEMS.register("swift_blade", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));
    public static final ItemObject<ToolPartItem> swift_guard = TINKER_ITEMS.register("swift_guard", () -> new ToolPartItem(PartItem, HandleMaterialStats.ID));

    public static final ItemObject<ToolPartItem> laser_medium = TINKER_ITEMS.register("laser_medium", () -> new ToolPartItem(PartItem, LaserMediumMaterialStats.ID));
    public static final ItemObject<ToolPartItem> battery_cell = TINKER_ITEMS.register("battery_cell", () -> new ToolPartItem(PartItem, BatteryCellMaterialStats.ID));
    public static final ItemObject<ToolPartItem> barrel = TINKER_ITEMS.register("barrel", () -> new ToolPartItem(PartItem, HeadMaterialStats.ID));

    public static final ItemObject<ModifiableItem> tinker_charm = TINKER_ITEMS.register("tinker_charm", () -> new TinkerCharm(ToolItem, ToolDefinitions.TINKER_CHARM));
    public static final ItemObject<ModifiableItem> great_sword = TINKER_ITEMS.register("great_sword",() -> new ModifiableItem(ToolItem, ToolDefinitions.GREAT_SWORD));
    public static final ItemObject<ModifiableItem> swift_sword = TINKER_ITEMS.register("swift_sword", () -> new ModifiableItem(ToolItem, ToolDefinitions.SWIFT_SWORD));
    public static final ItemObject<ModifiableItem> vampire_knife = TINKER_ITEMS.register("vampire_knife", () -> new VampireKnives(ToolItem, ToolDefinitions.VAMPIRE_KNIVES));
    public static final ItemObject<ModifiableItem> blade_convergence = TINKER_ITEMS.register("blade_convergence", () -> new BladeConvergence(ToolItem, ToolDefinitions.BLADE_CONVERGENCE));
    public static final ItemObject<ModifiableItem> laser_gun = TINKER_ITEMS.register("laser_gun", () -> new LaserGun(ToolItem, ToolDefinitions.LASER_GUN));

    public static final ItemObject<PartCastItem> charm_chain_cast = TINKER_ITEMS.register("charm_chain_cast", () -> new PartCastItem(CastItem, charm_chain));
    public static final ItemObject<PartCastItem> charm_chain_red_sand_cast = TINKER_ITEMS.register("charm_chain_red_sand_cast", () -> new PartCastItem(CastItem, charm_chain));
    public static final ItemObject<PartCastItem> charm_chain_sand_cast = TINKER_ITEMS.register("charm_chain_sand_cast", () -> new PartCastItem(CastItem, charm_chain));
    public static final ItemObject<PartCastItem> charm_core_cast = TINKER_ITEMS.register("charm_core_cast", () -> new PartCastItem(CastItem, charm_core));
    public static final ItemObject<PartCastItem> charm_core_red_sand_cast = TINKER_ITEMS.register("charm_core_red_sand_cast", () -> new PartCastItem(CastItem, charm_core));
    public static final ItemObject<PartCastItem> charm_core_sand_cast = TINKER_ITEMS.register("charm_core_sand_cast", () -> new PartCastItem(CastItem, charm_core));
    public static final ItemObject<PartCastItem> swift_blade_cast = TINKER_ITEMS.register("swift_blade_cast", () -> new PartCastItem(CastItem, swift_blade));
    public static final ItemObject<PartCastItem> swift_blade_red_sand_cast = TINKER_ITEMS.register("swift_blade_red_sand_cast", () -> new PartCastItem(CastItem, swift_blade));
    public static final ItemObject<PartCastItem> swift_blade_sand_cast = TINKER_ITEMS.register("swift_blade_sand_cast", () -> new PartCastItem(CastItem, swift_blade));
    public static final ItemObject<PartCastItem> swift_guard_cast = TINKER_ITEMS.register("swift_guard_cast", () -> new PartCastItem(CastItem, swift_guard));
    public static final ItemObject<PartCastItem> swift_guard_red_sand_cast = TINKER_ITEMS.register("swift_guard_red_sand_cast", () -> new PartCastItem(CastItem, swift_guard));
    public static final ItemObject<PartCastItem> swift_guard_sand_cast = TINKER_ITEMS.register("swift_guard_sand_cast", () -> new PartCastItem(CastItem, swift_guard));
    public static final ItemObject<PartCastItem> barrel_cast = TINKER_ITEMS.register("barrel_cast", () -> new PartCastItem(CastItem, barrel));
    public static final ItemObject<PartCastItem> barrel_red_sand_cast = TINKER_ITEMS.register("barrel_red_sand_cast", () -> new PartCastItem(CastItem, barrel));
    public static final ItemObject<PartCastItem> barrel_sand_cast = TINKER_ITEMS.register("barrel_sand_cast", () -> new PartCastItem(CastItem, barrel));
    public static final ItemObject<PartCastItem> battery_cell_cast = TINKER_ITEMS.register("battery_cell_cast", () -> new PartCastItem(CastItem, battery_cell));
    public static final ItemObject<PartCastItem> battery_cell_red_sand_cast = TINKER_ITEMS.register("battery_cell_red_sand_cast", () -> new PartCastItem(CastItem, battery_cell));
    public static final ItemObject<PartCastItem> battery_cell_sand_cast = TINKER_ITEMS.register("battery_cell_sand_cast", () -> new PartCastItem(CastItem, battery_cell));
    public static final ItemObject<PartCastItem> laser_medium_cast = TINKER_ITEMS.register("laser_medium_cast", () -> new PartCastItem(CastItem, laser_medium));
    public static final ItemObject<PartCastItem> laser_medium_red_sand_cast = TINKER_ITEMS.register("laser_medium_red_sand_cast", () -> new PartCastItem(CastItem, laser_medium));
    public static final ItemObject<PartCastItem> laser_medium_sand_cast = TINKER_ITEMS.register("laser_medium_sand_cast", () -> new PartCastItem(CastItem, laser_medium));


    public STItems() {
    }
}
