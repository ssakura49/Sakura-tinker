package com.ssakura49.sakuratinker.library.events;

import javax.annotation.Nullable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TinkerToolCriticalEvent extends Event {
    private final IToolStackView tool;
    private final ToolAttackContext context;
    private boolean isCritical;

    public TinkerToolCriticalEvent(IToolStackView tool, ToolAttackContext context, boolean isCritical) {
        this.tool = tool;
        this.context = context;
        this.isCritical = isCritical;
    }

    @Nullable
    public CriticalHitEvent getEvent() {
        return this.context.getPlayerAttacker() != null ? ForgeHooks.getCriticalHit(this.context.getPlayerAttacker(), this.context.getTarget(), this.isCritical, this.isCritical ? 1.5F : 1.0F) : null;
    }

    public IToolStackView getTool() {
        return this.tool;
    }

    public ToolAttackContext getContext() {
        return this.context;
    }

    public boolean getCritical() {
        return this.isCritical;
    }

    public void setCritical(boolean critical) {
        this.isCritical = critical;
    }
}
