package com.ssakura49.sakuratinker.mixin.Goety;

import com.Polarice3.Goety.utils.SEHelper;
import com.ssakura49.sakuratinker.compat.Goety.api.SoulDiscountProvider;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers.ApocalyptiumModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(SEHelper.class)
public abstract class SEHelperMixin {

    @Inject(
            method = "soulDiscount(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void injectSoulDiscount(LivingEntity living, CallbackInfoReturnable<Float> cir) {
        float original = cir.getReturnValue();
        AtomicInteger total = new AtomicInteger(0);
        ToolUtil.getAllModifiers(living).forEach(entry -> {
            if (entry.getModifier() instanceof SoulDiscountProvider provider) {
                total.addAndGet(provider.getSoulDiscount(living));
            }
        });

        cir.setReturnValue(original - (total.get() / 100f));
    }
}
