package com.ssakura49.sakuratinker.content.tools.tiers;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class InfinityTiers implements Tier {
    public static Tier instance = new InfinityTiers();
    public InfinityTiers(){}

    public int getUses() {
        return 999;
    }

    public float getSpeed() {
        return 99f;
    }

    public float getAttackDamageBonus() {
        return 9999f;
    }

    public int getLevel() {
        return 99;
    }

    public int getEnchantmentValue() {
        return 99;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}
