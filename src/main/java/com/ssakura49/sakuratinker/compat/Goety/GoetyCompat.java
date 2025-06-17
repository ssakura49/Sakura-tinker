package com.ssakura49.sakuratinker.compat.Goety;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.DevourSoulModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulErosionModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulIntakeModifier;
import com.ssakura49.sakuratinker.compat.Goety.modifiers.SoulSeekerModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GoetyCompat {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(SakuraTinker.MODID);


    public static final Item.Properties PartItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties CastItem = new Item.Properties().stacksTo(64);
    public static final Item.Properties ToolItem = new Item.Properties().stacksTo(1);

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
}
