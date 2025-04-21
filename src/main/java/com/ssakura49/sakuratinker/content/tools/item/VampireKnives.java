package com.ssakura49.sakuratinker.content.tools.item;

import com.ssakura49.sakuratinker.content.entity.GhostKnife;
import com.ssakura49.sakuratinker.content.tools.definition.ToolDefinitions;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import com.ssakura49.sakuratinker.utils.TooltipUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class VampireKnives extends ModifiableItem {
    private static final float ANGLE_OFFSET_15 = 15.0f;
    private static final float ANGLE_OFFSET_30 = 30.0f;
    private static final float LIFE_STEAL_CHANCE = 0.5f; // 吸血概率
    private static final float LIFE_STEAL_PERCENT = 0.1f; // 恢复造成的伤害值

    public VampireKnives(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }
//    @Override
//    public int getUseDuration(ItemStack stack) {
//        return 120; // 最大使用持续时间
//    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE; // 使用长矛的动画
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);

        if (tool.isBroken()) {
            return InteractionResultHolder.fail(stack);
        }
        float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        float cooldown = ConditionalStatModifierHook.getModifiedStat(
                tool,
                player,
                STToolStats.COOLDOWN,
                tool.getStats().get(STToolStats.COOLDOWN) * 30.0F / speed
        );

        // 计算基础伤害
        float baseDamage = ToolAttackUtil.getAttributeAttackDamage(
                tool,
                player,
                hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND
        );

        // 发射主飞刀
        GhostKnife mainKnife = createKnife(level, player, baseDamage, 0f);
        mainKnife.setOnHitCallback(this::handleLifeSteal);
        level.addFreshEntity(mainKnife);

        float rand = level.random.nextFloat();

        if (rand < 0.2f) { // 20%概率5飞刀
            // 15度偏移的2把
            GhostKnife rightKnife1 = createKnife(level, player, baseDamage * 0.8f, ANGLE_OFFSET_15);
            rightKnife1.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(rightKnife1);

            GhostKnife leftKnife1 = createKnife(level, player, baseDamage * 0.8f, -ANGLE_OFFSET_15);
            leftKnife1.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(leftKnife1);

            // 30度偏移的2把
            GhostKnife rightKnife2 = createKnife(level, player, baseDamage * 0.8f, ANGLE_OFFSET_30);
            rightKnife2.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(rightKnife2);

            GhostKnife leftKnife2 = createKnife(level, player, baseDamage * 0.8f, -ANGLE_OFFSET_30);
            leftKnife2.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(leftKnife2);
        }
        else if (rand < 0.7f) { // 50%概率3飞刀 (0.7-0.2=0.5)
            // 15度偏移的2把
            GhostKnife rightKnife = createKnife(level, player, baseDamage * 0.8f, ANGLE_OFFSET_15);
            rightKnife.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(rightKnife);

            GhostKnife leftKnife = createKnife(level, player, baseDamage * 0.8f, -ANGLE_OFFSET_15);
            leftKnife.setOnHitCallback(this::handleLifeSteal);
            level.addFreshEntity(leftKnife);
        }

        // 播放音效
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, player.getSoundSource(), 1.0F, 1.5F);

        // 消耗耐久
        ToolDamageUtil.damageAnimated(tool, 1, player);
        player.awardStat(Stats.ITEM_USED.get(this));

        // 设置冷却
        player.getCooldowns().addCooldown(this, (int)cooldown);

        return InteractionResultHolder.consume(stack);
    }

    private GhostKnife createKnife(Level level, Player player, float damage, float horizontalAngleOffset) {
        // 获取当前手持的 ToolStack 和 InteractionHand
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack); // 从物品栏获取 ToolStack

        // 创建 GhostKnife 并传递 ToolStack 和 InteractionHand
        GhostKnife knife = new GhostKnife(level, 0.6f, tool, hand);
        knife.baseDamage = damage;
        knife.setOwner(player);

        // 设置方向和位置
        Vec3 lookAngle = player.getLookAngle();
        Vec3 rotationAxis = new Vec3(0, 1, 0);
        Vec3 rotatedDirection = rotateVectorAroundAxis(lookAngle, rotationAxis, horizontalAngleOffset);
        knife.shoot(rotatedDirection.x, rotatedDirection.y, rotatedDirection.z, 1.5F, 1.0F);
        knife.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

        knife.offHand = (hand == InteractionHand.OFF_HAND);
        return knife;
    }

    // 水平旋转专用方法（优化版）
    private Vec3 rotateVectorAroundAxis(Vec3 vector, Vec3 axis, float degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        // 简化的水平旋转计算（绕Y轴）
        return new Vec3(
                vector.x * cos + vector.z * sin,
                vector.y,  // 保持垂直方向不变
                vector.z * cos - vector.x * sin
        ).normalize();
    }

    // 吸血回调处理
    private void handleLifeSteal(LivingEntity attacker, LivingEntity target, float damageDealt) {
        // 检查attacker和target不为null
        if (attacker == null || target == null || attacker.level() == null) {
            return;
        }

        // 检查吸血概率
        if (attacker.level().random.nextFloat() < LIFE_STEAL_CHANCE) {
            float healAmount = damageDealt * LIFE_STEAL_PERCENT;
            // 确保治疗量至少为1点
            if (healAmount < 1.0f) healAmount = 1.0f;

            attacker.heal(healAmount);

            // 只在服务端生成粒子效果
            if (!attacker.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel)attacker.level();
                serverLevel.sendParticles(ParticleTypes.HEART,
                        target.getX(), target.getEyeY(), target.getZ(),
                        5, 0.3, 0.3, 0.3, 0.2);
            }
        }
    }

    private List<Component> getVampireKnivesStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }

        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        TooltipUtils.addToolStatTooltip(builder, tool, STToolStats.COOLDOWN);
        builder.addAllFreeSlots();

        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }

        return tooltips;
    }

    @Override
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        tooltips = this.getVampireKnivesStats(tool, player, tooltips, key, flag);
        return tooltips;
    }
}
