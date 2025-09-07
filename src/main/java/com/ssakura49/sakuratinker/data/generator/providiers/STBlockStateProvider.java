package com.ssakura49.sakuratinker.data.generator.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class STBlockStateProvider extends BlockStateProvider {
    public STBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, SakuraTinker.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<BlockItem> object : STItems.getListSimpleBlock()) {
            Block block = object.get().getBlock();
            ModelFile file = cubeAll(block);
            simpleBlock(block, file);
        }
    }
}
