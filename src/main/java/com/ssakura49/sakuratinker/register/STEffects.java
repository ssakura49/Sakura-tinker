package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.content.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;


public class STEffects {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    public static final RegistryObject<MobEffect> IMMORTALITY = EFFECT.register("immortality", ImmortalityEffect::new);
    public static final RegistryObject<MobEffect> MORTAL_WOUND = EFFECT.register("mortal_wound", MortalWoundEffect::new);
    //public static final RegistryObject<MobEffect> UniversalBarrier = EFFECT.register("universal_barrier", UniversalBarrierEffect::new);
    public static final RegistryObject<MobEffect> TORTURE = EFFECT.register("torture", TortureEffect::new);
    public static final RegistryObject<MobEffect> BLOOD_BURN = EFFECT.register("blood_burn", BloodBurnEffect::new);
    public static final RegistryObject<MobEffect> GRAVITY = EFFECT.register("gravity", GravityEffect::new);
    public static final RegistryObject<MobEffect> VAMPIRING = EFFECT.register("vampiring", Vampiring::new);
    public static final RegistryObject<MobEffect> FungalParasites = EFFECT.register("fungal_parasites", FungalParasites::new);
    public static final RegistryObject<MobEffect> FusionBurn = EFFECT.register("fusion_burn", FusionBurnEffect::new);

    public STEffects() {
    }
}
