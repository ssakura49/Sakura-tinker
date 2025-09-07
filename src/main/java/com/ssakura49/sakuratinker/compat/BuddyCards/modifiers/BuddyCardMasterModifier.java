package com.ssakura49.sakuratinker.compat.BuddyCards.modifiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.UUID;

public class BuddyCardMasterModifier extends BaseModifier {

    private static final UUID ATTACK_BONUS_UUID = UUID.fromString("2c16444e-5eb4-497d-91b7-60b06fcb2197");
    private static final UUID DIG_SPEED_BONUS_UUID = UUID.fromString("c3ddd63e-fadd-4510-9b55-8c960870ebd4");

    private static final ResourceLocation BASE_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "buddycard_base");
    private static final ResourceLocation NETHER_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "buddycard_nether");
    private static final ResourceLocation END_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "buddycard_end");
    private static final ResourceLocation TICK_COUNTER_KEY = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "buddycard_tick_counter");



    public BuddyCardMasterModifier() {
    }

    public static ResourceLocation getTickCounterKey() {
        return TICK_COUNTER_KEY;
    }

    // 用来存储临时的玩家卡组进度数据
    public static class PlayerProgressCache {
        public float baseProgress = 0f;
        public float netherProgress = 0f;
        public float endProgress = 0f;

        public double getAttackBonus() {
            return baseProgress * 0.5 + netherProgress * 1.0 + endProgress * 1.5;
        }

        public double getDigSpeedBonus() {
            return baseProgress * 0.1 + netherProgress * 0.2 + endProgress * 0.3;
        }

        public double getStatsBonus() {
            return baseProgress * 0.1 + netherProgress *0.1 + endProgress * 0.1;
        }
    }

    public static PlayerProgressCache getProgressCache(IToolStackView tool) {
        if (tool == null) {
            return new PlayerProgressCache();
        }
        return getPlayerProgressFromNBT(tool.getPersistentData());
    }

    public static PlayerProgressCache getPlayerProgressFromNBT(ModDataNBT persistentData) {
        PlayerProgressCache cache = new PlayerProgressCache();
        cache.baseProgress = persistentData.getFloat(BASE_KEY);
        cache.netherProgress = persistentData.getFloat(NETHER_KEY);
        cache.endProgress = persistentData.getFloat(END_KEY);
        return cache;
    }
    public void updateProgressToNBT(ModDataNBT persistentData, Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        BuddycardCollectionSaveData data = BuddycardCollectionSaveData.get(serverLevel);
        UUID uuid = player.getUUID();

        BuddycardSet baseSet = BuddycardsAPI.findSet("base");
        BuddycardSet netherSet = BuddycardsAPI.findSet("nether");
        BuddycardSet endSet = BuddycardsAPI.findSet("end");

        float base = baseSet == null ? 0f : data.checkPlayerSetCompletion(uuid, baseSet).calc();
        float nether = netherSet == null ? 0f : data.checkPlayerSetCompletion(uuid, netherSet).calc();
        float end = endSet == null ? 0f : data.checkPlayerSetCompletion(uuid, endSet).calc();

        persistentData.putFloat(BASE_KEY, base);
        persistentData.putFloat(NETHER_KEY, nether);
        persistentData.putFloat(END_KEY, end);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (!(attacker instanceof Player player)) return actualDamage;
        PlayerProgressCache cache = getPlayerProgressFromNBT(tool.getPersistentData());
        int level = modifier.getLevel();
        double bonus = cache.getAttackBonus();
        return (float)(actualDamage + bonus * level);
    }

    @Override
    public void modifierBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = event.getEntity();
        PlayerProgressCache cache = getPlayerProgressFromNBT(tool.getPersistentData());
        float bonus = (float) cache.getDigSpeedBonus();
        if (bonus > 0) {
            event.setNewSpeed(event.getNewSpeed() + bonus);
        }
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!(holder instanceof Player player) || level.isClientSide) return;
        ModDataNBT data = tool.getPersistentData();
        int counter = data.getInt(TICK_COUNTER_KEY);
        if (counter > 0) {
            data.putInt(TICK_COUNTER_KEY, counter - 1);
            return;
        }
        updateProgressToNBT(data, player);
        data.putInt(TICK_COUNTER_KEY, 5);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        PlayerProgressCache cache = getPlayerProgressFromNBT(tool.getPersistentData());
        tooltip.add(Component.literal("攻击力加成: +" + String.format("%.2f", cache.getAttackBonus())));
        tooltip.add(Component.literal("挖掘速度加成: +" + String.format("%.2f", cache.getDigSpeedBonus())));
    }


}
