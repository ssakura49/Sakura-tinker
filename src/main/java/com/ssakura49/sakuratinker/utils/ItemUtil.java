package com.ssakura49.sakuratinker.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class ItemUtil {
    public static List<ItemStack> getDefaultLootStacks(ResourceLocation lootTable, ServerPlayer player) {
        LootParams lootParams = (new LootParams.Builder(player.serverLevel()))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withLuck(player.getLuck())
                .create(LootContextParamSets.GIFT);
        return ItemUtil.getLootStacks(lootTable, player, lootParams);
    }

    public static List<ItemStack> getLootStacks(ResourceLocation lootTable, ServerPlayer player, LootParams lootParams) {
        MinecraftServer server = player.level().getServer();
        if (server == null || lootTable.equals(BuiltInLootTables.EMPTY)) {
            return List.of();
        }
        return server.getLootData().getLootTable(lootTable).getRandomItems(lootParams);
    }
}
