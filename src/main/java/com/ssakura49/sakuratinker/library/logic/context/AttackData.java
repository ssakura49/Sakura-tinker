package com.ssakura49.sakuratinker.library.logic.context;

import com.ssakura49.sakuratinker.library.logic.helper.AttackLogicHelper;
import com.ssakura49.sakuratinker.utils.EntityUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;

public record AttackData(DamageSource source, LivingEntity entity, EquipmentContext context, EquipmentSlot slot) {
    public AttackData(DamageSource source, LivingEntity entity, EquipmentContext context, EquipmentSlot slot) {
        this.source = source;
        this.entity = entity;
        this.context = context;
        this.slot = slot;
    }

    public boolean isDirect() {
        return OnAttackedModifierHook.isDirectDamage(this.source);
    }

    public boolean isNullAttacker() {
        return this.source.getDirectEntity() == null && this.source.getEntity() == null;
    }

    public Entity getDirect() {
        return this.source.getDirectEntity();
    }

    public ItemStack getSlotStack(LivingEntity entity) {
        return entity.getItemBySlot(this.slot);
    }

    public boolean isArrowSource() {
        return this.source.getDirectEntity() instanceof AbstractArrow;
    }

    @Nullable
    public LivingEntity getAttacker() {
        Entity var2 = this.source.getEntity();
        LivingEntity var10000;
        if (var2 instanceof LivingEntity liv) {
            var10000 = liv;
        } else {
            var10000 = null;
        }

        return var10000;
    }

    public LazyOptional<TinkerDataCapability.Holder> getTinkerData() {
        return this.context.getTinkerData();
    }

    @Nullable
    public Player getPlayerAttack() {
        Entity var2 = this.source.getEntity();
        if (var2 instanceof Player player) {
            return player;
        } else {
            return null;
        }
    }

    public boolean isFullAttack() {
        Player player = this.getPlayerAttack();
        return player == null || EntityUtils.isFullChance(player);
    }

    public boolean isCritical() {
        Entity var2 = this.source.getEntity();
        if (var2 instanceof LivingEntity attacker) {
            return AttackLogicHelper.isVanillaCritical(false, this.isFullAttack(), attacker, this.entity);
        } else {
            return false;
        }
    }

    public float getAttackCooldown() {
        Player player = this.getPlayerAttack();
        if (player != null) {
            ToolAttackUtil.getCooldownFunction(player, InteractionHand.MAIN_HAND).getAsDouble();
        }

        return 1.0F;
    }

    @Nullable
    public ToolAttackContext getDefaultContext(boolean isExtraAttack) {
        if (this.getAttacker() != null) {
            Player player = null;
            if (this.getPlayerAttack() != null) {
                player = this.getPlayerAttack();
            }

            new ToolAttackContext(this.getAttacker(), player, InteractionHand.MAIN_HAND, this.entity, this.entity, this.isCritical(), this.getAttackCooldown(), isExtraAttack);
        }

        return null;
    }

    public DamageSource source() {
        return this.source;
    }

    public LivingEntity entity() {
        return this.entity;
    }

    public EquipmentContext context() {
        return this.context;
    }

    public EquipmentSlot slot() {
        return this.slot;
    }
}
