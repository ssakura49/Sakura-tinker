package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class OmnipotenceModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

//    public OmnipotenceModifier() {
//        MinecraftForge.EVENT_BUS.addListener(this::onLivingDamage);
//    }
//
//    @SubscribeEvent
//    public void onLivingDamage(LivingDamageEvent event) {
//        DamageSource source = event.getSource();
//        LivingEntity target = event.getEntity();
//        if (source.getEntity() instanceof Player player) {
//            ItemStack item = player.getMainHandItem();
//            if (this.getLevel(item) > 0) {
//                target.setHealth(0);
//                target.dropAllDeathLoot(source);
//               EntityUtil.die(target,source);
//            }
//        }
//    }
//    private int getLevel(ItemStack stack) {
//        if (stack.getItem() instanceof IModifiable) {
//            IToolStackView tool = ToolStack.from(stack);
//            return tool.getModifierLevel(this);
//        }
//        return 0;
//    }
}

