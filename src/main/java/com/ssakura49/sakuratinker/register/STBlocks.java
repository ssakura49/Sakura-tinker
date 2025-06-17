package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class STBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SakuraTinker.MODID);
    private static final BlockBehaviour.Properties ORE = DropExperienceBlock.Properties.of().requiresCorrectToolForDrops().strength(3.5F, 40f).sound(SoundType.STONE);
    private static final BlockBehaviour.Properties METAL = Block.Properties.of().requiresCorrectToolForDrops().strength(5F, 1200f).sound(SoundType.METAL);

    public static final RegistryObject<Block> orichalcum_ore_deepslate = BLOCKS.register("orichalcum_ore_deepslate", () -> new Block(ORE));
    public static final RegistryObject<Block> orichalcum_ore = BLOCKS.register("orichalcum_ore", () -> new Block(ORE));
    public static final RegistryObject<Block> EEZO_ORE = BLOCKS.register("eezo_ore", () -> new Block(ORE));
    public static final RegistryObject<Block> terracryst_ore = BLOCKS.register("terracryst_ore", () -> new Block(ORE));
    public static final RegistryObject<Block> terracryst_ore_deepslate = BLOCKS.register("terracryst_ore_deepslate", () -> new Block(ORE));
    public static final RegistryObject<Block> prometheum_ore = BLOCKS.register("prometheum_ore", () -> new Block(ORE));
    public static final RegistryObject<Block> prometheum_ore_deepslate = BLOCKS.register("prometheum_ore_deepslate", () -> new Block(ORE));
}
