package com.ssakura49.sakuratinker.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class BlockUtil {
    public static Optional<BlockPos> findLightningRod(ServerLevel serverLevel, BlockPos blockPos) {
        return findLightningRod(serverLevel, blockPos, 128);
    }

    public static Optional<BlockPos> findRangeLightningRod(ServerLevel serverLevel, BlockPos blockPos, int range) {
        return findLightningRod(serverLevel, blockPos, range);
    }

    public static Optional<BlockPos> findLightningRod(ServerLevel serverLevel, BlockPos blockPos, int range) {
        Optional<BlockPos> optional = serverLevel.getPoiManager().findClosest(
                (poiTypeHolder) -> poiTypeHolder.is(PoiTypes.LIGHTNING_ROD),
                (blockPos1) -> blockPos1.getY() == serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos1.getX(), blockPos1.getZ()) - 1, blockPos, range, PoiManager.Occupancy.ANY);
        return optional.map((blockPos1) -> blockPos1.above(1));
    }

    public static Optional<BlockPos> findNetherPortal(ServerLevel serverLevel, BlockPos blockPos, int range) {
        return serverLevel.getPoiManager().findClosest(
                (poiTypeHolder) -> poiTypeHolder.is(PoiTypes.NETHER_PORTAL),
                blockPos, range, PoiManager.Occupancy.ANY);
    }
}
