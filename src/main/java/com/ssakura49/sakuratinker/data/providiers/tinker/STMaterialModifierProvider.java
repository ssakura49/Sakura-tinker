package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.data.enums.EnumMaterial;
import com.ssakura49.sakuratinker.data.enums.EnumMaterialModifier;
import com.ssakura49.sakuratinker.data.enums.EnumTconMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.tools.data.material.MaterialTraitsDataProvider;

public class STMaterialModifierProvider extends MaterialTraitsDataProvider {
    public STMaterialModifierProvider(PackOutput packOutput) {
        super(packOutput, new STMaterialProvider(packOutput));
    }

    @Override
    protected void addMaterialTraits() {
        for (EnumMaterial material : EnumMaterial.values()){
            for (EnumMaterialModifier materialModifier:material.modifiers){
                if (materialModifier.statType==null){
                    addDefaultTraits(material.id,materialModifier.modifiers);
                }
                else addTraits(material.id,materialModifier.statType,materialModifier.modifiers);
            }
        }
        for (EnumTconMaterial material:EnumTconMaterial.values()){
            for (EnumMaterialModifier modifier:material.modifiers){
                addTraits(material.id,modifier.statType,modifier.modifiers);
            }
        }
    }

    @Override
    public String getName() {
        return "Sakura Tinker Material Modifier Provider";
    }
}
