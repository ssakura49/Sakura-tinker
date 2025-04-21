package com.ssakura49.sakuratinker.compat.IceAndFireCompat.modifiers;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.entity.Entity;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BreathResistanceModifier extends BaseModifier {
    @Override
    public float onModifyTakeDamage(IToolStackView armor, AttackData data, int level, float amount) {
        Entity entity = data.getDirect();
        if (entity instanceof EntityFireDragon || entity instanceof EntityIceDragon || entity instanceof EntityLightningDragon) {
            return (amount * (1.0F - 0.1F * (float)level));
        }
        return amount;
    }
}
