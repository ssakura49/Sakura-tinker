package com.ssakura49.sakuratinker.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

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
    }

    public static class Items{
        private static TagKey<Item> forgeTag(String name){
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),new ResourceLocation("forge",name));
        }

        public static final TagKey<Item> eezo_ore = forgeTag("ores/eezo");
        public static final TagKey<Item> nihilite_ore = forgeTag("ores/nihilite");

        public static final TagKey<Item> youkai_ingot = forgeTag("ingots/youkai");
        public static final TagKey<Item> soul_sakura = forgeTag("ingots/soul_sakura");
        public static final TagKey<Item> nihilite_ingot = forgeTag("ingots/nihilite");
        public static final TagKey<Item> eezo_ingot = forgeTag("ingots/eezo");
        public static final TagKey<Item> arcane_alloy = forgeTag("ingots/arcane_alloy");
        public static final TagKey<Item> colorful_ingot = forgeTag("ingots/colorful");

        public static final TagKey<Item> fiery_crystal = forgeTag("gems/fiery_crystal");
    }
}
