package com.ssakura49.sakuratinker.utils.tinker;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 独立工具冷却管理器
 * 基于 ToolStackMixin 注入的 etstlib_tool_uuid 来做区分
 * UUID toolId = ((IToolUuidGetter) tool).etstlib$getUuid();
 */
public class ToolCooldownManager {
    private static final Map<UUID, Map<UUID, Integer>> playerCooldowns = new HashMap<>();

    /**
     * 设置冷却
     * @param player 玩家
     * @param toolUuid 工具的UUID
     * @param cooldownTick 冷却时长（tick）
     */
    public static void setCooldown(Player player, UUID toolUuid, int cooldownTick) {
        Map<UUID, Integer> toolMap = playerCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
        toolMap.put(toolUuid, player.tickCount + cooldownTick);
    }

    /**
     * 是否冷却中
     */
    public static boolean isOnCooldown(Player player, UUID toolUuid) {
        Map<UUID, Integer> toolMap = playerCooldowns.get(player.getUUID());
        if (toolMap == null) return false;
        Integer expireTick = toolMap.get(toolUuid);
        if (expireTick == null) return false;
        return expireTick > player.tickCount;
    }

    /**
     * 获取剩余冷却
     */
    public static int getRemaining(Player player, UUID toolUuid) {
        Map<UUID, Integer> toolMap = playerCooldowns.get(player.getUUID());
        if (toolMap == null) return 0;
        Integer expireTick = toolMap.get(toolUuid);
        if (expireTick == null) return 0;
        return Math.max(0, expireTick - player.tickCount);
    }

    /**
     * 每tick清理过期数据
     * 建议在 ServerTickEvent 或 PlayerTickEvent 中调用
     * {@link } com.ssakura49.sakuratinker.event.onAbsorptionPlayerTick  在这里进行清除
     *
     */
    public static void tick(Player player) {
        Map<UUID, Integer> toolMap = playerCooldowns.get(player.getUUID());
        if (toolMap == null) return;

        toolMap.entrySet().removeIf(entry -> entry.getValue() <= player.tickCount);
    }
}