package com.ssakura49.sakuratinker.library.tinkering.modifiers;

import com.ssakura49.sakuratinker.library.hooks.combat.CauseDamageModifierHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.IncrementalModifierEntry;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public abstract class ToolDamageModifier extends Modifier implements CauseDamageModifierHook, ValidateModifierHook, ModifierRemovalHook {
    public ToolDamageModifier(){}

    private static final float DAMAGE_PER_PERCENT = 0.01f;

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,
                STHooks.CAUSE_DAMAGE,
                ModifierHooks.VALIDATE,
                ModifierHooks.REMOVE
        );
    }

    @Override
    public @NotNull Component getDisplayName(@NotNull IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        Component component = IncrementalModifierEntry.addAmountToName(getDisplayName(entry.getLevel()), getPolish(tool), getPolishCapacity(tool, entry));
        return component.copy().withStyle(style -> style.withColor(0xFFD700));
    }

    @Override
    @Nullable
    public Component validate(@NotNull IToolStackView tool, @NotNull ModifierEntry entry) {
        int capacity = getPolishCapacity(tool, entry);
        if (getPolish(tool) > capacity) {
            setPolish(tool.getPersistentData(), capacity);
        }
        return null;
    }

    @Override @Nullable
    public Component onRemoved(IToolStackView tool, @NotNull Modifier modifier) {
        tool.getPersistentData().remove(getPolishKey());
        return null;
    }

    @Override
    public float onCauseDamage(IToolStackView tool, ModifierEntry modifier, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, float baseDamage, float currentDamage) {
        int polish = getPolish(tool);
        return currentDamage * (0.6f + polish / 100.0f);
    }

    protected ResourceLocation getPolishKey() {
        return getId();
    }

    public int getPolish(IToolStackView tool) {
        return tool.getPersistentData().getInt(this.getPolishKey());
    }

    protected float getPolishPercent(IToolStackView tool, ModifierEntry entry) {
        int current = getPolish(tool);
        int capacity = getPolishCapacity(tool, entry);
        return capacity > 0 ? (float)current / capacity : 0f;
    }

    public abstract int getPolishCapacity(IToolStackView tool, ModifierEntry entry);

    public void setPolish(ModDataNBT dataNBT, int amount) {
        dataNBT.putInt(this.getPolishKey(), Math.max(amount, 0));
    }

    public void setPolish(IToolStackView tool, ModifierEntry modifier, int amount) {
        this.setPolish(tool.getPersistentData(), Math.min(amount, this.getPolishCapacity(tool, modifier)));
    }

    protected void addPolish(IToolStackView tool, ModifierEntry modifier, int amount) {
        this.setPolish(tool, modifier, amount + this.getPolish(tool));
    }
}
