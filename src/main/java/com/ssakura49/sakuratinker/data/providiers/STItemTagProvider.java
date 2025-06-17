package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.ExtraBotany.ExtraBotanyCompat;
import com.ssakura49.sakuratinker.data.STTagKeys;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

public class STItemTagProvider extends ItemTagsProvider {
    public STItemTagProvider(PackOutput output,
                             CompletableFuture<HolderLookup.Provider> lookupProvider,
                             CompletableFuture<TagLookup<Block>> blockTags,
                             @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SakuraTinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 矿石标签
        this.tag(STTagKeys.Items.eezo_ore).add(STItems.eezo_ore.get());
        this.tag(STTagKeys.Items.terracryst_ore).add(STItems.terracryst_ore.get(), STItems.terracryst_ore_deepslate.get());
        this.tag(STTagKeys.Items.prometheum_ore).add(STItems.prometheum_ore.get(), STItems.prometheum_ore_deepslate.get());
        this.tag(STTagKeys.Items.orichalcum_ore).add(STItems.orichalcum_ore.get(), STItems.orichalcum_ore_deepslate.get());
        this.tag(Tags.Items.ORES).replace(false)
                .addTag(STTagKeys.Items.eezo_ore)
                .addTag(STTagKeys.Items.terracryst_ore)
        ;
        // 锭标签
        this.tag(STTagKeys.Items.youkai_ingot).add(STItems.youkai_ingot.get());
        this.tag(STTagKeys.Items.soul_sakura).add(STItems.soul_sakura.get());
        this.tag(STTagKeys.Items.nihilite_ingot).add(STItems.nihilite_ingot.get());
        this.tag(STTagKeys.Items.eezo_ingot).add(STItems.eezo_ingot.get());
        this.tag(STTagKeys.Items.arcane_alloy).add(STItems.arcane_alloy.get());
        this.tag(STTagKeys.Items.colorful_ingot).add(STItems.colorful_ingot.get());
        this.tag(STTagKeys.Items.prometheum_ingot).add(STItems.prometheum_ingot.get());
        this.tag(STTagKeys.Items.orichalcum_ingot).add(STItems.orichalcum_ingot.get());
        this.tag(STTagKeys.Items.bear_interest_ingot).add(STItems.bear_interest_ingot.get());
        this.tag(STTagKeys.Items.mycelium_slimesteel).add(STItems.mycelium_slimesteel.get());
        this.tag(STTagKeys.Items.frost_slimesteel).add(STItems.frost_slimesteel.get());
        this.tag(STTagKeys.Items.echo_slimesteel).add(STItems.echo_slimesteel.get());
        this.tag(STTagKeys.Items.unholy_alloy).add(STItems.unholy_alloy.get());
        this.tag(Tags.Items.INGOTS).replace(false)
                .addTag(STTagKeys.Items.youkai_ingot)
                .addTag(STTagKeys.Items.soul_sakura)
                .addTag(STTagKeys.Items.nihilite_ingot)
                .addTag(STTagKeys.Items.eezo_ingot)
                .addTag(STTagKeys.Items.arcane_alloy)
                .addTag(STTagKeys.Items.colorful_ingot)
                .addTag(STTagKeys.Items.prometheum_ingot)
                .addTag(STTagKeys.Items.orichalcum_ingot)
                .addTag(STTagKeys.Items.bear_interest_ingot)
                .addTag(STTagKeys.Items.mycelium_slimesteel)
                .addTag(STTagKeys.Items.frost_slimesteel)
                .addTag(STTagKeys.Items.echo_slimesteel)
                .addTag(STTagKeys.Items.unholy_alloy)
        ;
        // 宝石/晶体标签
        this.tag(STTagKeys.Items.fiery_crystal).add(STItems.fiery_crystal.get());
        this.tag(STTagKeys.Items.terracryst).add(STItems.terracryst.get());
        this.tag(STTagKeys.Items.slime_crystal_earth).add(STItems.slime_crystal_earth.get());
        this.tag(STTagKeys.Items.slime_crystal_sky).add(STItems.slime_crystal_sky.get());
        this.tag(STTagKeys.Items.slime_crystal_nether).add(STItems.slime_crystal_nether.get());
        this.tag(Tags.Items.GEMS).replace(false)
                .addTag(STTagKeys.Items.fiery_crystal)
                .addTag(STTagKeys.Items.terracryst)
        ;
        //
        this.tag(STTagKeys.Items.slime_ball_frost).add(STItems.slime_ball_frost.get());
        this.tag(STTagKeys.Items.slime_ball_mycelium).add(STItems.slime_ball_mycelium.get());
        this.tag(STTagKeys.Items.slime_ball_echo).add(STItems.slime_ball_echo.get());
        this.tag(STTagKeys.Items.blood_ball).add(STItems.blood_ball.get());
        this.tag(Tags.Items.SLIMEBALLS).replace(false)
                .addTag(STTagKeys.Items.slime_ball_frost)
                .addTag(STTagKeys.Items.slime_ball_mycelium)
                .addTag(STTagKeys.Items.slime_ball_echo)
                .addTag(STTagKeys.Items.blood_ball)
        ;


//        this.tag(TinkerTags.Items.PATTERNS).replace(false).add(
//                STItems.charm_chain.get(),
//                STItems.charm_core.get(),
//                STItems.swift_blade.get(),
//                STItems.laser_medium.get(),
//                STItems.energy_unit.get(),
//                STItems.barrel.get(),
//                STItems.blade.get(),
//                STItems.arrow_head.get(),
//                STItems.arrow_shaft.get(),
//                STItems.fletching.get(),
//                STItems.blade_box.get(),
//                STItems.great_blade.get(),
//                STItems.shell.get(),
//                STItems.flag.get(),
//                STItems.fox_mask_main.get(),
//                STItems.fox_mask_core.get()
//        );

//        this.tag(TinkerTags.Items.GOLD_CASTS).replace(false).add(
//                STItems.charm_chain_cast.get(),
//                STItems.charm_core_cast.get(),
//                STItems.swift_blade_cast.get(),
//                STItems.swift_guard_cast.get(),
//                STItems.barrel_cast.get(),
//                STItems.energy_unit_cast.get(),
//                STItems.laser_medium_cast.get(),
//                STItems.blade_cast.get(),
//                STItems.blade_sand_cast.get(),
//                STItems.arrow_head_cast.get(),
//                STItems.arrow_shaft_cast.get(),
//                STItems.great_blade_cast.get(),
//                STItems.shell_cast.get(),
//                STItems.flag_cast.get(),
//                STItems.fox_mask_main_cast.get(),
//                STItems.fox_mask_core_cast.get()
//        );
//        this.tag(TinkerTags.Items.RED_SAND_CASTS).replace(false).add(
//                STItems.charm_chain_red_sand_cast.get(),
//                STItems.charm_core_red_sand_cast.get(),
//                STItems.swift_blade_red_sand_cast.get(),
//                STItems.swift_guard_red_sand_cast.get(),
//                STItems.energy_unit_red_sand_cast.get(),
//                STItems.laser_medium_red_sand_cast.get(),
//                STItems.arrow_head_red_sand_cast.get(),
//                STItems.arrow_shaft_red_sand_cast.get(),
//                STItems.great_blade_red_sand_cast.get(),
//                STItems.shell_red_sand_cast.get(),
//                STItems.flag_red_sand_cast.get(),
//                STItems.fox_mask_main_red_sand_cast.get(),
//                STItems.fox_mask_core_red_sand_cast.get()
//        );
//        this.tag(TinkerTags.Items.SAND_CASTS).replace(false).add(
//                STItems.charm_chain_red_sand_cast.get(),
//                STItems.charm_core_red_sand_cast.get(),
//                STItems.swift_blade_red_sand_cast.get(),
//                STItems.swift_guard_red_sand_cast.get(),
//                STItems.energy_unit_red_sand_cast.get(),
//                STItems.laser_medium_red_sand_cast.get(),
//                STItems.arrow_head_red_sand_cast.get(),
//                STItems.arrow_shaft_red_sand_cast.get(),
//                STItems.great_blade_red_sand_cast.get(),
//                STItems.shell_red_sand_cast.get(),
//                STItems.flag_red_sand_cast.get(),
//                STItems.fox_mask_main_red_sand_cast.get(),
//                STItems.fox_mask_core_red_sand_cast.get()
//        );
//        this.tag(TinkerTags.Items.SINGLE_USE_CASTS).replace(false).add(
//
//        );
//
//        this.tag(TinkerTags.Items.MULTI_USE_CASTS).replace(false).add(
//
//        );
    }
}
