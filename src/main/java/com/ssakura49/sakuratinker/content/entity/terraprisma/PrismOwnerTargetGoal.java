package com.ssakura49.sakuratinker.content.entity.terraprisma;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class PrismOwnerTargetGoal extends TargetGoal {
    private final TerraPrismEntity prism;
    private LivingEntity ownerLastHurt;

    public PrismOwnerTargetGoal(TerraPrismEntity prism) {
        super(prism, false);
        this.prism = prism;
    }

    @Override
    public boolean canUse() {
        if (prism.getOwner() instanceof Player owner) {
            ownerLastHurt = owner.getLastHurtMob();
            return ownerLastHurt != null
                    && ownerLastHurt.isAlive()
                    && !ownerLastHurt.equals(prism)
                    && prism.canAttack(ownerLastHurt, TargetingConditions.forCombat());
        }
        return false;
    }

    @Override
    public void start() {
        prism.setTarget(ownerLastHurt);
//        System.out.println("锁定主人攻击的目标: " + ownerLastHurt); // 调试输出
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        return prism.getTarget() != null && prism.getTarget().isAlive();
    }
}