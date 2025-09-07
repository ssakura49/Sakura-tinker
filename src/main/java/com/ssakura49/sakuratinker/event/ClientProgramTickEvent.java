package com.ssakura49.sakuratinker.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class ClientProgramTickEvent extends TickEvent {
    public ClientProgramTickEvent(Phase phase) {
        super(Type.CLIENT, LogicalSide.CLIENT, phase);
    }
}