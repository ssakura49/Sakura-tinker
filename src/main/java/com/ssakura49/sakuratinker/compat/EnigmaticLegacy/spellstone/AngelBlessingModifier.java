package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class AngelBlessingModifier extends BaseModifier {
    @Override
    public boolean isNoLevels(){
        return true;
    }
    @Override
    public float onModifyTakeDamage(IToolStackView armor, AttackData data, int level, float amount) {
        if (data.source().is(DamageTypes.FALL)) {
            return 0;
        }
        if (data.source().is(DamageTypes.FLY_INTO_WALL)) {
            return 0;
        }
        return amount;
    }

    @Override
    public void onProjectileShoot(IToolStackView bow, int level, LivingEntity shooter, Projectile projectile, AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        if (arrow != null) {
            arrow.setDeltaMovement(arrow.getDeltaMovement().scale(1.5));
            arrow.setBaseDamage(arrow.getBaseDamage() * 1.2);
        }
    }
}
