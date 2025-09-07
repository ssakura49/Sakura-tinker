package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor.IngaRyu;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import java.util.ArrayList;
import java.util.List;

public class IngaRyuUtils {
    public static List<HeadMaterialStats> getMeleeStatsFromArmor(ToolStack armor) {
        ItemStack stack = armor.createStack();
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        List<MaterialVariantId> materialIds = MaterialIdNBT.from(stack).getMaterials();
        List<HeadMaterialStats> result = new ArrayList<>();

        for (MaterialVariantId variant : materialIds) {
            MaterialId id = variant.getId();
            registry.getMaterialStats(id, MaterialRegistry.MELEE_HARVEST).ifPresent(stats -> {
                if (stats instanceof HeadMaterialStats headStats) {
                    result.add(headStats);
                }
            });
        }
        return result;
    }

    public static List<ModifierEntry> getToolModifiersFromArmorMaterials(ToolStack armor) {
        ItemStack stack = armor.createStack();
        List<MaterialVariantId> variants = MaterialIdNBT.from(stack).getMaterials();
        IMaterialRegistry registry = MaterialRegistry.getInstance();

        List<ModifierEntry> all = new ArrayList<>();

        for (MaterialVariantId variant : variants) {
            MaterialId matId = variant.getId();
            List<ModifierEntry> traits = registry.getTraits(matId, MaterialRegistry.MELEE_HARVEST);
            all.addAll(traits);
        }
        return all;
    }
}
