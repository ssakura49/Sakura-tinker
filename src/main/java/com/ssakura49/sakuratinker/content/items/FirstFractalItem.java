/*
package com.ssakura49.sakuratinker.content.items;

import com.ssakura49.sakuratinker.content.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker.network.handler.PacketHandler;
import com.ssakura49.sakuratinker.network.packet.FractalLeftClickPack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import top.theillusivec4.curios.common.network.NetworkHandler;

public class FirstFractalItem extends SwordItem {
    public FirstFractalItem(Properties properties) {
        super(Tiers.NETHERITE, 6, -2F, new Properties()
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .durability(0));
        MinecraftForge.EVENT_BUS.addListener(this::leftClick);
        MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::attackEntity);
    }

    public void attackEntity(AttackEntityEvent evt) {
        if (!evt.getEntity().level().isClientSide()) {
            trySpawnPhantomSword(evt.getEntity(), evt.getTarget());
        }
    }

    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            PacketHandler.sendToServer(new FractalLeftClickPack());
        }
    }

    public void leftClickBlock(PlayerInteractEvent.LeftClickBlock evt) {
        if (evt.getEntity().level().isClientSide() && !evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            PacketHandler.sendToServer(new FractalLeftClickPack());
        }
    }

    public void trySpawnPhantomSword(Player player, Entity target) {
        if (!player.level().isClientSide() &&
                !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() &&
                player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this &&
                player.getAttackStrengthScale(0) == 1) {

            BlockPos targetpos = target == null ?
                    rayTraceFromEntity(player, 80F, true).getBlockPos().offset(0, 1, 0) :
                    BlockPos.containing(target.position()).offset(0, 1, 0);

            double range = 13D;
            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x, y, z;

            for(int i = 0; i < 3; i++) {
                PhantomSwordEntity sword = new PhantomSwordEntity(player.level(), player, targetpos);
                sword.setDelay(5 + 5 * i);
                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = targetpos.getX() + range * Math.sin(k) * Math.cos(j);
                y = targetpos.getY() + range * Math.cos(k);
                z = targetpos.getZ() + range * Math.sin(k) * Math.sin(j);
                j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPos(x, y, z);
                sword.faceTarget();
                player.level().addFreshEntity(sword);
            }

            PhantomSwordEntity sword2 = new PhantomSwordEntity(player.level(), player, targetpos);
            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = targetpos.getX() + range * Math.sin(k) * Math.cos(j);
            y = targetpos.getY() + range * Math.cos(k);
            z = targetpos.getZ() + range * Math.sin(k) * Math.sin(j);
            sword2.setPos(x, y, z);
            sword2.faceTarget();
            sword2.setVariety(9);
            player.level().addFreshEntity(sword2);
        }
    }

    public static BlockHitResult rayTraceFromEntity(Entity e, double distance, boolean fluids) {
        HitResult result = e.pick(distance, 1, fluids);
        return result instanceof BlockHitResult blockHit ? blockHit :
                BlockHitResult.miss(result.getLocation(), null, BlockPos.containing(result.getLocation()));
    }
}

 */
