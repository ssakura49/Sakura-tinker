package com.ssakura49.sakuratinker.utils.data;

import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.utils.time.TimeStopUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

public class ClientLevelExpandedContext extends LevelExpandedContext {
    //当前玩家所在的维度
    //如果时停 为非空，否则 null
    public ResourceKey<Level> currentTimeStopDimension = null;
    public ClientLevelExpandedContext(Level level) {
        super(level);
    }
    public boolean isCurrentTS() {
        return currentTimeStopDimension != null;
    }
    @Override
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop ) {
            RendererUtils.isTimeStop_andSameDimension = TimeStopUtils.andSameDimension(level);
            if (RendererUtils.isTimeStop_andSameDimension) {
                ci.cancel();
            }
        } else RendererUtils.isTimeStop_andSameDimension = false;
    }
}
