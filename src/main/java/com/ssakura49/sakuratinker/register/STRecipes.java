package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.recipes.SoulSakuraSealRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.SimpleRecipeSerializer;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

public class STRecipes {
    protected static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SakuraTinker.MODID);

    public static final RegistryObject<RecipeSerializer<SoulSakuraSealRecipe>> SOUL_SAKURA_SEAL_RECIPE =
            RECIPE_SERIALIZERS.register("soul_sakura_seal", () ->
                    new SimpleRecipeSerializer<>(SoulSakuraSealRecipe::new));

    public static void init(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
