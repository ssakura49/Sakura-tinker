package com.ssakura49.sakuratinker.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;

public class STTagKeys {
    public static class Fluids {
        private static TagKey<Fluid> forgeTag(String string) {
            return TagKey.create(ForgeRegistries.FLUIDS.getRegistryKey(), new ResourceLocation("forge", string));
        }

        public static final TagKey<Fluid> molten_youkai = forgeTag("molten_youkai");
        public static final TagKey<Fluid> molten_etherium = forgeTag("molten_etherium");
        public static final TagKey<Fluid> molten_arcane_salvage = forgeTag("molten_arcane_salvage");
        public static final TagKey<Fluid> molten_infinity = forgeTag("molten_infinity");
        public static final TagKey<Fluid> molten_soul_sakura = forgeTag("molten_soul_sakura");
        public static final TagKey<Fluid> molten_fiery_crystal = forgeTag("molten_fiery_crystal");
        public static final TagKey<Fluid> molten_nihilite = forgeTag("molten_nihilite");
        public static final TagKey<Fluid> molten_eezo = forgeTag("molten_eezo");
        public static final TagKey<Fluid> molten_arcane_alloy = forgeTag("molten_arcane_alloy");
        public static final TagKey<Fluid> molten_neutron = forgeTag("molten_neutron");
        public static final TagKey<Fluid> molten_colorful = forgeTag("molten_colorful");

        public static final TagKey<Fluid> molten_crystal_matrix = forgeTag("molten_crystal_matrix");
        public static final TagKey<Fluid> molten_blood_bound_steel = forgeTag("molten_blood_bound_steel");
        public static final TagKey<Fluid> molten_blood = forgeTag("molten_blood");
        public static final TagKey<Fluid> molten_dragon_fire_steel = forgeTag("molten_dragon_fire_steel");
        public static final TagKey<Fluid> molten_dragon_ice_steel = forgeTag("molten_dragon_ice_steel");
        public static final TagKey<Fluid> molten_dragon_lightning_steel = forgeTag("molten_dragon_lightning_steel");
        public static final TagKey<Fluid> molten_steady_alloy = forgeTag("molten_steady_alloy");
        public static final TagKey<Fluid> molten_south_star = forgeTag("molten_south_star");
        public static final TagKey<Fluid> molten_terracryst = forgeTag("molten_terracryst");
        public static final TagKey<Fluid> molten_prometheum = forgeTag("molten_prometheum");
        public static final TagKey<Fluid> molten_orichalcum = forgeTag("molten_orichalcum");
        public static final TagKey<Fluid> molten_aurumos = forgeTag("molten_aurumos");
        public static final TagKey<Fluid> molten_bear_interest = forgeTag("molten_bear_interest");
        public static final TagKey<Fluid> slime_frost = forgeTag("slime_frost");
        public static final TagKey<Fluid> slime_mycelium = forgeTag("slime_mycelium");
        public static final TagKey<Fluid> slime_echo = forgeTag("slime_echo");
        public static final TagKey<Fluid> molten_mana_steel = forgeTag("molten_mana_steel");
        public static final TagKey<Fluid> molten_goozma = forgeTag("molten_goozma");
        public static final TagKey<Fluid> molten_frost_slimesteel = forgeTag("molten_frost_slimesteel");
        public static final TagKey<Fluid> molten_echo_slimesteel = forgeTag("molten_echo_slimesteel");
        public static final TagKey<Fluid> molten_mycelium_slimesteel = forgeTag("molten_mycelium_slimesteel");
        public static final TagKey<Fluid> molten_infinity_catalyst = forgeTag("molten_infinity_catalyst");
        public static final TagKey<Fluid> molten_orichalcos = forgeTag("molten_orichalcos");
        public static final TagKey<Fluid> molten_pyrothium = forgeTag("molten_pyrothium");
        public static final TagKey<Fluid> molten_cursed_metal = forgeTag("molten_cursed_metal");
        public static final TagKey<Fluid> molten_dark_metal = forgeTag("molten_dark_metal");
        public static final TagKey<Fluid> molten_unholy_alloy = forgeTag("molten_unholy_alloy");
        public static final TagKey<Fluid> molten_dread_steel = forgeTag("molten_dread_steel");
        public static final TagKey<Fluid> molten_terra_steel = forgeTag("molten_terra_steel");
    }

