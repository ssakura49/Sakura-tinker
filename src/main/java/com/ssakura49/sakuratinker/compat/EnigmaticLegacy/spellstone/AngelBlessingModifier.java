package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class AngelBlessingModifier extends BaseModifier {
    @Override
    public boolean isNoLevels(){
        return true;
    }
    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.is(DamageTypes.FALL)) {
            return 0;
        }
        if (source.is(DamageTypes.FLY_INTO_WALL)) {
            return 0;
        }
        return amount;
    }

    @Override
    public void onProjectileShoot(IToolStackView bow, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        if (arrow != null) {
            arrow.setDeltaMovement(arrow.getDeltaMovement().scale(1.5));
            arrow.setBaseDamage(arrow.getBaseDamage() * 1.2);
        }
    }
}
