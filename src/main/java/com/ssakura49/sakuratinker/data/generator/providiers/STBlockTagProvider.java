package com.ssakura49.sakuratinker.data.generator.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class STBlockTagProvider extends BlockTagsProvider {
    public STBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SakuraTinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).replace(false).add(
                STBlocks.EEZO_ORE.get(),
                STBlocks.orichalcum_ore.get(),
                STBlocks.orichalcum_ore_deepslate.get(),
                STBlocks.prometheum_ore.get(),
                STBlocks.prometheum_ore_deepslate.get(),
                STBlocks.terracryst_ore.get(),
                STBlocks.terracryst_ore_deepslate.get()
        );
        tag(BlockTags.MINEABLE_WITH_PICKAXE).replace(false)
                .add(STBlocks.EEZO_ORE.get())
                .add(STBlocks.orichalcum_ore.get())
                .add(STBlocks.orichalcum_ore_deepslate.get())
                .add(STBlocks.prometheum_ore.get())
                .add(STBlocks.prometheum_ore_deepslate.get())
                .add(STBlocks.terracryst_ore.get())
                .add(STBlocks.terracryst_ore_deepslate.get());
    }
}
