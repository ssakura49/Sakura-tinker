package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;

public class GaiaSoulModifier extends BaseModifier {
    @Override
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (attacker instanceof Player player) {
            if (ManaItemHandler.instance().requestManaExact(attacker.getMainHandItem(), player, 160, true)) {
                ToolAttackUtil.attackEntitySecondary(magic(player), damageDealt * (float)level * 0.1F, target, attacker, false);
                ToolAttackUtil.attackEntitySecondary(freezing(player), damageDealt * (float)level * 0.1F, target, attacker, false);
                ToolAttackUtil.attackEntitySecondary(lightning(player), damageDealt * (float)level * 0.1F, target, attacker, false);
                ToolAttackUtil.attackEntitySecondary(fire(player), damageDealt * (float)level * 0.1F, target, attacker, false);
                ToolAttackUtil.attackEntitySecondary(fall(player), damageDealt * (float)level * 0.1F, target, attacker, false);
                ToolAttackUtil.attackEntitySecondary(drowning(player), damageDealt * (float)level * 0.1F, target, attacker, false);
            }
        }
    }
    public static LegacyDamageSource magic(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().magic().typeHolder(), attacker).setMagic();
    }
    public static LegacyDamageSource freezing(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().freeze().typeHolder(), attacker).setFreezing();
    }
    public static LegacyDamageSource lightning(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().lightningBolt().typeHolder(), attacker).setLightning();
    }
    public static LegacyDamageSource fire(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().inFire().typeHolder(), attacker).setFire();
    }
    public static LegacyDamageSource fall(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().fall().typeHolder(), attacker).setFall();
    }
    public static LegacyDamageSource drowning(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().drown().typeHolder(), attacker).setDrowning();
    }
}
