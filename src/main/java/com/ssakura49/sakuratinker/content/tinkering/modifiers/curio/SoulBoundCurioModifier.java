package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

public class SoulBoundCurioModifier extends CurioModifier {

    public static boolean curios = ModList.get().isLoaded("curios");
    public static final ModifierId SOUL_BOUND_CURIO = new ModifierId(new ResourceLocation(SakuraTinker.MODID, "soul_bound_curio"));

    @Override
    public boolean isNoLevels() {
        return true;
    }

    public static void addCuriosDropListener() {
        if (curios) {
            MinecraftForge.EVENT_BUS.<DropRulesEvent>addListener(event -> {
                Entity entity = event.getEntity();
                if (!(entity instanceof ServerPlayer player)) {
                    return;
                }
                event.addOverride(stack -> {
                    ToolStack tool = ToolStack.from(stack);
                    if (!tool.isBroken() && tool.getModifiers().getLevel(SOUL_BOUND_CURIO) > 0) {
                        return true;
                    }
                    return false;
                }, DropRule.ALWAYS_KEEP);
            });
        }
    }
}
