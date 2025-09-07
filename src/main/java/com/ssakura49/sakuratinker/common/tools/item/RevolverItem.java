package com.ssakura49.sakuratinker.common.tools.item;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.magic.spells.wind.FlyingSpell;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.c2h6s.etstlib.util.IToolUuidGetter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.menu.RevolverMenu;
import com.ssakura49.sakuratinker.common.entity.BulletEntity;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.utils.tinker.ItemUtil;
import com.ssakura49.sakuratinker.utils.tinker.ToolCooldownManager;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.*;
import java.util.function.Consumer;

public class RevolverItem extends ModifiableItem {
    private static final ResourceLocation TAG_CURRENT_CHAMBER = SakuraTinker.location("bullet_slot");
    public static final int MAX_BULLETS = 6;

    //public static final ResourceLocation LAST_SHOT_TICK = SakuraTinker.location("last_shot_tick");
    //public static final ResourceLocation SHOOT_STAGE = SakuraTinker.location("shoot_stage");
    //public static final ResourceLocation CHARGE_TICKS = SakuraTinker.location("charge_ticks"); // 新增：记录蓄力时间

    public RevolverItem(Properties props, ToolDefinition def) {
        super(props, def);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        if (player.isShiftKeyDown()) {
            if (!world.isClientSide) {
                player.openMenu(new SimpleMenuProvider((id, inv, p) -> new RevolverMenu(id, inv, stack), stack.getHoverName()));
            }
            return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
        }
        LazyOptional<IItemHandler> opt = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!opt.isPresent()) return InteractionResultHolder.fail(stack);
        IItemHandler handler = opt.orElseThrow(IllegalStateException::new);
        ModDataNBT nbt = tool.getPersistentData();
        int currentChamber = nbt.getInt(TAG_CURRENT_CHAMBER);
        for (int i = 0; i < MAX_BULLETS; i++) {
            ItemStack bullet = handler.getStackInSlot(currentChamber);
            ToolStack bulletTool = ToolStack.from(bullet);
            if (!bullet.isEmpty() && !bulletTool.isBroken()) {
                //nbt.putInt(SHOOT_STAGE, 1);
                //nbt.putInt(CHARGE_TICKS, 0);// 重置蓄力计时器
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        if (level.isClientSide) return;
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) return;
        ModDataNBT nbt = tool.getPersistentData();
        int currentChamber = nbt.getInt(TAG_CURRENT_CHAMBER);
        LazyOptional<IItemHandler> opt = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!opt.isPresent()) return;
        IItemHandler handler = opt.orElseThrow(IllegalStateException::new);
        int chargeTime = this.getUseDuration(stack) - timeLeft;

