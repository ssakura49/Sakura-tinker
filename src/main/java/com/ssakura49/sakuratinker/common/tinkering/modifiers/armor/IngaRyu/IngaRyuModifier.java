package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor.IngaRyu;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.library.logic.context.AttackedContent;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import java.util.List;

import static com.ssakura49.sakuratinker.common.tinkering.modifiers.misc.HyperBurstModifier.BASE_CHANCE;
import static com.ssakura49.sakuratinker.common.tinkering.modifiers.misc.HyperBurstModifier.CRIT_CHARGE_KEY;

public class IngaRyuModifier extends BaseModifier {
    private static final int RANGE = 5;
    private static final float DAMAGE_RATIO = 0.7f;

    @Override
    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier,
                                   EquipmentContext context, EquipmentSlot slot,
                                   DamageSource source, float amount, boolean isDirect) {
        // 受击时重置暴击充能为基础值
        tool.getPersistentData().putFloat(CRIT_CHARGE_KEY, BASE_CHANCE);
    }

    @Override
    public void onTakeDamagePost(IToolStackView armor, ModifierEntry entry, LivingDamageEvent event, @Nullable AttackedContent data) {
        LivingEntity wearer = null;
        if (data != null) {
            wearer = data.entity();
        }
        if (wearer == null || wearer.level().isClientSide) return;
        if (event.getSource().getMsgId().equals("inga_ryu.rebound")) {
            return;
        }
        List<LivingEntity> monsters = EntityUtil.getMonsters(wearer, 4 + entry.getLevel() * RANGE);
        if (monsters.isEmpty()) return;

        if (wearer instanceof Player player) {
            ToolStack armorStack = ToolStack.from(wearer.getItemBySlot(data.slot()));
            List<HeadMaterialStats> stats = IngaRyuUtils.getMeleeStatsFromArmor(armorStack);
            List<ModifierEntry> traits = IngaRyuUtils.getToolModifiersFromArmorMaterials(armorStack);

            float baseDamage = event.getAmount() + 10.0f;
            float bonusDamage = 0f;
            for (HeadMaterialStats stat : stats) {
                bonusDamage += stat.attack();
            }

            float rawDamage = baseDamage * 0.7f;
            float finalDamage = rawDamage * (1f + bonusDamage);

            for (LivingEntity target : monsters) {
                if (!target.isAlive() || target.equals(wearer)) continue;

                ToolAttackContext context = new ToolAttackContext(
                        player, player, InteractionHand.MAIN_HAND,
                        target, target, false, 0, true
                );

                float damage = finalDamage;
                for (ModifierEntry entry1 : traits) {
                    damage = entry1.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(armor, entry1, context, finalDamage, damage);
                }

                if (damage <= 0f) continue;

                for (ModifierEntry entry1 : traits) {
                    entry1.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(armor, entry1, context, damage, 0, 0);
                }

                LegacyDamageSource source = LegacyDamageSource.playerAttack(player)
                        .setBypassArmor()
                        .setMagic()
                        .setAvoidsGuardianThorns()
                        .setMsgId("inga_ryu.rebound");
                target.hurt(source, damage);

                for (ModifierEntry entry1 : traits) {
                    entry1.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(armor, entry1, context, damage);
                }
            }
        }
    }
}