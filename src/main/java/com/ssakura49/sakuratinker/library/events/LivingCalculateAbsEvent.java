package com.ssakura49.sakuratinker.library.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class LivingCalculateAbsEvent extends Event {
    private final LivingEntity entity;
    private final DamageSource source;

    public LivingCalculateAbsEvent(LivingEntity entity, DamageSource source) {
        this.entity = entity;
        this.source = source;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public Entity getAttacker() {
        return this.source.getEntity();
    }

    @Nullable
    public LivingEntity getLivingAttacker() {
        Entity var2 = this.source.getEntity();
        LivingEntity var10000;
        if (var2 instanceof LivingEntity liv) {
            var10000 = liv;
        } else {
            var10000 = null;
        }

        return var10000;
    }

    public static class Armor extends LivingCalculateAbsEvent {
        private final float damageBeforeArmor;
        private float damageAfterArmor;

        public Armor(LivingEntity entity, DamageSource source, float damageBeforeArmor, float damageAfterArmor) {
            super(entity, source);
            this.damageBeforeArmor = damageBeforeArmor;
            this.damageAfterArmor = damageAfterArmor;
        }

        public float getDamageBeforeArmor() {
            return this.damageBeforeArmor;
        }

        public float getDamageAfterArmor() {
            return this.damageAfterArmor;
        }

        public void setDamageAfterArmor(float damageAfterArmor) {
            this.damageAfterArmor = damageAfterArmor;
        }

        public void setOriginDamage() {
            this.damageAfterArmor = this.damageBeforeArmor;
        }
    }

    @Cancelable
    public static class Magic extends LivingCalculateAbsEvent {
        private final float damageBeforeMagic;

        public Magic(LivingEntity entity, DamageSource source, float damageBeforeMagic) {
            super(entity, source);
            this.damageBeforeMagic = damageBeforeMagic;
        }

        public float getDamageBeforeMagic() {
            return this.damageBeforeMagic;
        }
    }
}
