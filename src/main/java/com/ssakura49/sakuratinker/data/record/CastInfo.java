package com.ssakura49.sakuratinker.data.record;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.library.tools.part.PartCastItem;

import java.util.function.Supplier;

public record CastInfo(ItemObject<PartCastItem> item, boolean multiUse, CastType type) {
    public enum CastType {
        GOLD, SAND, RED_SAND
    }
}
