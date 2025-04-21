package com.ssakura49.sakuratinker.mixin;

import com.ssakura49.sakuratinker.library.damagesource.PercentageBypassArmorSource;
import com.ssakura49.sakuratinker.utils.MixinTemp;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At(value = "HEAD"),method = "getDamageAfterArmorAbsorb")
    public void getDamage(DamageSource source0, float amount, CallbackInfoReturnable<Float> cir){
        MixinTemp.damageBeforeArmorAbs = amount;
    }

    @Inject(at = @At(value = "RETURN"),method = "getDamageAfterArmorAbsorb",cancellable = true)
    public void percentageBypass(DamageSource source0, float amount, CallbackInfoReturnable<Float> cir){
        if (source0 instanceof PercentageBypassArmorSource source) {
            float absorbedAmount = cir.getReturnValueF()*(1-source.getPercentage());
            cir.setReturnValue(absorbedAmount+MixinTemp.damageBeforeArmorAbs*source.getPercentage());
        }
    }
}
