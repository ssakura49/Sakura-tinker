package com.ssakura49.sakuratinker.compat.IceAndFireCompat.modifiers;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class DragonMartyrModifier extends BaseModifier {
    @Override
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (isDragon(target)) {
            return actualDamage * (1 + 0.2f * modifier.getLevel());
        }
        return actualDamage;
    }

    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (isDragon(target)) {
            float bonusDamage = (float) (arrow.getBaseDamage() * 0.2f * level);
            arrow.setBaseDamage(arrow.getBaseDamage() + bonusDamage);
        }
    }

    private boolean isDragon(LivingEntity entity) {
        return entity instanceof EntityFireDragon ||
                entity instanceof EntityIceDragon ||
                entity instanceof EntityLightningDragon;
    }
}
