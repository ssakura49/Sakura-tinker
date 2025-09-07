package com.ssakura49.sakuratinker.common.entity.base;

import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IYoyo {

    /**
     * The weight of the yoyo. Used to calculate movement speed.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The weight of the yoyo.
     */
    double getWeight(ItemStack yoyo);

    /**
     * The maximum distance the yoyo can be from the player.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The maximum distance.
     */
    double getLength(ItemStack yoyo);

    /**
     * The maximum number of ticks the yoyo can stay out.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The maximum duration.
     */
    int getDuration(ItemStack yoyo);

    /**
     * The minimum time in between attacks made by the yoyo.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The attack interval.
     */
    int getAttackInterval(ItemStack yoyo);

    /**
     * The maximum number of items the yoyo can hold.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The maximum collected drops.
     */
    int getMaxCollectedDrops(ItemStack yoyo);

    /**
     * Should damage the yoyo by amount.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @param hand The hand the yoyo is held in.
     * @param amount The amount of damage.
     * @param entity The entity wielding the yoyo.
     */
    <T extends LivingEntity> void damageItem(ItemStack yoyo, InteractionHand hand, int amount, T entity);

    /**
     * Actions when the yoyo entity touches another entity.
     * @param yoyoStack The ItemStack that was used to launch the yoyo.
     * @param player The player wielding the yoyo.
     * @param hand The hand the yoyo is held in.
     * @param yoyo The yoyo entity.
     * @param target The target entity.
     */
    void entityInteraction(ItemStack yoyoStack, Player player, InteractionHand hand, YoyoEntity yoyo, Entity target);

    /**
     * Whether the yoyo interacts with blocks.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return True if the yoyo interacts with blocks, false otherwise.
     */
    boolean interactsWithBlocks(ItemStack yoyo);

    /**
     * Actions when the yoyo entity touches blocks.
     * @param yoyoStack The ItemStack that was used to launch the yoyo.
     * @param player The player wielding the yoyo.
     * @param world The world where the interaction happens.
     * @param pos The position of the block being touched.
     * @param state The state of the block being touched.
     * @param block The block being touched.
     * @param yoyo The yoyo entity.
     */
    void blockInteraction(ItemStack yoyoStack, Player player, Level world, BlockPos pos, BlockState state, Block block, YoyoEntity yoyo);

    /**
     * Actions to be performed during the yoyo entity's tick.
     * @param yoyoStack The ItemStack that was used to launch the yoyo.
     * @param yoyo The yoyo entity being updated.
     */
    default void onUpdate(ItemStack yoyoStack, YoyoEntity yoyo) {}

    /**
     * A multiplier for the yoyo entity's movement speed in water.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The water movement modifier.
     */
    default float getWaterMovementModifier(ItemStack yoyo) {
        return 0.3f;
    }

    /**
     * The color the cord should be.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @param ticks The number of ticks the yoyo has existed.
     * @return The cord color.
     */
    @OnlyIn(Dist.CLIENT)
    default int getCordColor(ItemStack yoyo, float ticks) {
        return 0xDDDDDD;
    }

    /**
     * The orientation the yoyo should be spinning in.
     * @param yoyo The ItemStack that was used to launch the yoyo.
     * @return The render orientation.
     */
    @OnlyIn(Dist.CLIENT)
    default RenderOrientation getRenderOrientation(ItemStack yoyo) {
        return RenderOrientation.Vertical;
    }
}
