package com.ssakura49.sakuratinker.content.tools.item;

import com.ssakura49.sakuratinker.content.tools.capability.EnergyCapability;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import com.ssakura49.sakuratinker.register.STSounds;
import com.ssakura49.sakuratinker.utils.ItemUtils;
import com.ssakura49.sakuratinker.utils.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

public class LaserGun extends ModifiableItem {

    public LaserGun(Item.Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @OnlyIn(Dist.CLIENT)
    private void emitLaser(ToolStack tool, Player attacker, InteractionHand hand, Level world, Vec3 angle, float range) {
        Vec3 startPos = attacker.getEyePosition(); // 修复4：正确的起点计算
        List<LivingEntity> hitEntities = new ArrayList<>();

        for (double d = 0; d < range; d += 0.5) {
            Vec3 currentPos = startPos.add(angle.scale(d));

            // 修复5：在服务端生成粒子（会同步到客户端）
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        currentPos.x, currentPos.y, currentPos.z,
                        1, 0, 0, 0, 0);
            }

            // 碰撞检测
            AABB hitBox = new AABB(currentPos.x - 0.5, currentPos.y - 0.5, currentPos.z - 0.5,
                    currentPos.x + 0.5, currentPos.y + 0.5, currentPos.z + 0.5);

            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, hitBox)) {
                if (entity.isAlive() && !hitEntities.contains(entity) && entity != attacker) {
                    ToolAttackUtil.attackEntity(tool, attacker, hand, entity,
                            ToolAttackUtil.getCooldownFunction(attacker, hand), false);
                    hitEntities.add(entity);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(itemStack);
        ModDataNBT toolData = tool.getPersistentData();
        boolean creative = player.getAbilities().instabuild;

        if ((!tool.isBroken() && toolData.contains(EnergyCapability.STORED_ENERGY, 3) || creative) && !level.isClientSide()) {
            boolean hasEnergy = toolData.getInt(EnergyCapability.STORED_ENERGY) >= 200;

            if (!creative && !hasEnergy) {
                return InteractionResultHolder.fail(itemStack);
            }

            float range = ConditionalStatModifierHook.getModifiedStat(tool, player, STToolStats.RANGE);
            float cooldown = ConditionalStatModifierHook.getModifiedStat(tool, player, STToolStats.COOLDOWN, (Float)tool.getStats().get(STToolStats.COOLDOWN) * 20.0F);
            this.emitLaser(tool, player, hand, level, player.getLookAngle(), range);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    STSounds.LASER_SHOOT.get(), SoundSource.PLAYERS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);

            ToolDamageUtil.damageAnimated(tool, 1, player, hand);
            EnergyCapability.removeEnergy(tool, 500, false, false);

            ItemUtils.addCooldown(player, (Item)this, Math.round(cooldown));
        }
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    @Override
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player,
                                              List<Component> tooltips, TooltipKey key,
                                              TooltipFlag tooltipFlag) {
        return getLaserGunStats(tool, player, tooltips, key, tooltipFlag);
    }

    public List<Component> getLaserGunStats(IToolStackView tool, @Nullable Player player,
                                            List<Component> tooltips, TooltipKey key,
                                            TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);

        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }

        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        TooltipUtils.addToolStatTooltip(builder, tool, STToolStats.ENERGY_STORE);
        TooltipUtils.addToolStatTooltip(builder, tool, STToolStats.RANGE);
        TooltipUtils.addToolStatTooltip(builder, tool, STToolStats.COOLDOWN);
//        int stored = tool.getPersistentData().getInt(EnergyCapability.STORED_ENERGY);
//        int max = tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY);
//        builder.add(Component.literal("能量: " + stored + " / " + max)
//                .withStyle(ChatFormatting.BLUE));
        builder.addAllFreeSlots();

        for (ModifierEntry entry : tool.getModifierList()) {
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }

        return tooltips;
    }
}
