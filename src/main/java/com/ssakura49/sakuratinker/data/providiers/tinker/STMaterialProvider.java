package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.data.enums.EnumMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class STMaterialProvider extends AbstractMaterialDataProvider {
    public STMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        for (EnumMaterial material:EnumMaterial.values()){
            addMaterial(material.id,material.tier,8, material.craftable, material.hidden, material.condition);
        }
    }

    @Override
    public String getName() {
        return "Sakura Tinker Material Data Provider";
    }
}
