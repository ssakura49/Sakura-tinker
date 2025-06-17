package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.content.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.utils.entity.EntityLookUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FirstFractal extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        HitResult hitResult = player.pick(80.0, 1.0F, true);
        Entity target = hitResult.getType() == HitResult.Type.ENTITY ?
                ((EntityHitResult)hitResult).getEntity() : null;
        trySpawnPhantomSword(tool, player,target);
        ToolDamageUtil.damageAnimated(tool,1, player);
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {

        trySpawnPhantomSword(tool,player,null);
        ToolDamageUtil.damageAnimated(tool,1, player);
    }

    private void trySpawnPhantomSword(IToolStackView tool, Player player, @Nullable Entity explicitTarget) {
        if (!player.level().isClientSide() && player.getAttackStrengthScale(0) >= 0.9) {
            float phantom_amount = tool.getStats().getInt(STToolStats.PHANTOM_AMOUNT);
            float range = tool.getStats().getInt(STToolStats.RANGE);
            Entity targetEntity = explicitTarget;
            if (targetEntity == null) {
                targetEntity = EntityLookUtil.getEntityLookedAt(player, 80.0);
            }
            BlockPos targetPos;
            if (targetEntity != null) {
                targetPos = BlockPos.containing(targetEntity.position()).offset(0, 2, 0);
            } else {
                HitResult hitResult = player.pick(80.0, 1.0F, true);
                targetPos = BlockPos.containing(hitResult.getLocation()).offset(0, 2, 0);
            }

            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x, y, z;
            ItemStack itemStack = player.getMainHandItem();

            for (int i = 0; i < phantom_amount; i++) {
                PhantomSwordEntity sword = new PhantomSwordEntity(player.level(), player, targetPos, itemStack);
                sword.setTool(itemStack.copy());
                sword.setOwner(player);
                sword.setDelay(5 + 5 * i);

                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = targetPos.getX() + range * Math.sin(k) * Math.cos(j);
                y = targetPos.getY() + range * Math.cos(k);
                z = targetPos.getZ() + range * Math.sin(k) * Math.sin(j);

                j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPos(x, y + 1.5, z);
                sword.faceTarget();
                player.level().addFreshEntity(sword);
            }

            PhantomSwordEntity sword2 = new PhantomSwordEntity(player.level(), player, targetPos, itemStack);
            sword2.setTool(itemStack);
            sword2.setOwner(player);

            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = targetPos.getX() + range * Math.sin(k) * Math.cos(j);
            y = targetPos.getY() + range * Math.cos(k);
            z = targetPos.getZ() + range * Math.sin(k) * Math.sin(j);

            sword2.setPos(x, y + 1.5, z);
            sword2.faceTarget();
            sword2.setVariety(9);
            player.level().addFreshEntity(sword2);
        }
    }
}
