package com.ssakura49.sakuratinker.library.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class AttackSpeedModifyEvent extends Event {
    private final Player player;
    private final float oldAttackCooldown;
    private double attackSpeed;
    private float cooldownModifier = 1.0F;

    public AttackSpeedModifyEvent(Player player, double attackSpeed, float oldAttackCooldown) {
        this.player = player;
        this.attackSpeed = attackSpeed;
        this.oldAttackCooldown = oldAttackCooldown;
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getAttackSpeed() {
        return this.attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getOldAttackCooldown() {
        return this.oldAttackCooldown;
    }

    public float getCooldownModifier() {
        return this.cooldownModifier;
    }

    public void setCooldownModifier(float cooldownModifier) {
        this.cooldownModifier = cooldownModifier;
    }

    public void mulCooldownModifier(float cooldownModifier) {
        this.setCooldownModifier(this.getCooldownModifier() * cooldownModifier);
    }
}
