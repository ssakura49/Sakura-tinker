package com.ssakura49.sakuratinker.client.model;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public class BakedModels {
    public static final BakedModel[] firstFractalWeaponModels = new BakedModel[10];

    public static void init(ModelManager modelManager) {
        for(int i = 0; i < firstFractalWeaponModels.length; i++) {
            firstFractalWeaponModels[i] = modelManager
                    .getModel(new ResourceLocation(SakuraTinker.MODID, "item/misc/first_fractal_" + i));
        }
    }
}
