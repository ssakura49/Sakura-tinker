package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class STAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, SakuraTinker.MODID);

    public static final RegistryObject<Attribute> REALITY_SUPPRESSION =
            ATTRIBUTES.register("reality_suppression", () ->
                    new RangedAttribute("attribute.name." + SakuraTinker.MODID + ".reality_suppression", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> REALITY_SUPPRESSION_RESISTANCE =
            ATTRIBUTES.register("reality_suppression_resistance", () ->
                    new RangedAttribute("attribute.name." + SakuraTinker.MODID + ".reality_suppression_resistance", 1.0D, 0.0D, 2.0D).setSyncable(true));

    public static Attribute getRealitySuppression() {
        return REALITY_SUPPRESSION.get();
    }

    public static Attribute getRealitySuppressionResistance() {
        return REALITY_SUPPRESSION_RESISTANCE.get();
    }


}
