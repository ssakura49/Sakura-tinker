package com.ssakura49.sakuratinker.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.utils.EquipmentUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At(value = "HEAD"),method = "bobHurt",cancellable = true)
    public void cancleShaking(PoseStack p_109118_, float p_109119_, CallbackInfo ci){
        Entity entity = ((GameRenderer) (Object) this).getMinecraft().getCameraEntity();
        if (entity instanceof LivingEntity living){
            if (EquipmentUtil.isNullAlmightyAntiStun(living) || EquipmentUtil.isBloodBurnAntiStun(living)){
                ci.cancel();
            }
        }
    }
}
