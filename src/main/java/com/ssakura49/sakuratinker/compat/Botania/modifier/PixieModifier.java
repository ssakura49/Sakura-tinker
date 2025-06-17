package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.PixieEntity;

public class PixieModifier extends BaseModifier {
    private static final float SPAWN_CHANCE = 0.05F;
    private static final float PIXIE_DAMAGE = 2.0F;

    @Override
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context,
                                LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (!attacker.level().isClientSide && attacker.getRandom().nextFloat() < SPAWN_CHANCE * level) {
            spawnPixie(attacker, target);
        }
    }

    private void spawnPixie(LivingEntity summoner, LivingEntity target) {
        PixieEntity pixie = new PixieEntity(summoner.level());
        pixie.setPos(summoner.getX(), summoner.getY() + 1.0, summoner.getZ());
        pixie.setProps(target, summoner, 0, PIXIE_DAMAGE);
        summoner.level().addFreshEntity(pixie);
    }
}
