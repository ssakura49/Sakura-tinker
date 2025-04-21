package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.block.BurningLiquidBlock;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static slimeknights.tconstruct.fluids.block.BurningLiquidBlock.createBurning;

public class STFluids {
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister YKHC_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister EL_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister ISS_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister TF_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister REA_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister DE_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);
    public static final FluidDeferredRegister IAF_FLUIDS = new FluidDeferredRegister(SakuraTinker.MODID);

    protected static Map<FluidObject<ForgeFlowingFluid>,Boolean> FLUID_MAP = new HashMap<>();
    public static Set<FluidObject<ForgeFlowingFluid>> getFluids(){
        return FLUID_MAP.keySet();
    }
    public static Map<FluidObject<ForgeFlowingFluid>,Boolean> getFluidMap(){
        return FLUID_MAP;
    }
    private static FluidObject<ForgeFlowingFluid> registerHotFluid(FluidDeferredRegister register,String name,int temp,int lightLevel,int burnTime,float damage,boolean gas){
        FluidObject<ForgeFlowingFluid> object = register.register(name).type(hot(name,temp,gas)).bucket().block(createBurning(MapColor.COLOR_GRAY,lightLevel,burnTime,damage)).commonTag().flowing();
        FLUID_MAP.put(object,gas);
        return object;
    }
    private static FluidObject<ForgeFlowingFluid> registerFluid(FluidDeferredRegister register, String name, int temp, Function<Supplier<? extends FlowingFluid>, LiquidBlock> blockFunction, boolean gas){
        FluidObject<ForgeFlowingFluid> object = register.register(name).type(hot(name,temp,gas)).bucket().block(blockFunction).commonTag().flowing();
        FLUID_MAP.put(object,gas);
        return object;
    }

    public static final FluidObject<ForgeFlowingFluid> molten_youkai = registerHotFluid(YKHC_FLUIDS,"molten_youkai", 2400,4,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_etherium = registerHotFluid(EL_FLUIDS,"molten_etherium", 2500,4,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_arcane_salvage = registerHotFluid(ISS_FLUIDS,"molten_arcane_salvage", 2500,2,4, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_infinity = registerHotFluid(REA_FLUIDS,"molten_infinity", 9999,4,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_soul_sakura = registerHotFluid(FLUIDS,"molten_soul_sakura", 2000,4,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_fiery_crystal = registerHotFluid(TF_FLUIDS,"molten_fiery_crystal", 5600,15,4, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_nihilite = registerFluid(FLUIDS,"molten_nihilite", 2400,(Function<Supplier<? extends FlowingFluid>, LiquidBlock>) supplier -> new BurningLiquidBlock(supplier, FluidDeferredRegister.createProperties(MapColor.COLOR_GRAY, 15), 200, 8) {
        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            if (entity instanceof LivingEntity living) {
                living.hurt(LegacyDamageSource.any(living.damageSources().generic()).setBypassInvulnerableTime().setBypassArmor().setBypassEnchantment().setBypassMagic().setBypassShield().setMsgId("void"),2);
            }
        }
    },false);
    public static final FluidObject<ForgeFlowingFluid> molten_eezo = registerHotFluid(FLUIDS,"molten_eezo", 2500,1,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_arcane_alloy = registerHotFluid(ISS_FLUIDS,"molten_arcane_alloy", 2500,9,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_neutron = registerHotFluid(REA_FLUIDS,"molten_neutron", 2500,9,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_colorful = registerFluid(REA_FLUIDS,"molten_colorful", 8000,(Function<Supplier<? extends FlowingFluid>, LiquidBlock>) supplier -> new BurningLiquidBlock(supplier, FluidDeferredRegister.createProperties(MapColor.COLOR_GRAY, 15), 200, 8) {
        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            if (entity instanceof LivingEntity living) {
                living.heal(2);
            }
        }
    },false);
    public static final FluidObject<ForgeFlowingFluid> molten_crystal_matrix = registerHotFluid(REA_FLUIDS,"molten_crystal_matrix", 4500,9,2, 0.2f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_blood_bound_steel = registerHotFluid(FLUIDS, "molten_blood_bound_steel", 1300, 5, 2, 0.4f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_blood = registerHotFluid(FLUIDS, "molten_blood", 800, 8, 2, 0.5f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_dragon_fire_steel = registerHotFluid(IAF_FLUIDS, "molten_dragon_fire_steel", 1600, 11, 2, 0.5f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_dragon_ice_steel = registerHotFluid(IAF_FLUIDS, "molten_dragon_ice_steel", 1600, 11, 2, 0.5f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_dragon_lightning_steel = registerHotFluid(IAF_FLUIDS, "molten_dragon_lightning_steel", 1600, 11, 2, 0.5f, false);
    public static final FluidObject<ForgeFlowingFluid> molten_steady_alloy = registerHotFluid(FLUIDS, "molten_steady_alloy", 1600, 11, 2, 0.5f, false);


    private static FluidType.Properties hot(String name,int Temp,boolean gas) {
        return FluidType.Properties.create().density(gas?-2000:2000).viscosity(10000).temperature(Temp)
                .descriptionId(TConstruct.makeDescriptionId("fluid", name))
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .canSwim(false).canDrown(false)
                .pathType(BlockPathTypes.LAVA).adjacentPathType(null);
    }
}
