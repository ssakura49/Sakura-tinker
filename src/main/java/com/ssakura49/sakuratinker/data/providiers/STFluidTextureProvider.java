package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STFluids;
import net.minecraft.data.PackOutput;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.mantle.fluid.texture.FluidTexture;
import slimeknights.mantle.registration.object.FluidObject;

public class STFluidTextureProvider extends AbstractFluidTextureProvider {
    public STFluidTextureProvider(PackOutput packOutput){
        super(packOutput, SakuraTinker.MODID);
    }

    @Override
    public void addTextures() {
        for (FluidObject<ForgeFlowingFluid> object: STFluids.getFluids()){
            this.commonFluid(object.getType());
        }
    }

    public FluidTexture.Builder commonFluid(FluidType fluid) {
        return super.texture(fluid).textures(SakuraTinker.location("block/fluid/" + ForgeRegistries.FLUID_TYPES.get().getKey(fluid).getPath() + "/"), false, false);
    }

    @Override
    public String getName() {
        return "Sakura Tinker Fluid Texture Provider";
    }
}
