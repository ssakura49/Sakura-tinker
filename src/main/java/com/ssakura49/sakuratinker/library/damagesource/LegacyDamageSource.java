package com.ssakura49.sakuratinker.library.damagesource;

import com.ssakura49.sakuratinker.register.STDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class LegacyDamageSource extends DamageSource {
    public String msgId=null;
    public ArrayList<ResourceKey<DamageType>> damageTypes =new ArrayList<>();
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos) {
        super(holder, directEntity, causingEntity, sourcePos);
    }
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        this(holder, directEntity, causingEntity, null);
    }
    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity) {
        this(holder, directEntity, directEntity, null);
    }
    public LegacyDamageSource(@NotNull LivingEntity directEntity) {
        this(directEntity.damageSources().generic().typeHolder(), directEntity, directEntity, null);
    }
    public LegacyDamageSource(DamageSource source){
        this(source.typeHolder(),source.getDirectEntity(),source.getEntity(),source.sourcePositionRaw());
    }

    public static LegacyDamageSource playerAttack(@NotNull Player player){
        return new LegacyDamageSource(player.damageSources().playerAttack(player));
    }
    public static LegacyDamageSource mobAttack(@NotNull LivingEntity living){
        return new LegacyDamageSource(living.damageSources().mobAttack(living));
    }
    public static LegacyDamageSource indirectMagic(@NotNull LivingEntity living){
        return new LegacyDamageSource(living.damageSources().magic().typeHolder(),living);
    }
    public static LegacyDamageSource directMagic(@NotNull Level level){
        return new LegacyDamageSource(level.damageSources().magic());
    }
    public static LegacyDamageSource any(@NotNull DamageSource source){
        return new LegacyDamageSource(source);
    }
    public static LegacyDamageSource any(Holder<DamageType> holder, @Nullable Entity directEntity){
        return new LegacyDamageSource(holder,directEntity);
    }
    public static LegacyDamageSource explosion(@NotNull Entity directEntity, Entity causingEntity){
        return new LegacyDamageSource(directEntity.damageSources().explosion(directEntity,causingEntity));
    }
    public static LegacyDamageSource explosion(@NotNull Explosion explosion){
        return new LegacyDamageSource(explosion.getDamageSource());
    }
    public static LegacyDamageSource thorns(@NotNull LivingEntity living){
        return new LegacyDamageSource(living.damageSources().thorns(living).typeHolder(),living);
    }
    public static LegacyDamageSource projectile(Entity projectile, @Nullable LivingEntity shooter) {
        LegacyDamageSource source;
        if (shooter != null) {
            if (shooter instanceof Player player) {
                source = new LegacyDamageSource(player.damageSources().playerAttack(player));
            } else {
                source = new LegacyDamageSource(shooter.damageSources().mobAttack(shooter));
            }
        } else {
            source = new LegacyDamageSource(projectile.damageSources().generic());
        }
        source.setProjectile();
        return source;
    }
    public static LegacyDamageSource overLoad(Entity source) {
        return new LegacyDamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(STDamageTypes.OVERLOAD_DAMAGE_TYPE), source).setBypassInvulnerableTime();
    }

    //对头盔造成额外伤害
    public LegacyDamageSource setDamageHelmet(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.DAMAGES_HELMET.location()));
        return this;
    }
    //无视护甲
    public LegacyDamageSource setBypassArmor(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_ARMOR.location()));
        return this;
    }
    //无视盾牌
    public LegacyDamageSource setBypassShield(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_SHIELD.location()));
        return this;
    }
    //无视无敌
    public LegacyDamageSource setBypassInvulnerability(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_INVULNERABILITY.location()));
        return this;
    }
    //无视无敌帧
    public LegacyDamageSource setBypassInvulnerableTime(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_COOLDOWN.location()));
        return this;
    }
    //无视效果
    public LegacyDamageSource setBypassMagic(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_RESISTANCE.location()));
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_EFFECTS.location()));
        return this;
    }
    //无视附魔
    public LegacyDamageSource setBypassEnchantment(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.BYPASSES_ENCHANTMENTS.location()));
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
        return this;
    }
    //火焰
    public LegacyDamageSource setFire(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FIRE.location()));
        return this;
    }
    //箭矢
    public LegacyDamageSource setProjectile(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_PROJECTILE.location()));
        return this;
    }
    //爆炸
    public LegacyDamageSource setExplosion(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_EXPLOSION.location()));
        return this;
    }
    //掉落
    public LegacyDamageSource setFall(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FALL.location()));
        return this;
    }
    //溺水
    public LegacyDamageSource setDrowning(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_DROWNING.location()));
        return this;
    }
    //冰冻
    public LegacyDamageSource setFreezing(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_FREEZING.location()));
        return this;
    }
    //闪电
    public LegacyDamageSource setLightning(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.IS_LIGHTNING.location()));
        return this;
    }
    //不会激怒中立生物
    public LegacyDamageSource setNoAnger(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.NO_ANGER.location()));
        return this;
    }
    //无击退
    public LegacyDamageSource setNoImpact(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.NO_IMPACT.location()));
        return this;
    }
    //不触发守卫者的荆棘
    public LegacyDamageSource setAvoidsGuardianThorns(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
        return this;
    }
    //总会对末影龙造成伤害
    public LegacyDamageSource setAlwaysHurtsEnderDragons(){
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE,DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS.location()));
        return this;
    }
    //魔法伤害
    public LegacyDamageSource setMagic(){
        this.damageTypes.add(DamageTypes.MAGIC);
        return this;
    }

    public LegacyDamageSource setMsgId(String string){
        this.msgId=string;
        return this;
    }
    public Component getLocalizedDeathMessage(@NotNull LivingEntity victim) {
        String messageId = getMessageId();
        if (this.getEntity() == null && this.getDirectEntity() == null) {
            LivingEntity killer = victim.getKillCredit();
            String playerMessageKey = messageId + ".player";
            return killer != null
                    ? Component.translatable(playerMessageKey, victim.getDisplayName(), killer.getDisplayName())
                    : Component.translatable(messageId, victim.getDisplayName());
        }
        Component attackerName = getAttackerDisplayName();
        ItemStack weapon = getWeaponUsed();
        if (!weapon.isEmpty() && weapon.hasCustomHoverName()) {
            String itemMessageKey = messageId + ".item";
            return Component.translatable(itemMessageKey, victim.getDisplayName(), attackerName, weapon.getDisplayName());
        }
        return Component.translatable(messageId, victim.getDisplayName(), attackerName);
    }
    private String getMessageId() {
        return "death.attack." + (this.msgId == null ? this.type().msgId() : this.msgId);
    }
    private Component getAttackerDisplayName() {
        return this.getEntity() == null
                ? Objects.requireNonNull(this.getDirectEntity()).getDisplayName()
                : this.getEntity().getDisplayName();
    }
    private ItemStack getWeaponUsed() {
        Entity attacker = this.getEntity();
        return attacker instanceof LivingEntity livingAttacker
                ? livingAttacker.getMainHandItem()
                : ItemStack.EMPTY;
    }

    @Override
    public boolean is(TagKey<DamageType> key) {
        if (!damageTypes.isEmpty()){
            return damageTypes.contains(ResourceKey.create(Registries.DAMAGE_TYPE, key.location())) || super.is(key);
        }
        return super.is(key);
    }
    @Override
    public boolean is(ResourceKey<DamageType> key) {
        if (!damageTypes.isEmpty()){
            return damageTypes.contains(key) || super.is(key);
        }
        return super.is(key);
    }
}
