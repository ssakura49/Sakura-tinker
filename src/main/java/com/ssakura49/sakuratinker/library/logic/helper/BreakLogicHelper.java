package com.ssakura49.sakuratinker.library.logic.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.common.ForgeHooks;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.RemoveBlockModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.ToolHarvestLogic;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Objects;

public class BreakLogicHelper {
    public BreakLogicHelper() {
    }

    public static void dropItems(BlockState state, BlockPos pos, Level level) {
        if (state.getBlock().getLootTable() == BuiltInLootTables.EMPTY) {
            Block.popResource(level, pos, new ItemStack(state.getBlock()));
        } else if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            BlockEntity blockEntity = level.getBlockEntity(pos);

            for(ItemStack drop : Block.getDrops(state, serverLevel, pos, blockEntity)) {
                Block.popResource(level, pos, drop);
            }
        }

    }

    public static boolean removeBlock(IToolStackView tool, ToolHarvestContext context) {
        Boolean removed = null;
        if (!tool.isBroken()) {
            for(ModifierEntry entry : tool.getModifierList()) {
                removed = ((RemoveBlockModifierHook)entry.getHook(ModifierHooks.REMOVE_BLOCK)).removeBlock(tool, entry, context);
                if (removed != null) {
                    break;
                }
            }
        }

        BlockState state = context.getState();
        ServerLevel world = context.getWorld();
        BlockPos pos = context.getPos();
        if (removed == null) {
            removed = state.onDestroyedByPlayer(world, pos, context.getPlayer(), context.canHarvest(), world.getFluidState(pos));
        }

        if (removed) {
            state.getBlock().destroy(world, pos, state);
        }
        return removed;
    }

    public static boolean breakBlock(ToolStack tool, ItemStack stack, ToolHarvestContext context) {
        ServerPlayer player = (ServerPlayer)Objects.requireNonNull(context.getPlayer());
        ServerLevel world = context.getWorld();
        BlockPos pos = context.getPos();
        GameType type = player.gameMode.getGameModeForPlayer();
        int exp = ForgeHooks.onBlockBreakEvent(world, type, player, pos);
        if (exp == -1) {
            return false;
        } else if (player.blockActionRestricted(world, pos, type)) {
            return false;
        } else if (player.isCreative()) {
            removeBlock(tool, context);
            return true;
        } else {
            BlockState state = context.getState();
            int damage = ToolHarvestLogic.getDamage(tool, world, pos, state);
            BlockEntity te = world.getBlockEntity(pos);
            boolean removed = removeBlock(tool, context);
            Block block = state.getBlock();
            if (removed) {
                block.playerDestroy(world, player, pos, state, te, stack);
            }

            if (removed && exp > 0) {
                block.popExperience(world, pos, exp);
            }

            if (!tool.isBroken()) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    ((BlockBreakModifierHook)entry.getHook(ModifierHooks.BLOCK_BREAK)).afterBlockBreak(tool, entry, context);
                }

                ToolDamageUtil.damageAnimated(tool, damage, player);
            }

            return true;
        }
    }
}
