package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.data.enums.EnumMaterial;
import com.ssakura49.sakuratinker.data.enums.EnumTconMaterial;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;

public class STMaterialStatProvider extends AbstractMaterialStatsDataProvider {
    public STMaterialStatProvider(PackOutput packOutput) {
        super(packOutput, new STMaterialProvider(packOutput));
    }

    @Override
    protected void addMaterialStats() {
        for (EnumMaterial material: EnumMaterial.values()){
            if (material.stats.getArmorBuilder()!=null){
                addArmorStats(material.id, material.stats.getArmorBuilder(),material.stats.getStats());
                if (material.stats.allowShield){
                    addMaterialStats(material.id, material.stats.getArmorBuilder().buildShield());
                }
            }
            else addMaterialStats(material.id, material.stats.getStats());
        }
        for (EnumTconMaterial material:EnumTconMaterial.values()){
            addMaterialStats(material.id,material.stats.stats);
        }
    }

    @Override
    public @NotNull String getName() {
        return "Sakura Tinker Material Stats Data Provider";
    }
}
