package com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class DanmakuBallModifier extends BaseModifier {
    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot slot) {
        if (level.isClientSide) return;
        if (player.getCooldowns().isOnCooldown(tool.getItem())) {
            return;
        }
        YHDanmaku.Bullet bullet = switch (entry.getLevel()) {
            case 1 -> YHDanmaku.Bullet.BALL;
            case 2 -> YHDanmaku.Bullet.MENTOS;
            case 3 -> YHDanmaku.Bullet.BUBBLE;
            default -> YHDanmaku.Bullet.BALL;
        };
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), player, level);
        danmaku.setItem(bullet.get(DyeColor.BLUE).asStack());
        danmaku.setup(
                bullet.damage(),
                40,
                false,
                bullet.bypass(),
                RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 2)
        );
        level.addFreshEntity(danmaku);
        int baseCooldown = YHModConfig.COMMON.playerDanmakuCooldown.get();
        if (head.is(YHTagGen.TOUHOU_HAT) && head.getItem() instanceof TouhouHatItem item && item.support(DyeColor.WHITE)) {
            player.getCooldowns().addCooldown(tool.getItem(), baseCooldown / 2);
        } else {
            player.getCooldowns().addCooldown(tool.getItem(), baseCooldown);
        }
    }
}
