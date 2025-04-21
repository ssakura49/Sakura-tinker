package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

public class KoboldModifier extends BaseModifier {
    private static final UUID ARMOR_UUID = UUID.fromString("80aecf2d-f9ef-42eb-a432-2eda89129737");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("6b06bfae-ba1b-4e67-8867-2dbf3e9c65fc");

    private static final double BOOST_PERCENTAGE = 0.1;
    private static final float HEALTH_THRESHOLD = 40.0f;

    private boolean isValidPlayer(Player player) {
        return player != null && player.isAlive() && !player.isSpectator();
    }
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        int level = Math.min(3, Math.max(0, modifier.getLevel()));
        if (attacker instanceof Player player && isValidPlayer(player)) {
            if (player.getMaxHealth() > HEALTH_THRESHOLD) {
                damage += (float) (damage * (BOOST_PERCENTAGE * level));
            }
        }
        return damage;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (level.isClientSide || !(entity instanceof Player player)) return;
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
        float maxHealth = player.getMaxHealth();
        float currentHealth = player.getHealth();
        if (isCorrectSlot && slot.getType() == EquipmentSlot.Type.ARMOR) {
            addArmorAttributes(player, maxHealth);
        }
        if (isCorrectSlot && currentHealth < HEALTH_THRESHOLD) {
            applyEffects(player);
        }
    }

    private void addArmorAttributes(Player player, float maxHealth) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance toughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armor != null)
            armor.removeModifier(ARMOR_UUID);
        if (toughness != null)
            toughness.removeModifier(TOUGHNESS_UUID);
        if (maxHealth > HEALTH_THRESHOLD) {
            if (armor != null) {
                armor.addTransientModifier(new AttributeModifier(ARMOR_UUID, "armor_boost", BOOST_PERCENTAGE, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (toughness != null) {
                toughness.addTransientModifier(new AttributeModifier(TOUGHNESS_UUID, "toughness_boost", BOOST_PERCENTAGE, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }

    private void applyEffects(Player player) {
        MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true);
        MobEffectInstance weak = new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false, true);
        player.addEffect(slow);
        player.addEffect(weak);
    }
}
