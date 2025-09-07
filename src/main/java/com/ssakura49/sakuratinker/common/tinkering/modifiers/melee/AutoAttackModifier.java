package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class AutoAttackModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    private static final ResourceLocation COOLDOWN_KEY = ResourceLocation.fromNamespaceAndPath("sakuratinker", "auto_attack_cooldown");

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry entry, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!(holder instanceof Player player)) return;
        if (player.getUsedItemHand() != InteractionHand.MAIN_HAND) return;
        if (player.level().isClientSide) return;
        if (player.getAttackStrengthScale(0.5f) < 1.0f) return;

        ModDataNBT data = tool.getPersistentData();
        int cooldown = data.getInt(COOLDOWN_KEY);
        if (cooldown > 0) {
            data.putInt(COOLDOWN_KEY, cooldown - 1);
            return;
        }
        float attackSpeed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        if (attackSpeed <= 0.1f) attackSpeed = 0.1f;
        AABB box = player.getBoundingBox().inflate(3.0);
        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, box, (e) ->
                EntityUtil.isHostile(e) && e.isAlive() && !e.isInvulnerable());

        if (!targets.isEmpty()) {
            LivingEntity target = targets.get(0);
            int attackCooldownTicks = (int)(20f / attackSpeed);
            data.putInt(COOLDOWN_KEY, attackCooldownTicks);

            player.resetAttackStrengthTicker();

            ToolAttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, target, () -> tool.getStats().get(ToolStats.ATTACK_DAMAGE), true);
        }
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
