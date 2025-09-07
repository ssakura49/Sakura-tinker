package com.ssakura49.sakuratinker.event.event.forge;

import com.google.common.collect.Queues;
import com.ssakura49.sakuratinker.common.tools.item.RevolverItem;
import com.ssakura49.sakuratinker.common.tools.item.RevolverItemBackPack;
import com.ssakura49.sakuratinker.library.interfaces.item.DelayTick;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Queue;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTickEvent {
//    @SubscribeEvent
//    public static void onServerTick(TickEvent.ServerTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            for (ServerLevel level : event.getServer().getAllLevels()) {
//                RevolverItemBackPack.RevolverShotManager.tick(level);
//            }
//        }
//    }
    public static final Queue<DelayTick> DELAYS = Queues.newArrayDeque();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            int size = DELAYS.size();
            for (int i = 0; i < size; i++) {
                Delay delay = (Delay) DELAYS.poll();
                if (delay != null) {
                    delay.run();
                }
            }
        }
    }

    public static void addDelay(int t, DelayTick ir) {
        DELAYS.add(new Delay(t, ir));
    }

    private static record Delay(int t, DelayTick ir) implements DelayTick {
        private Delay(int t, DelayTick ir) {
            this.t = t;
            this.ir = ir;
        }

        public void run() {
            if (this.t > 0) {
                DELAYS.add(new Delay(this.t - 1, this.ir));
            } else {
                this.ir.run();
            }

        }

        public int t() {
            return this.t;
        }

        public DelayTick ir() {
            return this.ir;
        }
    }
}
