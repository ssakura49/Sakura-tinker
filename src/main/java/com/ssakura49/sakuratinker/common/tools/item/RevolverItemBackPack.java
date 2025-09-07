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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class RevolverItemBackPack extends ModifiableItem {
    private static final ResourceLocation TAG_CURRENT_CHAMBER = SakuraTinker.location("bullet_slot");
    public static final int MAX_BULLETS = 6;

    public static final ResourceLocation BULLET_1 = SakuraTinker.location("bullet_1");
    public static final ResourceLocation BULLET_2 = SakuraTinker.location("bullet_2");
    public static final ResourceLocation BULLET_3 = SakuraTinker.location("bullet_3");
    public static final ResourceLocation BULLET_4 = SakuraTinker.location("bullet_4");
    public static final ResourceLocation BULLET_5 = SakuraTinker.location("bullet_5");
    public static final ResourceLocation BULLET_6 = SakuraTinker.location("bullet_6");

    public static final ResourceLocation SHOOT = SakuraTinker.location("shoot");

    public static final ResourceLocation RELOAD = SakuraTinker.location("reload");

    public RevolverItemBackPack(Properties props, ToolDefinition def) {
        super(props, def);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
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
        int bulletsFired = 0;
        float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        int longCooldown = (int)(20.0F / speed);
        for (int i = 0; i < MAX_BULLETS; i++) {
            ItemStack bullet = handler.getStackInSlot(currentChamber);
            ToolStack bulletTool = ToolStack.from(bullet);
            if (!bullet.isEmpty() && !bulletTool.isBroken()) {
                fireBullet(world, player, bullet, tool);
                ToolDamageUtil.damageAnimated(bulletTool, 1, player);
                //ItemUtil.addCooldown(player, tool, shortCooldown);
                bulletsFired++;
            }
            currentChamber = (currentChamber + 1) % MAX_BULLETS;
        }
        nbt.putInt(TAG_CURRENT_CHAMBER, currentChamber);
        if (currentChamber == 0 && bulletsFired > 0) {
            ItemUtil.addCooldown(player, tool, longCooldown * bulletsFired);
        }
        return bulletsFired > 0 ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
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
//    @Override
//    public int getUseDuration(ItemStack stack) {
//        return  72000;
//    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new RevolverClient());
    }

    public static class RevolverClient implements IClientItemExtensions {

    }
//
//    public static class RevolverClient implements IClientItemExtensions {
//
//        @Override
//        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand,
//                                               float partialTick, float equipProcess, float swingProcess) {
//            if (!(itemInHand.getItem() instanceof RevolverItem)) return false;
//
//
//            return true;
//        }
//    }

}
