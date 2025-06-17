package com.ssakura49.sakuratinker.plugin.jei;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.recipes.SoulSakuraSealRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import java.util.List;

@JeiPlugin
public class SakuraTinkerJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(SakuraTinker.MODID, "jei_plugin");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SealRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RegistryAccess access = level.registryAccess();
            RecipeManager manager = level.getRecipeManager();

            List<SoulSakuraSealRecipe> recipes = RecipeHelper.getJEIRecipes(
                    access,
                    manager,
                    TinkerRecipeTypes.TINKER_STATION.get(),
                    SoulSakuraSealRecipe.class
            );
            registration.addRecipes(SealRecipeCategory.SEAL, recipes);
        }
    }
}
