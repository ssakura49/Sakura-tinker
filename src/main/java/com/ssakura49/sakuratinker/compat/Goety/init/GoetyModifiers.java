package com.ssakura49.sakuratinker.compat.Goety.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.DevourSoulModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulErosionModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulIntakeModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulSeekerModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class GoetyModifiers {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<DevourSoulModifier> DevourSoul;
    public static StaticModifier<SoulIntakeModifier> SoulIntake;
    public static StaticModifier<SoulSeekerModifier> SoulSeeker;
    public static StaticModifier<SoulErosionModifier> SoulErosion;

    static {
        DevourSoul = MODIFIERS.register("devour_soul", DevourSoulModifier::new);
        SoulIntake = MODIFIERS.register("soul_intake",SoulIntakeModifier::new);
        SoulSeeker = MODIFIERS.register("soul_seeker", SoulSeekerModifier::new);
        SoulErosion = MODIFIERS.register("soul_erosion", SoulErosionModifier::new);
    }

    public static void register(IEventBus bus) {
        MODIFIERS.register(bus);
    }
}
