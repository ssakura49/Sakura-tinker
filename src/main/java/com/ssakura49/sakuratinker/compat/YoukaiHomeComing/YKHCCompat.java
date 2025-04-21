package com.ssakura49.sakuratinker.compat.YoukaiHomeComing;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers.BlurredModifier;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers.ClearStrikeModifier;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers.YoukaifiedModifier;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.modifiers.DanmakuBallModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class YKHCCompat {
    public static ModifierDeferredRegister YKHC_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static final StaticModifier<YoukaifiedModifier> Youkaified = YKHC_MODIFIERS.register("youkaified", YoukaifiedModifier::new);
    public static final StaticModifier<BlurredModifier> Blurred = YKHC_MODIFIERS.register("blurred", BlurredModifier::new);
    public static final StaticModifier<ClearStrikeModifier> ClearStrike = YKHC_MODIFIERS.register("clear_strike", ClearStrikeModifier::new);
    public static final StaticModifier<DanmakuBallModifier> DanmakuBall = YKHC_MODIFIERS.register("danmaku_ball", DanmakuBallModifier::new);

}
