package com.ssakura49.sakuratinker.mixin.ExtraBotany;

import io.github.lounode.extrabotany.api.gaia.GaiaArena;
import io.github.lounode.extrabotany.common.lib.RegistryHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GaiaArena.class, remap = false)
public class GaiaArenaMixin {
    @Inject(method = "checkFeasibility", at = @At("RETURN"), cancellable = true)
    private static void injectModidSupport(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            String modid = RegistryHelper.getRegistryName(stack.getItem()).getNamespace();
            if (modid.equals("tconstruct") || modid.equals("sakuratinker")) {
                cir.setReturnValue(true);
            }
        }
    }
}
