package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc.treasuremodifier;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.ItemUtil;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TreasureModifier extends BaseModifier {
    private static final String TREASURE_TOGGLE_LOOT_KEY_ID = "key.sakuratinker.treasure_toggle_loot";

    private static final Component ENERGY_INSUFFICIENT = Component.translatable("message.sakura_tinker.energy_insufficient");
    private static final Component TREASURE_FILLED = Component.translatable("message.sakura_tinker.treasure_filled");

    public static final ResourceLocation ENERGY_KEY_PREFIX = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "treasure_energy");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierKillLivingTarget(IToolStackView tool, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
        if (!(attacker instanceof Player player) || player.level().isClientSide()) return;

        ResourceLocation dimId = getNormalizedDimensionId(player.level());
        if (!STConfig.isValidDimension(dimId)) return;

        ResourceLocation mobId = EntityType.getKey(target.getType());
        int gain = STConfig.getEnergyValueFromJson(mobId, dimId);
        if (gain <= 0) return;

        ResourceLocation energyKey = getEnergyKey(dimId);
        ModDataNBT data = tool.getPersistentData();
        int current = data.getInt(energyKey);
        int max = STConfig.getCapacityFromJson(dimId);

        int updated = Math.min(current + gain, max);
        if (updated != current) {
            data.putInt(energyKey, updated);
            showEnergyProgress(player, dimId, updated, max);
        }
    }

    private void showEnergyProgress(Player player, ResourceLocation dimId, int current, int max) {
        if (current >= max * 0.9f) {
            int percent = (int) ((float) current / max * 100);
            player.displayClientMessage(
                    Component.translatable("message.sakura_tinker.energy_progress", percent, dimId.getPath()),
                    true
            );
        }
    }

    @Override
    public InteractionResult afterBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
        Player player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;

        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.PASS;

        BlockPos pos = context.getClickedPos();
        if (!(level.getBlockState(pos).getBlock() instanceof ChestBlock)) return InteractionResult.PASS;

        ResourceLocation dimId = getNormalizedDimensionId(level);
        ResourceLocation energyKey = getEnergyKey(dimId);
        ModDataNBT data = tool.getPersistentData();

        int current = data.getInt(energyKey);
//        int required = STConfig.getCapacityFromJson(dimId);
//
//        if (current < required) {
//            player.displayClientMessage(ENERGY_INSUFFICIENT, true);
//            return InteractionResult.FAIL;
//        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ChestBlockEntity chest) {
            // 获取当前选中的战利品表
            TreasureModifierData treasureData = TreasureModifierData.get(tool);
            ResourceLocation currentLootTable = treasureData.getCurrentLootTable();

            if (currentLootTable == null) {
                player.displayClientMessage(Component.translatable("message.sakura_tinker.no_loot_table_selected"), true);
                return InteractionResult.FAIL;
            }

            int lootCost = STConfig.getLootTableCost(currentLootTable);
            if (current < lootCost) {
                player.displayClientMessage(ENERGY_INSUFFICIENT, true);
                return InteractionResult.FAIL;
            }

            // 先扣能量
            data.putInt(energyKey, current - lootCost);

            List<ItemStack> loot = ItemUtil.getDefaultLootStacks(currentLootTable, (ServerPlayer) player);

            if (!loot.isEmpty()) {
                for (ItemStack stack : loot) {
                    EntityUtil.spawnItem(level, pos.above(), stack);
                }

                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.2f);
                player.displayClientMessage(TREASURE_FILLED, true);
                return InteractionResult.SUCCESS;
            } else {
                // 没掉落也返还能量
                data.putInt(energyKey, current);
                player.displayClientMessage(Component.translatable("message.sakura_tinker.loot_table_empty"), true);
                return InteractionResult.FAIL;
            }