    public static class Items {
//        public static final TagKey<Item> INGOTS = forgeTag("ingots");
//        public static final TagKey<Item> NUGGETS = forgeTag("nuggets");
        private static TagKey<Item> forgeTag(String name){
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),new ResourceLocation("forge",name));
        }
        private static TagKey<Item> modTag(String path) {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(MODID, path));
        }

        // 矿石标签
        public static final TagKey<Item> eezo_ore = forgeTag("ores/eezo");
        public static final TagKey<Item> terracryst_ore = forgeTag("ores/terracryst");
        public static final TagKey<Item> prometheum_ore = forgeTag("ores/prometheum");
        public static final TagKey<Item> orichalcum_ore = forgeTag("ores/orichalcum");

        // 锭标签
        public static final TagKey<Item> youkai_ingot = forgeTag("ingots/youkai");
        public static final TagKey<Item> soul_sakura = forgeTag("ingots/soul_sakura");
        public static final TagKey<Item> nihilite_ingot = forgeTag("ingots/nihilite");
        public static final TagKey<Item> eezo_ingot = forgeTag("ingots/eezo");
        public static final TagKey<Item> arcane_alloy = forgeTag("ingots/arcane_alloy");
        public static final TagKey<Item> colorful_ingot = forgeTag("ingots/colorful");
        public static final TagKey<Item> prometheum_ingot = forgeTag("ingots/prometheum");
        public static final TagKey<Item> orichalcum_ingot = forgeTag("ingots/orichalcum");
        public static final TagKey<Item> bear_interest_ingot = forgeTag("ingots/bear_interest");
        public static final TagKey<Item> mycelium_slimesteel = forgeTag("ingots/mycelium_slimesteel");
        public static final TagKey<Item> frost_slimesteel = forgeTag("ingots/frost_slimesteel");
        public static final TagKey<Item> echo_slimesteel = forgeTag("ingots/echo_slimesteel");
        public static final TagKey<Item> unholy_alloy = forgeTag("ingots/unholy_alloy");

        // 宝石/晶体标签
        public static final TagKey<Item> fiery_crystal = forgeTag("gems/fiery_crystal");
        public static final TagKey<Item> terracryst = forgeTag("gems/terracryst");
        public static final TagKey<Item> slime_crystal_earth = forgeTag("gems/slime_crystal_earth");
        public static final TagKey<Item> slime_crystal_sky = forgeTag("gems/slime_crystal_sky");
        public static final TagKey<Item> slime_crystal_nether = forgeTag("gems/slime_crystal_nether");

        // 特殊材料标签
        public static final TagKey<Item> wither_heart = forgeTag("materials/wither_heart");
        public static final TagKey<Item> south_star = forgeTag("materials/south_star");
        public static final TagKey<Item> goozma = forgeTag("materials/goozma");
        public static final TagKey<Item> pyrothium = forgeTag("materials/pyrothium");
        public static final TagKey<Item> coalescence_matrix = forgeTag("materials/coalescence_matrix");

        // 原材料标签
        public static final TagKey<Item> prometheum_raw = forgeTag("raw_materials/prometheum");
        public static final TagKey<Item> orichalcum_raw = forgeTag("raw_materials/orichalcum");

        // 粘液球标签
        public static final TagKey<Item> slime_ball_frost = forgeTag("slimeballs/frost");
        public static final TagKey<Item> slime_ball_mycelium = forgeTag("slimeballs/mycelium");
        public static final TagKey<Item> slime_ball_echo = forgeTag("slimeballs/echo");
        public static final TagKey<Item> blood_ball = forgeTag("slimeballs/blood");

        // 锭块标签
        public static final TagKey<Item> storage_blocks_youkai = forgeTag("storage_blocks/youkai");
        public static final TagKey<Item> storage_blocks_soul_sakura = forgeTag("storage_blocks/soul_sakura");
    }
}
