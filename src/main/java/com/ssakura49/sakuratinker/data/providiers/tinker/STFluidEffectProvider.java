package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STEffects;
import com.ssakura49.sakuratinker.register.STFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.data.tinkering.AbstractFluidEffectProvider;
import slimeknights.tconstruct.library.modifiers.fluid.FluidMobEffect;
import slimeknights.tconstruct.library.modifiers.fluid.TimeAction;
import slimeknights.tconstruct.library.modifiers.fluid.block.MobEffectCloudFluidEffect;

public class STFluidEffectProvider extends AbstractFluidEffectProvider {
    public STFluidEffectProvider(PackOutput output) {
        super(output, SakuraTinker.MODID);
    }

    @Override
    protected void addFluids() {
        addFluid(STFluids.molten_colorful.get(), 10)
                .fireDamage(2.0f)
                .addEntityEffects(FluidMobEffect.builder()
                        .effect(STEffects.MORTAL_WOUND.get(), 200,1)
                        .buildEntity(TimeAction.ADD))
                .addBlockEffect(new MobEffectCloudFluidEffect(FluidMobEffect.builder()
                        .effect(STEffects.MORTAL_WOUND.get(), 200,1)
                        .buildCloud()
                        .effects()));
    }

    public static ICondition modLoaded(String modId) {
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS, new ModLoadedCondition(modId));
    }
    public static ICondition tagFilled(TagKey<Item> tagKey) {
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS, new TagFilledCondition<>(tagKey));
    }

    @Override
    public String getName() {
        return "Sakura Tinker Fluid Effect Provider";
    }
}
