package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.IdGS;
import com.ssakura49.sakuratinker.client.InCommandCustomRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RendererRegister {
    public record CS(InCommandCustomRenderer renderer) {
    }
    public static Map<Integer, CS> RegisteredCustomRenderers = new HashMap<>();

    public static void registerCustomRenderer(InCommandCustomRenderer customRenderer) {
        IdGS.setID(customRenderer.renderer);
        RegisteredCustomRenderers.put(IdGS.id(customRenderer.renderer), new CS(customRenderer));
    }
}
