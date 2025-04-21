package com.ssakura49.sakuratinker.library.logic.helper;

import com.ssakura49.sakuratinker.library.events.TinkerToolCriticalEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.MeleeHitToolHook;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

public class AttackLogicHelper {
    private static final float DEGREE_TO_RADIANS = ((float)Math.PI / 180F);
    private static final AttributeModifier ANTI_KNOCKBACK_MODIFIER;

    public AttackLogicHelper() {
    }

    public static void hurtEntityFromAttacker(LivingEntity target, DamageSource sourceId, LivingEntity attacker, float damage) {
        DamageSource damageSource;
        if (attacker instanceof Player) {
            damageSource = attacker.damageSources().playerAttack((Player) attacker);
        } else {
            damageSource = attacker.damageSources().mobAttack(attacker);
        }
        target.hurt(damageSource, damage);
    }

    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot) {
        if (!tool.isBroken() && tool.hasTag(Items.MELEE)) {
            if (!attackerLiving.level().isClientSide && targetEntity.isAttackable() && !targetEntity.skipAttackInteraction(attackerLiving)) {
                LivingEntity targetLiving = ToolAttackUtil.getLivingEntity(targetEntity);
                Player attackerPlayer = null;
                if (attackerLiving instanceof Player) {
                    Player player = (Player)attackerLiving;
                    attackerPlayer = player;
                }

                float damage = ToolAttackUtil.getAttributeAttackDamage(tool, attackerLiving, sourceSlot);
                float cooldown = (float)cooldownFunction.getAsDouble();
                boolean fullyCharged = cooldown > 0.9F;
                TinkerToolCriticalEvent event = new TinkerToolCriticalEvent(tool, new ToolAttackContext(attackerLiving, attackerPlayer, hand, sourceSlot, targetEntity, targetLiving, isVanillaCritical(isExtraAttack, fullyCharged, attackerLiving, targetLiving), cooldown, isExtraAttack), isVanillaCritical(isExtraAttack, fullyCharged, attackerLiving, targetLiving));
                MinecraftForge.EVENT_BUS.post(event);
                boolean isCritical = event.getCritical();
                ToolAttackContext context = event.getContext();
                float baseDamage = damage;
                List<ModifierEntry> modifiers = tool.getModifierList();

                for(ModifierEntry entry : modifiers) {
                    damage = ((MeleeDamageModifierHook)entry.getHook(ModifierHooks.MELEE_DAMAGE)).getMeleeDamage(tool, entry, context, baseDamage, damage);
                }

                if (damage <= 0.0F) {
                    return !isExtraAttack;
                } else {
                    float knockback = (float)attackerLiving.getAttributeValue(Attributes.ATTACK_KNOCKBACK) / 2.0F;
                    if (targetLiving != null) {
                        knockback += 0.4F;
                    }

                    SoundEvent sound;
                    if (attackerLiving.isSprinting() && fullyCharged) {
                        sound = SoundEvents.PLAYER_ATTACK_KNOCKBACK;
                        knockback += 0.5F;
                    } else if (fullyCharged) {
                        sound = SoundEvents.PLAYER_ATTACK_STRONG;
                    } else {
                        sound = SoundEvents.PLAYER_ATTACK_WEAK;
                    }

                    if (!isExtraAttack) {
                        float criticalModifier = isCritical ? 1.5F : 1.0F;
                        if (attackerPlayer != null) {
                            CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer, targetEntity, isCritical, isCritical ? 1.5F : 1.0F);
                            isCritical = hitResult != null;
                            if (isCritical) {
                                criticalModifier = hitResult.getDamageModifier();
                            }
                        }

                        if (isCritical) {
                            damage *= criticalModifier;
                        }
                    }

                    boolean isMagic = damage > baseDamage;
                    if (cooldown < 1.0F) {
                        damage *= 0.2F + cooldown * cooldown * 0.8F;
                    }

                    float oldHealth = 0.0F;
                    if (targetLiving != null) {
                        oldHealth = targetLiving.getHealth();
                    }

                    float baseKnockback = knockback;

                    for(ModifierEntry entry : modifiers) {
                        knockback = ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
                    }

                    ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);
                    Optional<AttributeInstance> knockbackModifier = getKnockbackAttribute(targetLiving);
                    boolean canceledKnockback = false;
                    if (knockback < 0.4F) {
                        canceledKnockback = true;
                        knockbackModifier.ifPresent(AttackLogicHelper::disableKnockback);
                    } else if (targetLiving != null) {
                        knockback -= 0.4F;
                    }

                    boolean didHit;
                    if (isExtraAttack) {
                        didHit = ToolAttackUtil.dealDefaultDamage(attackerLiving, targetEntity, damage);
                    } else {
                        didHit = MeleeHitToolHook.dealDamage(tool, context, damage);
                    }

                    ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);
                    if (canceledKnockback) {
                        knockbackModifier.ifPresent(AttackLogicHelper::enableKnockback);
                    }

                    if (!didHit) {
                        if (!isExtraAttack) {
                            attackerLiving.level().playSound((Player)null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);
                        }

                        for(ModifierEntry entry : modifiers) {
                            ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).failedMeleeHit(tool, entry, context, damage);
                        }

                        return !isExtraAttack;
                    } else {
                        float damageDealt = damage;
                        if (targetLiving != null) {
                            damageDealt = oldHealth - targetLiving.getHealth();
                        }

                        if (knockback > 0.0F) {
                            if (targetLiving != null) {
                                targetLiving.knockback((double)knockback, (double)Mth.sin(attackerLiving.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(attackerLiving.getYRot() * ((float)Math.PI / 180F))));
                            } else {
                                targetEntity.push((double)(-Mth.sin(attackerLiving.getYRot() * ((float)Math.PI / 180F)) * knockback), 0.1, (double)(Mth.cos(attackerLiving.getYRot() * ((float)Math.PI / 180F)) * knockback));
                            }

                            attackerLiving.setDeltaMovement(attackerLiving.getDeltaMovement().multiply(0.6, (double)1.0F, 0.6));
                            attackerLiving.setSprinting(false);
                        }

                        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer) {
                            ServerPlayer serverPlayer = (ServerPlayer)targetEntity;
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
                            targetEntity.hurtMarked = false;
                        }

                        if (attackerPlayer != null) {
                            if (isCritical) {
                                sound = SoundEvents.PLAYER_ATTACK_CRIT;
                                attackerPlayer.crit(targetEntity);
                            }

                            if (isMagic) {
                                attackerPlayer.magicCrit(targetEntity);
                            }

                            attackerLiving.level().playSound((Player)null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), sound, attackerLiving.getSoundSource(), 1.0F, 1.0F);
                        }

                        if (damageDealt > 2.0F) {
                            Level time = attackerLiving.level();
                            if (time instanceof ServerLevel) {
                                ServerLevel server = (ServerLevel)time;
                                int particleCount = (int)(damageDealt * 0.5F);
                                server.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY((double)0.5F), targetEntity.getZ(), particleCount, 0.1, (double)0.0F, 0.1, 0.2);
                            }
                        }

                        attackerLiving.setLastHurtByMob((LivingEntity) targetEntity);
                        if (targetLiving != null) {
                            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
                        }

                        for(ModifierEntry entry : modifiers) {
                            ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).afterMeleeHit(tool, entry, context, damageDealt);
                        }

                        float speed = (Float)tool.getStats().get(ToolStats.ATTACK_SPEED);
                        int time = Math.round(20.0F / speed);
                        if (time < targetEntity.invulnerableTime) {
                            targetEntity.invulnerableTime = (targetEntity.invulnerableTime + time) / 2;
                        }

                        if (attackerPlayer != null) {
                            if (targetLiving != null) {
                                if (!attackerLiving.level().isClientSide && !isExtraAttack) {
                                    ItemStack held = attackerLiving.getItemBySlot(sourceSlot);
                                    if (!held.isEmpty()) {
                                        held.hurtEnemy(targetLiving, attackerPlayer);
                                    }
                                }

                                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
                            }

                            attackerPlayer.causeFoodExhaustion(0.1F);
                            if (!isExtraAttack) {
                                attackerPlayer.awardStat(Stats.ITEM_USED.get(tool.getItem()));
                            }
                        }

                        if (!tool.hasTag(Items.UNARMED)) {
                            int durabilityLost = targetLiving != null ? 1 : 0;
                            if (!tool.hasTag(Items.MELEE_PRIMARY)) {
                                durabilityLost *= 2;
                            }

                            ToolDamageUtil.damageAnimated(tool, durabilityLost, attackerLiving);
                        }

                        return true;
                    }
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isVanillaCritical(boolean isExtraAttack, boolean fullyCharged, LivingEntity attackerLiving, LivingEntity targetLiving) {
        return !isExtraAttack && fullyCharged && attackerLiving.fallDistance > 0.0F && !attackerLiving.onGround() && !attackerLiving.onClimbable() && !attackerLiving.isInWater() && !attackerLiving.hasEffect(MobEffects.BLINDNESS) && !attackerLiving.isPassenger() && targetLiving != null && !attackerLiving.isSprinting();
    }

    private static Optional<AttributeInstance> getKnockbackAttribute(@Nullable LivingEntity living) {
        return Optional.ofNullable(living).map((e) -> e.getAttribute(Attributes.KNOCKBACK_RESISTANCE)).filter((attribute) -> !attribute.hasModifier(ANTI_KNOCKBACK_MODIFIER));
    }

    private static void disableKnockback(AttributeInstance instance) {
        instance.addTransientModifier(ANTI_KNOCKBACK_MODIFIER);
    }

    private static void enableKnockback(AttributeInstance instance) {
        instance.removeModifier(ANTI_KNOCKBACK_MODIFIER);
    }

    static {
        ANTI_KNOCKBACK_MODIFIER = new AttributeModifier("tconstruct.anti_knockback", (double)1.0F, Operation.ADDITION);
    }
}
