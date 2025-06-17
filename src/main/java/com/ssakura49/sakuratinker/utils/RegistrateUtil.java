package com.ssakura49.sakuratinker.utils;

import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;

public class RegistrateUtil extends L2Registrate {
    public RegistrateUtil(String id) {
        super(id);
    }

    public <T extends ModifierModule> void module(String id, RecordLoadable<? extends T> loader) {
        ModifierModule.LOADER.register(new ResourceLocation(this.getModid(), id), loader);
    }
}
