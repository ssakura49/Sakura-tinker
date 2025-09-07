package com.ssakura49.sakuratinker.common.tinkering.modifiers.ranged;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class PrecisionStrikeModifier extends BaseModifier {
    @Override
    public void modifierArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, @NotNull AbstractArrow arrow, ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround, @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (!inGround && hasBeenShot && leftOwner) {
            Vec3 currentVelocity = arrow.getDeltaMovement();
            Vec3 acceleration = currentVelocity.normalize().scale(0.2 * entry.getLevel());
            Vec3 newVelocity = currentVelocity.add(acceleration);
            arrow.setDeltaMovement(newVelocity);
            arrow.hasImpulse = true;
        }
    }
}
