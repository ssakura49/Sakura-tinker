package com.ssakura49.sakuratinker.common.tinkering.modifiers.test;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.library.damagesource.PercentageBypassArmorSource;
import com.ssakura49.sakuratinker.register.STEffects;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class test extends BaseModifier  {


    public static MobEffect getEffect() {
        ResourceLocation effectId = ResourceLocation.withDefaultNamespace("regeneration");
        return ForgeRegistries.MOB_EFFECTS.getValue(effectId);
    }

    public void BBBB_afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        if (!(context.getTarget() instanceof LivingEntity target)) return;
        if (!(context.getAttacker() instanceof Player player))return;
        MobEffect effect = getEffect();
        MobEffectInstance instance = target.getEffect(effect);

        if (instance != null) {
            int amplifier = instance.getAmplifier();
            int newAmplifier = amplifier + 1;
            int duration = instance.getDuration();
            boolean ambient = instance.isAmbient();
            boolean visible = instance.isVisible();
            boolean showIcon = instance.showIcon();
            if (newAmplifier > entry.getLevel()) {
                newAmplifier = entry.getLevel();
            }
            int a = instance.getAmplifier();
            MobEffectInstance newEffect = new MobEffectInstance(
                    effect,
                    duration,
                    newAmplifier,
                    ambient,
                    visible,
                    showIcon);
            target.addEffect(newEffect);
            DamageSource source = context.getAttacker().damageSources().inFire();
//            LegacyDamageSource source = LegacyDamageSource.playerAttack(player).setFire();
            target.hurt(source, damageDealt * a);
        }
    }




    public static LegacyDamageSource magic(LivingEntity attacker) {
        return new LegacyDamageSource(attacker.damageSources().magic().typeHolder(), attacker).setMagic();
    }










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

    public class moredamage extends MobEffect{

        UUID ARMOR_PENALTY_UUID = UUID.fromString("874C97BF-6CF1-4E95-88D7-2B8828B42A2A");
        public moredamage(){
            super(MobEffectCategory.HARMFUL,0x3f3f3f);
        }
        @Override
        public boolean isDurationEffectTick(int duration,int amplifier){
            return false;
        }
        @Override
        public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier){
            super.addAttributeModifiers(entity,attributes,amplifier);
            double armorPenalty=-0.2*(amplifier+1);
            double currentArmor=entity.getAttributeValue(Attributes.ARMOR);
            double minArmorPenalty=-currentArmor;// most in 0
            armorPenalty=Math.max(armorPenalty,minArmorPenalty);
            AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
            if (armorAttr != null) {
                armorAttr.removeModifier(ARMOR_PENALTY_UUID);
                armorAttr.addTransientModifier(new AttributeModifier(
                        ARMOR_PENALTY_UUID,
                        "armor penalty",
                        armorPenalty,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));
            }
        }

        @Override
        public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
            super.removeAttributeModifiers(entity, attributes, amplifier);
            AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
            if (armorAttr != null) {
                armorAttr.removeModifier(ARMOR_PENALTY_UUID);
            }
        }
    }

    public void onLivingDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        LivingEntity attacker = entity.getLastAttacker();
        ItemStack stack = attacker.getMainHandItem();
        ToolStack toolStack = ToolStack.from(stack);
        if (ToolUtil.checkTool(stack)) {
            int level = toolStack.getModifierLevel(STModifiers.Void.get());//这里改成你自己的modifier
            if (level > 0) {
                //这里写增加buff的逻辑
            }

        }
    }

    public void remove(PotionBrewEvent event) {}


}
