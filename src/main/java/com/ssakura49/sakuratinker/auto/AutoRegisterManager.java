package com.ssakura49.sakuratinker.auto;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.RendererRegister;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashSet;

public class AutoRegisterManager {
    public static volatile boolean handled = false;
    private final HashSet<AbsAutoRegisterHandler<?>> handlers = new HashSet<>();
    private static AutoRegisterManager INSTANCE;
//    public EnchantmentAutoRegisterHandler enchantment;
    public ItemAutoRegisterHandler item;
//    public SpellAutoRegisterHandler spell;
//    public BlockAutoRegisterHandler block;
    AutoRegisterManager() {
    }
    void init() {
        item = new ItemAutoRegisterHandler(STItems.ITEMS, "com/ssakura49/sakuratinker/items");
//        block = new BlockAutoRegisterHandler(TargetRegister.BLOCKS, "com/ssakura49/sakuratinker/blocks");
//        enchantment = new EnchantmentAutoRegisterHandler(TargetRegister.ENCHANTMENTS, "com/ssakura49/sakuratinker/enchantments");
//        spell = new SpellAutoRegisterHandler(TargetRegister.SPELLS, "com/ssakura49/sakuratinker/spells");
    }
    public static AutoRegisterManager INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AutoRegisterManager();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    public HashSet<AbsAutoRegisterHandler<?>> getHandlers() {
        return handlers;
    }

    public synchronized void register() {
        if (handled) return;
        handled = true;
        handlers.forEach(handler -> {
            SakuraTinker.push();
            SakuraTinker.out("%s Handle, class loader : %s", handler.getClass().getSimpleName(), handler.getClass().getClassLoader());
            SakuraTinker.push2();
            handler.handle();
            SakuraTinker.pop2();
            SakuraTinker.pop();
        });
        handlers.forEach(handler -> handler.deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }
    private static ItemAutoRegisterHandler ITEM_ARH;
    public static ItemAutoRegisterHandler ITEM_ARH() {
        if (ITEM_ARH == null)
            ITEM_ARH = INSTANCE().item;
        return ITEM_ARH;
    }
}