//            // 填充箱子
//            boolean success = tryFillChestOrDrop(level, pos, (ServerPlayer) player, new TreasureEntry(currentLootTable, lootCost, required));
//
//            if (success) {
//                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.2f);
//                player.displayClientMessage(TREASURE_FILLED, true);
//                return InteractionResult.SUCCESS;
//            } else {
//                // 失败返还能量
//                data.putInt(energyKey, current);
//                return InteractionResult.FAIL;
//            }
        }
        return InteractionResult.PASS;
    }

    private static ResourceLocation getNormalizedDimensionId(Level level) {
        ResourceKey<Level> dimKey = level.dimension();
        if (dimKey == Level.OVERWORLD) return ResourceLocation.fromNamespaceAndPath("minecraft", "overworld");
        if (dimKey == Level.NETHER) return ResourceLocation.fromNamespaceAndPath("minecraft", "the_nether");
        if (dimKey == Level.END) return ResourceLocation.fromNamespaceAndPath("minecraft", "the_end");
        return dimKey.location();
    }

    private static ResourceLocation getEnergyKey(ResourceLocation dimId) {
        return ResourceLocation.fromNamespaceAndPath(
                ENERGY_KEY_PREFIX.getNamespace(),
                ENERGY_KEY_PREFIX.getPath() + "/" + dimId.getNamespace() + "/" + dimId.getPath()
        );
    }

    /** 填充箱子或掉落 */
    public static boolean tryFillChestOrDrop(Level level, BlockPos pos, ServerPlayer player, TreasureEntry entry) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof Container container)) {
            return false;
        }

        List<ItemStack> loot = ItemUtil.getDefaultLootStacks(entry.lootTable(), player);
        Random random = (Random) level.getRandom();

        for (ItemStack stack : loot) {
            boolean added = false;

            // 先尝试随机空槽放入
            List<Integer> slots = IntStream.range(0, container.getContainerSize()).boxed().collect(Collectors.toList());
            Collections.shuffle(slots, random);

            for (int slot : slots) {
                ItemStack slotStack = container.getItem(slot);
                if (slotStack.isEmpty()) {
                    container.setItem(slot, stack);
                    added = true;
                    break;
                }
            }

            // 再尝试合并
            if (!added) {
                for (int slot : slots) {
                    ItemStack slotStack = container.getItem(slot);
                    if (ItemStack.isSameItemSameTags(slotStack, stack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
                        int transferable = Math.min(stack.getCount(), slotStack.getMaxStackSize() - slotStack.getCount());
                        slotStack.grow(transferable);
                        stack.shrink(transferable);
                        if (stack.isEmpty()) {
                            added = true;
                            break;
                        }
                    }
                }
            }
            if (!added) {
                EntityUtil.spawnItem(level, pos, stack);
            }
        }

        if (container instanceof Container) {
            ((Container) container).setChanged();
        }
        return true;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player == null) return;
        ResourceLocation dimId = getNormalizedDimensionId(player.level());
        ModDataNBT data = tool.getPersistentData();
        ResourceLocation energyKey = getEnergyKey(dimId);
        int energy = data.getInt(energyKey);
        int capacity = STConfig.getCapacityFromJson(dimId);
        String keyName = getKeyDisplay(tool, entry);
        TreasureModifierData treasureData = TreasureModifierData.get(tool);
        ResourceLocation currentLoot = treasureData.getCurrentLootTable();

        int lootCost = currentLoot != null ? STConfig.getLootTableCost(currentLoot) : 0;

        tooltip.add(Component.literal("§d聚宝能量: §f" + energy + " / " + capacity));
        tooltip.add(Component.literal("§b当前战利品表: §f" + (currentLoot == null ? "无" : currentLoot.toString())));
        tooltip.add(Component.literal("§e切换战利品表能量消耗: §f" + lootCost));
        tooltip.add(Component.literal("按 [")
                .append(Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .append("] 切换战利品表")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onKeyPress(IToolStackView tool, ModifierEntry modifier, Player player, String key) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!TREASURE_TOGGLE_LOOT_KEY_ID.equals(key)) return;
        TreasureModifierData data = TreasureModifierData.get(tool);
        ResourceLocation dimId = getNormalizedDimensionId(player.level());
        List<STConfig.TreasureModifierConfig.LootTableEntry> availableTables = STConfig.getLootTablesForDimension(dimId);

        if (availableTables.isEmpty()) return;

        List<ResourceLocation> lootIds = availableTables.stream()
                .map(e -> ResourceLocation.parse(e.id))
                .toList();

        int index = lootIds.indexOf(data.getCurrentLootTable());
        index = (index + 1) % lootIds.size();
        ResourceLocation next = lootIds.get(index);
        data.setCurrentLootTable(next);
        TreasureModifierData.set(tool, data);

        player.displayClientMessage(Component.literal("§d已切换战利品表为：§f" + next.toString()), true);
    }

    @Override
    public @Nullable String getKeyId(IToolStackView tool, ModifierEntry modifier) {
        return TREASURE_TOGGLE_LOOT_KEY_ID;
    }
}
