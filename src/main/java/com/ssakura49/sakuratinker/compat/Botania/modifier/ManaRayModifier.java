package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.content.entity.ManaRayEntity;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.xplat.XplatAbstractions;

public class ManaRayModifier extends BaseModifier {
    public ManaRayModifier() {
        super();
    }
    private static int getColorForLevel(Player player){
        float manaFraction = getPlayerManaFraction(player);
        return getManaColor(manaFraction);
    }
    public static float getPlayerManaFraction(Player player) {
        int totalMana = 0;
        int totalMaxMana = 0;

        for (ItemStack stack : ManaItemHandler.instance().getManaItems(player)) {
            ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
            if (manaItem != null) {
                totalMana += manaItem.getMana();
                totalMaxMana += manaItem.getMaxMana();
            }
        }

        if (totalMaxMana == 0) {
            return 0F;
        }
        return (float) totalMana / (float) totalMaxMana;
    }
    public static int getManaColor(float fraction) {
        fraction = clamp(fraction, 0f, 1f);

        int red = (int) ((1 - fraction) * 255);
        int green = 0;
        int blue = (int) (fraction * 255);
        int alpha = 255;

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (!level.isClientSide && (double)player.getAttackStrengthScale(0.0F) > 0.8 && ManaItemHandler.INSTANCE.requestManaExactForTool(new ItemStack(BotaniaItems.terraSword), player, 100, true)) {
            ManaRayEntity entity = new ManaRayEntity(level, player);
            Vec3 look = player.getLookAngle();
            ItemStack itemStack = player.getMainHandItem();
            entity.setTool(itemStack.copy());
            float motionModifier = (float) (4.0F + entry.getLevel());
            float damage = (float) (0.5 * tool.getStats().getInt(ToolStats.ATTACK_DAMAGE));
            entity.setGravity(0);
            entity.setDamage(damage);
            entity.setColor(getColorForLevel(player));
            entity.setMana(100 * entry.getLevel());
            entity.setStartingMana(100 * entry.getLevel());
            entity.shoot(look.x, look.y, look.z, motionModifier, 1.0f);
            player.level().addFreshEntity(entity);
            player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

    }
    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (!level.isClientSide && (double)player.getAttackStrengthScale(0.0F) > 0.8 && ManaItemHandler.INSTANCE.requestManaExactForTool(new ItemStack(BotaniaItems.terraSword), player, 100, true)) {
            ManaRayEntity entity = new ManaRayEntity(level, player);
            Vec3 look = player.getLookAngle();
            ItemStack itemStack = player.getMainHandItem();
            entity.setTool(itemStack.copy());
            float motionModifier = (float) (4.0F + entry.getLevel());
            float damage = (float) (0.5 * tool.getStats().getInt(ToolStats.ATTACK_DAMAGE));
            entity.setGravity(0);
            entity.setDamage(damage);
            entity.setColor(getColorForLevel(player));
            entity.setMana(100 * entry.getLevel());
            entity.setStartingMana(100 * entry.getLevel());
            entity.shoot(look.x, look.y, look.z, motionModifier, 1.0f);
            player.level().addFreshEntity(entity);
            player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
