package com.ssakura49.sakuratinker.content.items;

import com.ssakura49.sakuratinker.content.entity.terraprisma.TerraPrismEntity;
import com.ssakura49.sakuratinker.register.STEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class TerraPrismaItem extends Item {
    public TerraPrismaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);

            // 查找附近是否已经存在该玩家的棱镜
            List<TerraPrismEntity> existing = level.getEntitiesOfClass(
                    TerraPrismEntity.class,
                    player.getBoundingBox().inflate(32),
                    e -> e.getOwner() == player
            );

            if (existing.size() < 4) { // 最多允许 3 个
                TerraPrismEntity prism = new TerraPrismEntity(STEntities.TERRA_PRISMA.get(), level);
                prism.setOwner(player);
                prism.moveTo(player.getX(), player.getY() + 1.2, player.getZ(), player.getYRot(), 0);
                level.addFreshEntity(prism);
                level.playSound(null, player.blockPosition(),
                        SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.2F);
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
