package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.PixieEntity;

public class PixieArmorModifier extends BaseModifier {
    private static final float PIXIE_DAMAGE = 2.0F;

    @Override
    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context,
                                   EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity holder = context.getEntity();
        if (holder.level().isClientSide) return;
        int level = getModifierLevelForSlot(holder, slotType, this);
        if (level <= 0) return;
        float chance = switch (slotType) {
            case HEAD -> 0.11F;
            case CHEST -> 0.17F;
            case LEGS -> 0.15F;
            case FEET -> 0.09F;
            default -> 0F;
        };
        if (holder.getRandom().nextFloat() < chance) {
            Entity attacker = source.getEntity();
            if (attacker instanceof LivingEntity livingAttacker) {
                spawnPixie(holder, livingAttacker, modifier.getLevel());
            }
        }
    }

    private int getModifierLevelForSlot(LivingEntity entity, EquipmentSlot slot, Modifier modifier) {
        return switch (slot) {
            case HEAD -> ToolUtil.getHeadModifierLevel(entity, modifier);
            case CHEST -> ToolUtil.getChestModifierLevel(entity, modifier);
            case LEGS -> ToolUtil.getLegsModifierLevel(entity, modifier);
            case FEET -> ToolUtil.getFeetModifierLevel(entity, modifier);
            default -> 0;
        };
    }

    private void spawnPixie(LivingEntity summoner, LivingEntity target, int level) {
        PixieEntity pixie = new PixieEntity(summoner.level());
        pixie.setPos(summoner.getX(), summoner.getY() + 1.0, summoner.getZ());
        pixie.setProps(target, summoner, 0, PIXIE_DAMAGE * level);
        summoner.level().addFreshEntity(pixie);
    }
}
