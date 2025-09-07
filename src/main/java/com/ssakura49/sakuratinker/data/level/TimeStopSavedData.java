package com.ssakura49.sakuratinker.data.level;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimeStopSavedData extends SavedData {
    public MinecraftServer server;
    public Set<ResourceLocation> dimensions = new HashSet<>();
    public static TimeStopSavedData create(CompoundTag tag) {
        TimeStopSavedData data = new TimeStopSavedData();
        ListTag tags = tag.getList("Dimensions", 10);
        for (int i=0;i<tags.size();i++) {
            CompoundTag compoundTag = tags.getCompound(i);
            if (compoundTag.contains("id")) {
                data.dimensions.add(ResourceLocation.parse(compoundTag.getString("id")));
            }
        }
        return data;
    }
    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (ResourceLocation resourceLocation : dimensions) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("id", resourceLocation.toString());
            listTag.add(compoundTag);
        }
        return tag;
    }
    public void addTsDimension(ResourceKey<Level> dimension) {
        this.dimensions.add(dimension.location());
        setDirty();
    }
    public void removeTsDimension(ResourceKey<Level> dimension) {
        SakuraTinker.out("Remove:"+dimension);
        dimensions.remove(dimension.location());
        setDirty();
    }
    public @Nullable List<ResourceKey<Level>> asResourceKeys() {
        return dimensions.stream().map(r->ResourceKey.create(Registries.DIMENSION, r)).toList();
    }
    public @Nullable List<ServerLevel> asServerLevel(MinecraftServer server) {
        return dimensions.stream().map(r->server.getLevel(ResourceKey.create(Registries.DIMENSION, r))).toList();
    }
    public static TimeStopSavedData readOrCreate(MinecraftServer server) {
        TimeStopSavedData data = server.overworld().getDataStorage().computeIfAbsent(TimeStopSavedData::create, TimeStopSavedData::new, "time_stop_saved_data");
        data.server = server;
        return data;
    }
}
