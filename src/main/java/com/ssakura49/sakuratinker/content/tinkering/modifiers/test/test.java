package com.ssakura49.sakuratinker.content.tinkering.modifiers.test;

import com.ssakura49.sakuratinker.content.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.TargetTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class test extends BaseModifier {

    private static final Random RANDOM = new Random();
    private static List<MobEffect> EFFECTS_CACHE = null;
    public static List<MobEffect> getAllHarmfulEffects() {
        if (EFFECTS_CACHE == null) {
            EFFECTS_CACHE = ForgeRegistries.MOB_EFFECTS.getValues().stream()
                    .filter(effect -> !effect.isBeneficial())
                    .collect(Collectors.toList());
        }
        return EFFECTS_CACHE;
    }
    public static void applyRandomHarmfulEffect(LivingEntity target, int durationTicks, int amplifier) {
        List<MobEffect> negativeEffects = getAllHarmfulEffects();
        if (negativeEffects.isEmpty()) {
            return;
        }
        MobEffect randomEffect = negativeEffects.get(RANDOM.nextInt(negativeEffects.size()));
        target.addEffect(new MobEffectInstance(randomEffect, durationTicks, amplifier));
    }
}
