package com.ssakura49.sakuratinker.content.tinkering.modifiers.ranged;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ProjectileTickModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class PrecisionStrikeModifier extends EtSTBaseModifier implements ProjectileTickModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PROJECTILE_TICK);
    }

    public void onProjectileTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull Projectile projectile, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner) {}
    @Override
    public void onArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (!inGround && hasBeenShot && leftOwner) {
            Vec3 currentVelocity = arrow.getDeltaMovement();
            Vec3 acceleration = currentVelocity.normalize().scale(0.2 * entry.getLevel());
            Vec3 newVelocity = currentVelocity.add(acceleration);
            arrow.setDeltaMovement(newVelocity);
            arrow.hasImpulse = true;
        }
    }
}
