package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.menu.RevolverMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class STMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES, SakuraTinker.MODID
    );

    public static final RegistryObject<MenuType<RevolverMenu>> REVOLVER = MENUS.register("revolver",
            () -> IForgeMenuType.create((id, inv, data) -> {
                return new RevolverMenu(id, inv, inv.player.getItemInHand(inv.player.getUsedItemHand()));
            }));

}
