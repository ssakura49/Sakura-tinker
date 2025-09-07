package com.ssakura49.sakuratinker.common.tools.item;

import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import com.ssakura49.sakuratinker.common.entity.base.BlockInteraction;
import com.ssakura49.sakuratinker.common.entity.base.EntityInteraction;
import com.ssakura49.sakuratinker.common.entity.base.IYoyo;
import com.ssakura49.sakuratinker.common.entity.base.RenderOrientation;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.register.STSounds;
import com.ssakura49.sakuratinker.utils.tinker.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YoyoItem extends ModifiableItem implements IYoyo {
    public YoyoItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    protected RenderOrientation renderOrientation = RenderOrientation.Vertical;
    protected List<EntityInteraction> entityInteractions = new ArrayList<>();
    protected List<BlockInteraction> blockInteractions = new ArrayList<>();


    public YoyoItem addBlockInteraction(BlockInteraction... interaction){
        Collections.addAll(blockInteractions,interaction);
        return this;
    }

    public YoyoItem addEntityInteraction(EntityInteraction... interaction){
        Collections.addAll(entityInteractions,interaction);
        return this;
    }

    public YoyoItem setRenderOrientation(RenderOrientation renderOrientation) {
        this.renderOrientation = renderOrientation;
        return this;
    }

    public static boolean isAttackEnable(ItemStack stack){
        return  !stack.getOrCreateTag().contains("attack") || stack.getOrCreateTag().getBoolean("attack");
    }
    public static void toggleAttack(ItemStack stack){
        stack.getOrCreateTag().putBoolean("attack",!isAttackEnable(stack));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide && state.getDestroySpeed(level,pos) != 0.0F){
            stack.hurtAndBreak(1,entity,e->e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack toolStack = ToolStack.from(stack);
        // 添加死亡检查
        if (!player.isAlive()) {
            return InteractionResultHolder.fail(stack);
        }
        if (!level.isClientSide){
            if (stack.getDamageValue() <= stack.getMaxDamage()){

                YoyoEntity yoyoEntity = YoyoEntity.CASTERS.get(player.getUUID());

                if (yoyoEntity == null) {
                    yoyoEntity = new YoyoEntity(level,player,hand);
                    level.addFreshEntity(yoyoEntity);
                    level.playSound(null, yoyoEntity.getX(), yoyoEntity.getY(), yoyoEntity.getZ(), STSounds.THROW.get(), SoundSource.NEUTRAL, 0.5f, 0.4f / (level.random.nextFloat() * 0.4f + 0.8f));

                    int amount = yoyoEntity.getHitCount();
                    ToolDamageUtil.damageAnimated(toolStack, amount, player);
                    player.causeFoodExhaustion(0.05f);
                } else {
                    yoyoEntity.setRetracting(!yoyoEntity.isRetracting());
                }

            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public double getWeight(ItemStack yoyo) {
        ToolStack toolStack = ToolStack.from(yoyo);
        return toolStack.getStats().getInt(STToolStats.WEIGHT);
    }

    @Override
    public double getLength(ItemStack yoyo) {
        ToolStack toolStack = ToolStack.from(yoyo);
        return toolStack.getStats().getInt(STToolStats.LENGTH);
    }

    @Override
    public int getDuration(ItemStack yoyo) {
        ToolStack toolStack = ToolStack.from(yoyo);
        return toolStack.getStats().getInt(STToolStats.TIME);
    }

    @Override
    public int getAttackInterval(ItemStack yoyo) {
        ToolStack toolStack = ToolStack.from(yoyo);
        return toolStack.getStats().getInt(STToolStats.ATTACK_INTERVAL);
    }

    @Override
    public int getMaxCollectedDrops(ItemStack yoyo) {
        ToolStack toolStack = ToolStack.from(yoyo);
        return toolStack.getStats().getInt(STToolStats.MAX_COLLECTED);
    }

    @Override
    public <T extends LivingEntity> void damageItem(ItemStack yoyo, InteractionHand hand, int amount, T entity) {

    }

    @Override
    public void entityInteraction(ItemStack yoyoStack, Player player, InteractionHand hand, YoyoEntity yoyo, Entity target) {
        if (target.level().isClientSide) return;
        entityInteractions.forEach(i->i.apply(yoyoStack, player, hand, yoyo, target));

    }

    @Override
    public boolean interactsWithBlocks(ItemStack yoyo) {
        return !blockInteractions.isEmpty();
    }

    @Override
    public void blockInteraction(ItemStack yoyoStack, Player player, Level world, BlockPos pos, BlockState state, Block block, YoyoEntity yoyo) {
        if (world.isClientSide) return;
        blockInteractions.forEach(i->i.apply(yoyoStack, player, pos, state, block, yoyo));
    }

    @Override
    public RenderOrientation getRenderOrientation(ItemStack yoyo) {
        return renderOrientation;
    }

    @Override
    public @NotNull List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        return this.getYoYoStats(tool, player, tooltips, key, tooltipFlag);
    }

    public List<Component> getYoYoStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.TIME);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.ATTACK_INTERVAL);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.WEIGHT);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.MAX_COLLECTED);
        TooltipUtil.addToolStatTooltip(builder, tool, STToolStats.LENGTH);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}
