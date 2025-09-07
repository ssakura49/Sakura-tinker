package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc.treasuremodifier;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class TreasureModifierData {
    private static final ResourceLocation KEY_ENERGY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "treasure_energy_current");
    private static final ResourceLocation KEY_LOOT_TABLE = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "treasure_loot_table");

    private int energy = 0;
    private ResourceLocation currentLootTable = null;

    // 从 ModDataNBT 读取
    public static TreasureModifierData get(IToolStackView tool) {
        ModDataNBT data = tool.getPersistentData();
        TreasureModifierData result = new TreasureModifierData();

        if (data.contains(KEY_ENERGY)) {
            result.energy = data.getInt(KEY_ENERGY);
        }

        if (data.contains(KEY_LOOT_TABLE)) {
            try {
                result.currentLootTable = ResourceLocation.parse(data.getString(KEY_LOOT_TABLE));
            } catch (ResourceLocationException e) {
                // 无效的资源位置，忽略
            }
        }

        return result;
    }

    // 保存到 ModDataNBT
    public static void set(IToolStackView tool, TreasureModifierData data) {
        ModDataNBT nbt = tool.getPersistentData();
        nbt.putInt(KEY_ENERGY, data.energy);

        if (data.currentLootTable != null) {
            nbt.putString(KEY_LOOT_TABLE, data.currentLootTable.toString());
        } else {
            nbt.remove(KEY_LOOT_TABLE);
        }
    }

    // Getter 和 Setter

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public ResourceLocation getCurrentLootTable() {
        return currentLootTable;
    }

    public void setCurrentLootTable(ResourceLocation currentLootTable) {
        this.currentLootTable = currentLootTable;
    }
}
