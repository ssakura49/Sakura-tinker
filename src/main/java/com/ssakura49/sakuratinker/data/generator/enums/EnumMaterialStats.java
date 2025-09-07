package com.ssakura49.sakuratinker.data.generator.enums;

import com.ssakura49.sakuratinker.common.tools.stats.*;
import com.ssakura49.sakuratinker.common.tools.tiers.DreadSteelTiers;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.*;

import static slimeknights.tconstruct.tools.stats.PlatingMaterialStats.Builder;

public enum EnumMaterialStats {
    soul_sakura(
            armor(100, 2.0f, 3.0f, 4.0f, 3.0f).toughness(3.0f).knockbackResistance(0.15f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.15f, 0.1f, -0.05f, 0.1f),
            new HeadMaterialStats(862, 7f, Tiers.NETHERITE, 4f),
            new GripMaterialStats(0.1f,0.1f,3f),
            new LimbMaterialStats(500,0.2f,0.2f,0.15f),
            new CharmChainMaterialStats(0.1f, 20, 0.2f, 0.2f, 0.2f, 0.2f),
            STStatlessMaterialStats.CHARM_CORE,
            new EnergyUnitMaterialStats(70000, 0.7f),
            new LaserMediumMaterialStats(35f, 0.4f, 14),
            new BattleFlagMaterialStats(15, 600, 600, 40),
            new PhantomCoreMaterialStats(2,32),
            new AxleMaterialStats(200, 10, 3.5f),
            new YoYoRingMaterialStats(2),
            new ChordMaterialStats(6),
            new SoulGathererMaterialStats(6f, 0.2f)
    ),
    nihilite(
            armor(100, 4.0f, 5.0f, 3.0f, 2.0f).toughness(3.0f).knockbackResistance(0.15f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0.4f, 0.15f, 0.15f),
            new HeadMaterialStats(956, 7f, Tiers.NETHERITE, 5f),
            new CharmChainMaterialStats(0.1f, 26, 0.25f, 0.15f, 0.2f, 0.15f),
            STStatlessMaterialStats.CHARM_CORE,
            new EnergyUnitMaterialStats(100000, 0.6f),
            new LaserMediumMaterialStats(40f, 0.5f, 10),
            new BattleFlagMaterialStats(25, 720, 320, 120),
            new PhantomCoreMaterialStats(2,32),
            new AxleMaterialStats(300, 10, 2.0f),
            new YoYoRingMaterialStats(2),
            new SoulGathererMaterialStats(8f, 0.25f)
    ),
    eezo(
            null,
            false,
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(0.3f, 0.2f, 0.1f, 0.2f),
            new HeadMaterialStats(1126, 7f, Tiers.DIAMOND, 5f),
            new LimbMaterialStats(400, 0.1f, 0.1f,0.1f),
            new CharmChainMaterialStats(0f, 20, 0.15f, 0.1f, 0.15f, 0.15f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(12, 160, 320, 100),
            new PhantomCoreMaterialStats(1,32),
            new AxleMaterialStats(200, 20, 4.0f),
            new YoYoRingMaterialStats(2),
            new SoulGathererMaterialStats(4f, 0.3f)
    ),
    blood_bound_steel(
            null,
            false,
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(0.3f, -0.1f, 0.15f, 0.15f),
            new HeadMaterialStats(634, 5f, Tiers.DIAMOND, 5f),
            new LimbMaterialStats(400, -0.2f, 0.2f,0.1f),
            new PhantomCoreMaterialStats(1,16),
            new AxleMaterialStats(240, 18, 4.0f),
            new YoYoRingMaterialStats(1),
            new SoulGathererMaterialStats(2f, 0.1f)
    ),
    steady_alloy(
            armor(80, 6.0f, 9.0f, 7.0f, 4.0f).toughness(5.0f).knockbackResistance(0.2f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.6f, -0.2f, -0.05f, -0.05f),
            new HeadMaterialStats(1968, 5f, Tiers.NETHERITE, 8f),
            new CharmChainMaterialStats(0.15f, 30, 0.3f, 0.3f, 0.25f, 0.2f),
            STStatlessMaterialStats.CHARM_CORE,
            new LimbMaterialStats(400, 0.1f, 0.1f,0.1f),
            new EnergyUnitMaterialStats(85000, 0.75f),
            new LaserMediumMaterialStats(35f, 0.5f, 8),
            new BattleFlagMaterialStats(40, 160, 160, 140),
            new PhantomCoreMaterialStats(2,32),
            new AxleMaterialStats(400, 20, 6.0f),
            new YoYoRingMaterialStats(2),
            new ChordMaterialStats(7),
            new SoulGathererMaterialStats(10f, 0.2f)
    ),
    south_star(
            armor(70, 3.0f, 5.0f, 4.0f, 2.0f).toughness(2.0f).knockbackResistance(0.02f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(-0.2f, 0.3f, 0.5f, 0.5f),
            new HeadMaterialStats(756, 7f, Tiers.NETHERITE, 6f),
            new CharmChainMaterialStats(0.15f, 30, 0.3f, 0.3f, 0.25f, 0.2f),
            STStatlessMaterialStats.CHARM_CORE,
            new LimbMaterialStats(300, 0.3f, 0.3f,0.2f),
            new EnergyUnitMaterialStats(60000, 0.3f),
            new LaserMediumMaterialStats(25f, 0.6f, 12),
            new BattleFlagMaterialStats(30, 320, 480, 100),
            new PhantomCoreMaterialStats(2,32)
    ),
    terracryst(
            armor(100, 4.0f, 7.0f, 5.0f, 3.0f).toughness(4.0f).knockbackResistance(0.3f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.4f, 0.2f, 0.4f, 0.35f),
            new HeadMaterialStats(1956, 3f, Tiers.NETHERITE, 7f),
            new CharmChainMaterialStats(-0.15f, 50, 0.25f, 0.2f, 0.3f, 0.2f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(8, 160, 1200, 70),
            new PhantomCoreMaterialStats(2,16),
            new AxleMaterialStats(240, 16, 2.5f),
            new YoYoRingMaterialStats(1),
            new ChordMaterialStats(6),
            new SoulGathererMaterialStats(4f, 0.1f)
    ),
    prometheum(
            armor(80, 2.0f, 5.0f, 3.0f, 2.0f).toughness(2.0f).knockbackResistance(0.1f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0.1f, 0.15f, 0.1f),
            new HeadMaterialStats(456, 5f, Tiers.NETHERITE, 4f),
            new CharmChainMaterialStats(-0.1f, 24, 0.15f, 0.15f, 0.1f, 0.1f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(12, 240, 240, 40),
            new PhantomCoreMaterialStats(1,16),
            new SoulGathererMaterialStats(4f, 0.1f)
    ),
    orichalcum(
            armor(70, 2.0f, 4.0f, 3.0f, 2.0f).toughness(3.0f).knockbackResistance(0.1f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0.1f, 0.15f, 0.1f),
            new HeadMaterialStats(556, 3f, Tiers.IRON, 5f),
            new CharmChainMaterialStats(0.2f, 14, 0.10f, 0.10f, 0.18f, 0.18f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(10, 40, 40, 40),
            new PhantomCoreMaterialStats(1,16),
            new SoulGathererMaterialStats(5f, 0.1f)
    ),
    aurumos(
            null,
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.6f, 0.15f, 0.2f, 0.15f),
            new HeadMaterialStats(996, 4f, Tiers.IRON, 6f),
            new CharmChainMaterialStats(0.2f, 14, 0.10f, 0.10f, 0.18f, 0.18f),
            STStatlessMaterialStats.CHARM_CORE,
            new LimbMaterialStats(1000, 0.3f, 0.2f,0.25f),
            new BattleFlagMaterialStats(12, 320, 160, 100),
            new PhantomCoreMaterialStats(1,16),
            new SoulGathererMaterialStats(4f, 0.15f)
    ),
    bear_interest(
            null,
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.1f, -0.15f, 0.15f, -0.1f),
            new HeadMaterialStats(330, 6f, Tiers.DIAMOND, 4f),
            new LimbMaterialStats(365, 0.2f, 0.1f,0.1f),
            new GripMaterialStats(0.1f,0.05f,1.0f),
            new PhantomCoreMaterialStats(1,12)
    ),
    mycelium_slimesteel(
            armor(80, 3.0f, 6.0f, 5.0f, 3.0f).toughness(1.0f).knockbackResistance(0.1f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0f, -0.05f, 0f),
            new HeadMaterialStats(1040, 4f, Tiers.DIAMOND, 2.5f),
            new LimbMaterialStats(460, -0.1f, -0.05f,0.15f),
            new GripMaterialStats(0.1f,0.1f,2.5f)
    ),
    frost_slimesteel(
            armor(80, 3.0f, 6.0f, 5.0f, 3.0f).toughness(1.0f).knockbackResistance(0.1f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0f, -0.05f, 0f),
            new HeadMaterialStats(1040, 4f, Tiers.DIAMOND, 2.5f),
            new LimbMaterialStats(460, -0.1f, -0.05f,0.15f),
            new GripMaterialStats(0.1f,0.1f,2.5f)
    ),
    echo_slimesteel(
            armor(120, 5.0f, 7.0f, 6.0f, 3.0f).toughness(5.0f).knockbackResistance(0.25f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.6f, -0.05f, 0.25f, 0.25f),
            new HeadMaterialStats(1862, 6f, Tiers.NETHERITE, 8f),
            new GripMaterialStats(0.3f,0.5f,5f),
            new LimbMaterialStats(1200,0.5f,0.5f,0.6f),
            new CharmChainMaterialStats(0.2f, 50, 0.3f, 0.2f, 0.25f, 0.25f),
            STStatlessMaterialStats.CHARM_CORE,
            new EnergyUnitMaterialStats(80000, 0.7f),
            new LaserMediumMaterialStats(80f, 0.7f, 11),
            new BattleFlagMaterialStats(50, 360, 80, 120)
    ),
//    goozma(
//            armor(120, 35.0f, 60.0f, 50.0f, 30.0f).toughness(40.0f).knockbackResistance(0.5f),
//            false,
//            StatlessMaterialStats.BINDING,
//            StatlessMaterialStats.MAILLE,
//            new HandleMaterialStats(1.0f, 1.0f, 1.0f, 1.0f),
//            new HeadMaterialStats(40000, 10f, InfinityTiers.INFINITY, 6666.0f),
//            new LimbMaterialStats(23000,1.0f,1.0f,1.0f),
//            new CharmChainMaterialStats(0.3f, 100, 0.5f, 0.5f, 1.0f, 0.5f),
//            STStatlessMaterialStats.CHARM_CORE,
//            new EnergyUnitMaterialStats(1000000, 2.0f),
//            new LaserMediumMaterialStats(200f, 0.2f),
//            new BattleFlagMaterialStats(80, 1200, 1200, 20)
//    ),
    youkai(
            armor(30, 2.0f, 5.0f, 2.0f, 4.0f).toughness(0f).knockbackResistance(0f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.1f, 0f, -0.1f, -0.05f),
            new HeadMaterialStats(320, 4f,Tiers.IRON, 2f),
            new LimbMaterialStats(300,-0.2f,0.1f,0f),
            new GripMaterialStats(0.05f,0.05f,2.0f),
            new CharmChainMaterialStats(0.1f, 18, 0.05f, 0.1f, 0.3f, 0.2f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(7, 280, 100, 80),
            new PhantomCoreMaterialStats(1,32)
    ),
    fiery_crystal(
            armor(80, 3.0f, 6.0f, 4.0f, 2.0f).toughness(3f).knockbackResistance(0.1f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.35f, 0.20f, 0.1f, 0.15f),
            new HeadMaterialStats(956, 6.0f,Tiers.DIAMOND, 7.0f),
            new LimbMaterialStats(400,0.1f,-0.1f,0.1f),
            new PhantomCoreMaterialStats(1,24)
    ),
    fairy_ice_crystal(
            null,
            false,
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(-0.1f, 0.1f, 0.1f, -0.1f),
            new HeadMaterialStats(130, 7.0f,Tiers.IRON, 2.0f)
    ),
    etherium(
            armor(70, 3.0f, 6.0f, 5.0f, 3.0f).toughness(3.0f).knockbackResistance(0f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.5f, 0.5f, -0.15f, 0.6f),
            new HeadMaterialStats(956, 7f,Tiers.DIAMOND, 7f),
            new LimbMaterialStats(300,-0.2f,0.1f,0.1f),
            new CharmChainMaterialStats(0.1f, 30, 0.15f, 0.25f, 0.3f, 0.3f),
            STStatlessMaterialStats.CHARM_CORE,
            new BattleFlagMaterialStats(18, 640, 640, 80),
            new PhantomCoreMaterialStats(3,64)
    ),
//    infinity(
//            armor(300, 30.0f, 60.0f, 50.0f, 30.0f).toughness(50.0f).knockbackResistance(0.25f),
//            false,
//            StatlessMaterialStats.BINDING,
//            StatlessMaterialStats.MAILLE,
//            new HandleMaterialStats(10f, 1.0f, 10f, 10f),
//            new HeadMaterialStats(66666, 10f,InfinityTiers.instance, 99999f),
//            new LimbMaterialStats(8800,2.0f,3.0f,1.0f),
//            new CharmChainMaterialStats(0.6f, 1000, 3.0f, 3.0f, 5.0f, 5.0f),
//            STStatlessMaterialStats.CHARM_CORE,
//            new EnergyUnitMaterialStats(90000000, 5.0f),
//            new LaserMediumMaterialStats(200f, 0.1f),
//            new BattleFlagMaterialStats(100, 3200, 3200, 10)
//    ),
    neutron(
        null,
        false,
        StatlessMaterialStats.BINDING,
        new HandleMaterialStats(-0.2f, -0.1f, 0.4f, 0.1f),
        new HeadMaterialStats(1620, 7f,Tiers.NETHERITE, 5f),
        new LimbMaterialStats(1300,0.2f,0.3f,-0.1f),
        new PhantomCoreMaterialStats(3,64)
    ),
//    colorful(
//            armor(300, 30.0f, 60.0f, 50.0f, 30.0f).toughness(50.0f).knockbackResistance(0.25f),
//            false,
//            StatlessMaterialStats.BINDING,
//            StatlessMaterialStats.MAILLE,
//            new HandleMaterialStats(1.0f, 1.0f, 1.0f, 1.0f),
//            new HeadMaterialStats(66666, 10f,InfinityTiers.INFINITY, 6666.0f),
//            new LimbMaterialStats(2300,1.0f,1.0f,1.0f),
//            new CharmChainMaterialStats(0.3f, 220, 0.6f, 0.6f, 1.5f, 1.5f),
//            STStatlessMaterialStats.CHARM_CORE,
//            new BattleFlagMaterialStats(80, 1000, 320, 20)
//    ),
    arcane_alloy(
            armor(30, 3.0f, 4.0f, 3.0f, 2.0f).toughness(2.0f).knockbackResistance(0.05f),
            false,
            StatlessMaterialStats.MAILLE,
            new SpellClothMaterialStats(20.0f,0.3f,0.15f,0.3f)
    ),
    orichalcos(
            armor(80, 6.0f, 9.0f, 5.0f, 4.0f).toughness(5.0f).knockbackResistance(0.25f),
            false,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.6f, 0.6f, 0.4f, 0.5f),
            new HeadMaterialStats(1634, 7.0f,Tiers.NETHERITE, 9.0f),
            new PhantomCoreMaterialStats(3,64)
    ),
    raven_feather(
            new FletchingMaterialStats(0.3f,0.1f)
    ),
    pyrothium(
            armor(80, 5.0f, 7.0f, 5.0f, 3.0f).toughness(4.0f).knockbackResistance(0.2f),
            false
    ),
    dread_steel(
            armor(80, 6.0f, 9.0f, 5.0f, 4.0f).toughness(5.0f).knockbackResistance(0.25f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.5f, 0.5f, 0.5f, 0.5f),
            new HeadMaterialStats(1880, 6.0f, DreadSteelTiers.instance, 9.0f)
    ),
    cursed_metal(
            armor(45, 2.0f, 5.0f, 4.0f, 2.0f).toughness(1.0f).knockbackResistance(0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.1f, 0.1f, 0.1f, 0.1f),
            new HeadMaterialStats(250, 6.0f, Tiers.IRON, 2.0f),
            new SoulGathererMaterialStats(4f, 0.15f)
    ),
    dark_metal(
            armor(45, 3.0f, 6.0f, 5.0f, 3.0f).toughness(2.0f).knockbackResistance(0.05f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.3f, 0.2f, 0.2f, 0.2f),
            new HeadMaterialStats(360, 7.0f, Tiers.IRON, 3.5f),
            new SoulGathererMaterialStats(6f, 0.2f)
    ),
    unholy_alloy(
            null,
            false,
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(0.5f, 0.7f, 0.6f, 0.6f),
            new HeadMaterialStats(1360, 7.0f, Tiers.NETHERITE, 7.0f),
            new SoulGathererMaterialStats(8f, 0.3f)
    ),
    mana_steel(
            armor(45, 3.0f, 6.0f, 5.0f, 3.0f).toughness(2.0f).knockbackResistance(0.05f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.3f, 0.2f, 0.2f, 0.2f),
            new HeadMaterialStats(360, 7.0f, Tiers.IRON, 3.5f),
            new PhantomCoreMaterialStats(1,16)
    ),
    terra_steel(
            armor(100, 4.0f, 8.0f, 6.0f, 3.0f).toughness(3.0f).knockbackResistance(1.5f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.7f, 0.55f, 0.55f, 0.6f),
            new HeadMaterialStats(1500, 7.0f, Tiers.DIAMOND, 6.0f),
            new PhantomCoreMaterialStats(2,32)
    ),
    elementium(
            armor(100, 2.0f, 6.0f, 5.0f, 2.0f).toughness(1.0f).knockbackResistance(0.15f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.3f, 0.25f, 0.2f, 0.23f),
            new HeadMaterialStats(750, 6.0f, Tiers.DIAMOND, 4.0f),
            new PhantomCoreMaterialStats(2,20)
    ),
    gaia(
            armor(120, 4.0f, 10.0f, 7.0f, 4.0f).toughness(4.0f).knockbackResistance(2.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.8f, 0.45f, 0.35f, 0.55f),
            new HeadMaterialStats(1550, 6.0f, Tiers.NETHERITE, 6.0f),
            new PhantomCoreMaterialStats(3,64)
    ),
    amphithere_feather(
            new FletchingMaterialStats(0.2f,0.2f)
    ),
    chimera_gamma(
            armor(100, 5.0f, 9.0f, 8.0f, 4.0f).toughness(5.0f).knockbackResistance(2.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.7f, 0.3f, 0.4f, 0.6f),
            new HeadMaterialStats(1862, 7f, Tiers.NETHERITE, 6.0f),
            new GripMaterialStats(0.5f,0.6f,5f),
            new LimbMaterialStats(1200,0.4f,0.3f,0.3f),
//            new CharmChainMaterialStats(0.4f, 60, 0.5f, 0.2f, 0.4f, 0.3f),
//            STStatlessMaterialStats.CHARM_CORE,
            new EnergyUnitMaterialStats(200000, 1.0f),
            new LaserMediumMaterialStats(50f, 0.35f, 20),
            new PhantomCoreMaterialStats(4,50),
            new SoulGathererMaterialStats(12f, 0.5f)
    ),
    dragon_sinew(
            StatlessMaterialStats.BOWSTRING
    ),
    buddysteel(
            armor(100, 3.0f, 8.0f, 6.0f, 3.0f).toughness(1.0f).knockbackResistance(0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.2f, 0.2f, 0.2f, 0.2f),
            new HeadMaterialStats(456, 5f, Tiers.IRON, 2.0f),
            new GripMaterialStats(0.2f,0.1f,2f),
            new LimbMaterialStats(400,0.1f,0.1f,0.1f)
    ),
    charged_buddysteel(
            armor(120, 3.0f, 8.0f, 6.0f, 3.0f).toughness(2.0f).knockbackResistance(1.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.3f, 0.3f, 0.3f, 0.3f),
            new HeadMaterialStats(756, 6f, Tiers.DIAMOND, 3.0f),
            new GripMaterialStats(0.3f,0.2f,3f),
            new LimbMaterialStats(700,0.2f,0.2f,0.2f)
    ),
    crimson_buddysteel(
            armor(140, 3.0f, 8.0f, 6.0f, 3.0f).toughness(2.0f).knockbackResistance(1.5f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.4f, 0.4f, 0.4f, 0.4f),
            new HeadMaterialStats(1056, 7f, Tiers.DIAMOND, 4.0f),
            new GripMaterialStats(0.4f,0.3f,4f),
            new LimbMaterialStats(1000,0.3f,0.3f,0.3f)
    ),
    void_buddysteel(
            armor(160, 4.0f, 9.0f, 7.0f, 4.0f).toughness(2.0f).knockbackResistance(1.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.5f, 0.5f, 0.5f, 0.5f),
            new HeadMaterialStats(1356, 7f, Tiers.NETHERITE, 5.0f),
            new GripMaterialStats(0.5f,0.4f,5f),
            new LimbMaterialStats(1300,0.4f,0.4f,0.4f)
    ),
    perfect_buddysteel(
            armor(210, 5.0f, 11.0f, 9.0f, 5.0f).toughness(4.0f).knockbackResistance(2.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.7f, 0.7f, 0.7f, 0.7f),
            new HeadMaterialStats(1856, 9f, Tiers.NETHERITE, 7.0f),
            new GripMaterialStats(0.7f,0.7f,7f),
            new LimbMaterialStats(1800,0.6f,0.6f,0.6f)
    ),
    true_perfect_buddysteel(
            armor(230, 6.0f, 12.0f, 9.0f, 6.0f).toughness(5.0f).knockbackResistance(2.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.9f, 0.9f, 0.9f, 0.9f),
            new HeadMaterialStats(2136, 10f, Tiers.NETHERITE, 8.0f),
            new GripMaterialStats(0.9f,0.9f,9f),
            new LimbMaterialStats(2200,0.8f,0.8f,0.8f)
    ),
    mana_string(
            StatlessMaterialStats.BOWSTRING
    ),
    living_wood(
            StatlessMaterialStats.BINDING
    ),
    living_rock(
            StatlessMaterialStats.BINDING
    ),
    aerialite(
            armor(100, 3.0f, 7.0f, 5.0f, 2.0f).toughness(3.0f).knockbackResistance(5.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.3f, 0.7f, 0.25f, 0.3f),
            new HeadMaterialStats(956, 9f, Tiers.NETHERITE, 5.0f)
    ),
    shadowium(
            armor(80, 3.0f, 7.0f, 5.0f, 2.0f).toughness(3.0f).knockbackResistance(2.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.1f, 0.7f, 0.25f, 0.5f),
            new HeadMaterialStats(856, 3f, Tiers.NETHERITE, 5.0f)
    ),
    photonium(
            armor(140, 3.0f, 7.0f, 5.0f, 2.0f).toughness(3.0f).knockbackResistance(4.0f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.6f, 0.7f, 0.25f, 0.2f),
            new HeadMaterialStats(1756, 9f, Tiers.NETHERITE, 4.0f)
    ),
    the_end(
            StatlessMaterialStats.BOWSTRING,
            StatlessMaterialStats.BINDING
    ),
    delusion(
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(0.2f, 0.6f, 0.6f, 0.45f),
            new HeadMaterialStats(1362, 7f, Tiers.NETHERITE, 6f),
            new GripMaterialStats(0.2f,0.6f,6f),
            new LimbMaterialStats(800,0.5f,0.5f,0.3f),
            new EnergyUnitMaterialStats(150000, 1.0f),
            new LaserMediumMaterialStats(40f, 0.25f, 18),
            new AxleMaterialStats(200, 8, 2.8f),
            new YoYoRingMaterialStats(3),
            new ChordMaterialStats(10)
    ),
    gluttonous(
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(1.2f, 0.3f, 0.2f, 0.2f),
            new HeadMaterialStats(2462, 4f, Tiers.NETHERITE, 3f),
            new GripMaterialStats(1.2f,0.25f,3f),
            new LimbMaterialStats(2400,0.5f,0.2f,0.3f),
            new EnergyUnitMaterialStats(250000, 2.0f),
            new AxleMaterialStats(600, 5, 2.8f)
    ),
    cold_iron_alloy(
            StatlessMaterialStats.BINDING,
            new HandleMaterialStats(-0.2f, 0.1f, 0.2f, -0.2f),
            new LimbMaterialStats(480,0.3f,0.1f,0.1f),
            new AxleMaterialStats(600, 20, 3.8f)
    ),
    paper(
            new SpellClothMaterialStats(5.0f, 5.0f, 0.3f, 15.0f)
    ),
    nefarious(
            armor(180, 6.0f, 10.0f, 7.0f, 4.0f).toughness(4.5f).knockbackResistance(2.5f),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            new HandleMaterialStats(0.5f, 0.6f, 0.35f, 0.45f),
            new HeadMaterialStats(1800, 8f, Tiers.NETHERITE, 7.0f),
            new GripMaterialStats(0.5f,0.4f,6f),
            new LimbMaterialStats(1400,0.7f,0.6f,0.6f),
//            new CharmChainMaterialStats(0.4f, 60, 0.5f, 0.2f, 0.4f, 0.3f),
//            STStatlessMaterialStats.CHARM_CORE,
            new EnergyUnitMaterialStats(300000, 1.2f),
            new LaserMediumMaterialStats(50f, 0.3f, 24),
            new PhantomCoreMaterialStats(4,50),
            new SoulGathererMaterialStats(15f, 0.6f)
    ),
//    apocalyptium(
//            armor(240, 6.0f, 11.0f, 9.0f, 6.0f).toughness(4f).knockbackResistance(3f),
//            true,
//            StatlessMaterialStats.BINDING,
//            StatlessMaterialStats.MAILLE,
//            new HandleMaterialStats(0.8f, 0.8f, 0.8f, 0.8f),
//            new HeadMaterialStats(2400, 8f, Tiers.NETHERITE, 10.0f)
//    )


    ;
    private final IMaterialStats[] stats;
    private final Builder armorStatBuilder;
    public final boolean allowShield;
    EnumMaterialStats(Builder builder, boolean allowShield , IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder =builder;
        this.allowShield = allowShield;
    }
    EnumMaterialStats(IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder =null;
        this.allowShield = false;
    }

    public IMaterialStats[] getStats() {
        return stats;
    }
    public Builder getArmorBuilder() {
        return armorStatBuilder;
    }

    public static Builder armor(int durabilityFactor,float helmet,float chestplate,float leggings,float boots){
        return PlatingMaterialStats.builder().durabilityFactor(durabilityFactor).armor(boots,leggings,chestplate,helmet);
    }
}
