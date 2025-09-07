package com.ssakura49.sakuratinker.common.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Optional;

public class DwarvenCurioModifier extends CurioModifier {
    private static final float BOOST_DISTANCE = 64f;
    private static final float DEBUFF_RANGE = 128f;
    private static final float MINING_BONUS = 6;
    private static final float VELOCITY_BONUS = 0.05f;

    @Override
    public int getPriority() {
        return 85;
    }


    private static float getBoost(Level world, float y, ModifierEntry entry, float baseSpeed, float bonus) {
        if (y < BOOST_DISTANCE) {
            float scale = Mth.clamp((BOOST_DISTANCE - y) / BOOST_DISTANCE, 0, 2);
            return baseSpeed + (entry.getEffectiveLevel() * scale * bonus);
        }
        float baselineDebuff = Math.max(world.getMaxBuildHeight() - (DEBUFF_RANGE + BOOST_DISTANCE), 96);
        if (y > baselineDebuff) {
            if (y >= baselineDebuff + DEBUFF_RANGE) {
                return baseSpeed * 0.25f;
            }
            return baseSpeed * (1 - ((y - baselineDebuff) / DEBUFF_RANGE * 0.75f));
        }
        return baseSpeed;
    }

    @Override
    public void onCurioBreakSpeed(IToolStackView curio, ModifierEntry entry, PlayerEvent.BreakSpeed event, Player player) {
        Optional<BlockPos> pos = event.getPosition();
        if (pos.isEmpty()) {
            return;
        }
        ModifierEntry modifier = curio.getModifier(this);
        event.setNewSpeed(getBoost(event.getEntity().level(), pos.get().getY(), modifier, event.getNewSpeed(), event.getOriginalSpeed() * curio.getMultiplier(ToolStats.MINING_SPEED) * MINING_BONUS));
    }


    @Override
    public float modifyStat(IToolStackView curio, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float baseValue, float multiplier) {
        if (stat == ToolStats.VELOCITY) {
            return getBoost(entity.level(), (float)entity.getY(), modifier, baseValue, multiplier * VELOCITY_BONUS);
        }
        return baseValue;
    }
}