        // 更新蓄力时间
        //int chargeTicks = nbt.getInt(CHARGE_TICKS);
        //nbt.putInt(CHARGE_TICKS, chargeTicks + 1);
        int intervalTick = 7;
        if (chargeTime % intervalTick != 0) return;
        //if (chargeTicks % intervalTick != 0) return;
        ItemStack bullet = handler.getStackInSlot(currentChamber);
        ToolStack bulletTool = ToolStack.from(bullet);
        if (!bullet.isEmpty() && !bulletTool.isBroken()) {
            fireBullet(level, player, bullet, tool);
            ToolDamageUtil.damageAnimated(bulletTool, 1, player);
            currentChamber = (currentChamber + 1) % MAX_BULLETS;
            nbt.putInt(TAG_CURRENT_CHAMBER, currentChamber);
            //nbt.putInt(SHOOT_STAGE, 2);
        }
    }
    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (ToolUtil.checkTool(stack)) {
            ToolStack toolStack = ToolStack.from(stack);
            ModDataNBT nbt = toolStack.getPersistentData();
            //nbt.putInt(SHOOT_STAGE, 0); // 重置射击状态
            //nbt.putInt(CHARGE_TICKS, 0); // 重置蓄力计时器
        }
    }

    private static void fireBullet(Level world, Player player, ItemStack bulletStack, ToolStack stack) {
        if (!world.isClientSide) {
            BulletEntity entity = new BulletEntity(world, player);
            ToolStack toolStack = ToolStack.from(bulletStack);
            entity.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            float damage = toolStack.getStats().get(ToolStats.ATTACK_DAMAGE);
            float velocity= stack.getStats().get(ToolStats.VELOCITY);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F+velocity, 1.0F);
            entity.setTool(bulletStack);
            entity.setDamage(damage);
            entity.setAttacker(player);
            entity.markFired();
            world.addFreshEntity(entity);
        }
    }
    private List<Component> getRevolverStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        builder.add(ToolStats.ATTACK_DAMAGE);
        builder.add(ToolStats.ATTACK_SPEED);
        builder.add(ToolStats.VELOCITY);
        builder.add(ToolStats.ACCURACY);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    @Override
    public List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        return this.getRevolverStats(tool, player, tooltips, key, flag);
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BOW);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new RevolverClient());
//    }
//    public static class RevolverClient implements IClientItemExtensions {
//        private static final HumanoidModel.ArmPose AIM = HumanoidModel.ArmPose.create("REVOLVER_AIM", false, (model, entity, arm) -> {});
//        private final Map<ItemStack, Map<HumanoidArm, RecoilState>> recoilMap = new WeakHashMap<>();
//        private final Map<ItemStack, Map<HumanoidArm, Integer>> recoilTimeMap = new WeakHashMap<>();
//
//        private static final int RECOIL_CYCLE = 25; //动画周期
//        private static final float MAX_RECOIL_ANGLE = 0.5f;
//
//        private static class RecoilState {
//            float recoil = 0f;
//            int recoilTime = 0;
//            float lastSway = 0f;
//        }
//
//        @Override
//        public @Nullable HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
//            if (!stack.isEmpty() && stack.getItem() instanceof RevolverItem
//                    && entity.isUsingItem() && entity.getUsedItemHand() == hand) {
//                return AIM;
//            }
//            return HumanoidModel.ArmPose.EMPTY;
//        }
//        @Override
//        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack stack,
//                                               float partialTick, float equipProcess, float swingProcess) {
//            if (!(stack.getItem() instanceof RevolverItem)) return false;
//
//            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
//            boolean firstPerson = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
//
//            ToolStack tool = ToolStack.from(stack);
//            ModDataNBT nbt = tool.getPersistentData();
//            int stage = nbt.getInt(SHOOT_STAGE);
//
//            Map<HumanoidArm, RecoilState> stackRecoil = recoilMap.computeIfAbsent(stack, k -> new HashMap<>());
//            RecoilState state = stackRecoil.computeIfAbsent(arm, k -> new RecoilState());
//
//            if (firstPerson) {
//                float baseX = i * 0.56F;
//                float baseY = -0.52F;
//                float baseZ = -0.72F;
//                poseStack.translate(baseX, baseY, baseZ);
//
//                poseStack.translate(0, 0, 0.2F);
//
//                if (stage == 1) {
//                    state.recoil = 0.3f;
//                    state.recoilTime = 0;
//                } else if (stage == 2) {
//                    state.recoilTime = (state.recoilTime + 1) % RECOIL_CYCLE;
//                    float progress = state.recoilTime / (float)RECOIL_CYCLE;
//                    state.recoil = Mth.sin(progress * (float)Math.PI) * MAX_RECOIL_ANGLE;
//                } else {
//                    state.recoil = Math.max(state.recoil - 0.04f, 0f);
//                    state.recoilTime = 0;
//                }
//                float xRot = -0.35f * state.recoil;
//                poseStack.mulPose(Axis.XP.rotation(xRot));
//                poseStack.mulPose(Axis.YP.rotation(i * 0f));
//                poseStack.mulPose(Axis.ZP.rotation(0f));
//                poseStack.translate(0, 0, -0.2F);
//            } else {
//                float baseY = -0.4F;
//                float baseZ = -0.8F;
//                poseStack.translate(i * 0.5F, baseY, baseZ);
//                poseStack.mulPose(Axis.XP.rotation(-0.7F));
//                poseStack.mulPose(Axis.YP.rotation(i * 0.35F));
//                if (stage == 1) {
//                    poseStack.mulPose(Axis.XP.rotation(-0.25F));
//                } else if (stage == 2) {
//                    float progress = state.recoilTime / (float)RECOIL_CYCLE;
//                    float anim = Mth.sin(progress * (float)Math.PI) * 0.35f;
//                    poseStack.mulPose(Axis.XP.rotation(-0.25F - anim));
//                }
//                float swing = Mth.sin(swingProcess * (float) Math.PI);
//                poseStack.mulPose(Axis.YP.rotation(i * swing * 0.15F));
//            }
//            return true;
//        }
//    }
}
