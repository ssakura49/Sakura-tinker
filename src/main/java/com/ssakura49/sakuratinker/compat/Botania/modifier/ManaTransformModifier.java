package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;

public class ManaTransformModifier extends BaseModifier {
    @Override
    public void finishHarvest(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context, int harvested) {
        if (harvested <= 0 || context.getWorld().isClientSide) return;

        Level level = context.getWorld();
        BlockState state = context.getState();
        BlockPos pos = context.getPos();

        List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class,
                new AABB(pos).inflate(1.5),
                e -> e != null && e.isAlive() && e.getDeltaMovement().lengthSqr() > 0.0001
        );

        for (ItemEntity item : drops) {
            ItemStack stack = item.getItem();
            if (state.is(BlockTags.LOGS) && stack.is(ItemTags.LOGS)) {
                item.setItem(new ItemStack(BotaniaBlocks.livingwood.asItem(), stack.getCount()));
            }
            else if (state.is(Blocks.STONE) && stack.is(Items.STONE)) {
                item.setItem(new ItemStack(BotaniaBlocks.livingrock.asItem(), stack.getCount()));
            }
        }
    }
}
