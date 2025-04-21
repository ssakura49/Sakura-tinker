package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticDamageTypes;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

import static com.aizistral.enigmaticlegacy.items.VoidPearl.baseDarknessDamage;
import static com.aizistral.enigmaticlegacy.items.VoidPearl.shadowRange;

public class VoidPearlModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder != null) {
            if (holder instanceof Player player) {
                if (!SuperpositionHandler.hasCurio(player, EnigmaticItems.VOID_PEARL)) {
                    if (player.getAirSupply() < 300) {
                        player.setAirSupply(300);
                    }

                    if (player.isOnFire()) {
                        player.clearFire();
                    }

                    for (MobEffectInstance effect : new ArrayList<>(player.getActiveEffects())) {
                        if (effect.getEffect() == MobEffects.NIGHT_VISION) {
                            if (effect.getDuration() >= EnigmaticItems.MINING_CHARM.nightVisionDuration - 10 && effect.getDuration() <= EnigmaticItems.MINING_CHARM.nightVisionDuration) {
                                continue;
                            }
                        }
                        player.removeEffect(effect.getEffect());
                    }

                    if (player.tickCount % 10 == 0) {
                        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX() - shadowRange.getValue(), player.getY() - shadowRange.getValue(), player.getZ() - shadowRange.getValue(), player.getX() + shadowRange.getValue(), player.getY() + shadowRange.getValue(), player.getZ() + shadowRange.getValue()));
                        boolean hasAnimalGuide = SuperpositionHandler.hasItem(player, EnigmaticItems.ANIMAL_GUIDEBOOK);

                        if (entities.contains(player)) {
                            entities.remove(player);
                        }

                        for (LivingEntity victim : entities) {
                            if (victim.level().getMaxLocalRawBrightness(victim.blockPosition(), 0) < 3 || (victim instanceof Phantom && !victim.isOnFire())) {
                                if (hasAnimalGuide && EnigmaticItems.ANIMAL_GUIDEBOOK.isProtectedAnimal(victim)) {
                                    continue;
                                }

                                if (victim instanceof Player) {
                                    Player playerVictim = (Player) victim;
                                    if (SuperpositionHandler.hasCurio(playerVictim, EnigmaticItems.VOID_PEARL)) {
                                        playerVictim.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1, false, true));
                                        continue;
                                    }
                                }

                                if (!(victim instanceof Player) || player.canHarmPlayer((Player) victim)) {
                                    boolean attack = victim.hurt(victim.damageSources().source(EnigmaticDamageTypes.DARKNESS, player), (float) baseDarknessDamage.getValue());

                                    if (attack) {
                                        player.level().playSound(null, victim.blockPosition(), SoundEvents.PHANTOM_BITE, SoundSource.PLAYERS, 1.0F, (float) (0.3F + (Math.random() * 0.4D)));

                                        victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1, false, true));
                                        victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, false, true));
                                        victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true));
                                        victim.addEffect(new MobEffectInstance(MobEffects.HUNGER, 160, 2, false, true));
                                        victim.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 3, false, true));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
