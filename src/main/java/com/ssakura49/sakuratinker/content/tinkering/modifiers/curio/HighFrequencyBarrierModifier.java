package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class HighFrequencyBarrierModifier extends CurioModifier implements TooltipModifierHook {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    private static final ResourceLocation REDUCTION_STACKS_KEY = new ResourceLocation(SakuraTinker.MODID, "high_freq_barrier_stacks");
    private static final ResourceLocation LAST_HIT_TIME_KEY = new ResourceLocation(SakuraTinker.MODID, "high_freq_barrier_last_hit");

    private static final int resetTicks = STConfig.COMMON.RESET_TICK.get();
    private static final double reductionPerHit = STConfig.COMMON.REDUCTION_PER_HIT.get();
    private static final double maxReduction = STConfig.COMMON.MAX_REDUCTION.get();

    @Override
    public void onCurioTakeDamagePre(IToolStackView curio, LivingHurtEvent event, LivingEntity entity, DamageSource source, int level) {
        ModDataNBT modData = curio.getPersistentData();
        long currentTime = entity.level().getGameTime();
        int currentStacks = modData.getInt(REDUCTION_STACKS_KEY);
        long lastHitTime = modData.get(LAST_HIT_TIME_KEY, (tag, key) -> {
            if (tag.contains(key, Tag.TAG_LONG)) {
                return tag.getLong(key);
            }
            return 0L;
        });
        if (resetTicks > 0 && currentTime - lastHitTime > resetTicks) {
            currentStacks = 0;
            modData.putInt(REDUCTION_STACKS_KEY, 0);
        }
        modData.put(LAST_HIT_TIME_KEY, LongTag.valueOf(currentTime));
        int maxStacks = (int)(maxReduction / reductionPerHit);
        if (currentStacks < maxStacks) {
            currentStacks++;
            modData.putInt(REDUCTION_STACKS_KEY, currentStacks);
        }
        float reduction = (float) Math.min(currentStacks * reductionPerHit,
                maxReduction);
        float newAmount = event.getAmount() * (1 - reduction);
        event.setAmount(newAmount);
    }
}
