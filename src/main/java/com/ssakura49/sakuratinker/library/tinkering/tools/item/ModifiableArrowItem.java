package com.ssakura49.sakuratinker.library.tinkering.tools.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedToolHook;
import slimeknights.tconstruct.library.tools.helper.*;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.TinkerToolActions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModifiableArrowItem extends ArrowItem implements IModifiableDisplay {
    private final ToolDefinition toolDefinition;
    private ItemStack toolForRendering;

    public ModifiableArrowItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties);
        this.toolDefinition = toolDefinition;
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        ToolStack.ensureInitialized(stack, this.getToolDefinition());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    @Override
    public Entity createEntity(Level world, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(world, original, stack);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (!this.canBeDepleted()) {
            return 0;
        } else {
            ToolStack tool = ToolStack.from(stack);
            int durability = tool.getStats().getInt(ToolStats.DURABILITY);
            if (tool.isBroken()) {
                return durability + 1;
            } else {
                return durability;
            }
        }
    }

    @Override
    public int getDamage(ItemStack stack) {
        return !this.canBeDepleted() ? 0 : ToolStack.from(stack).getDamage();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        ToolDamageUtil.handleDamageItem(stack, amount, damager, onBroken);
        return 0;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getDamageValue() == 1 && DurabilityDisplayModifierHook.showDurabilityBar(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(stack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        return stack.getCount() > 1 || EntityInteractionModifierHook.leftClickEntity(stack, player, target);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return AttributesModifierHook.getHeldAttributeModifiers(tool, slot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return (nbt != null && slot.getType() == EquipmentSlot.Type.HAND)
                ? this.getAttributeModifiers(ToolStack.from(stack), slot)
                : ImmutableMultimap.of();
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this.canPerformAction(stack, TinkerToolActions.SHIELD_DISABLE);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return IsEffectiveToolHook.isEffective(ToolStack.from(stack), state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        return ToolHarvestLogic.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return stack.getCount() == 1 ? MiningSpeedToolHook.getDestroySpeed(stack, state) : 0.0F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.getCount() > 1 || ToolHarvestLogic.handleBlockBreak(stack, pos, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    protected static boolean shouldInteract(@Nullable LivingEntity player, ToolStack toolStack, InteractionHand hand) {
        IModDataView volatileData = toolStack.getVolatileData();
        if (volatileData.getBoolean(NO_INTERACTION)) {
            return false;
        } else if (hand == InteractionHand.OFF_HAND) {
            return true;
        } else {
            return player == null || !volatileData.getBoolean(DEFER_OFFHAND) || player.getOffhandItem().isEmpty();
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT)
                            .beforeBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.BLOCK_INTERACT)
                            .afterBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        ToolStack tool = ToolStack.from(stack);
        if (shouldInteract(playerIn, tool, hand)) {
            for (ModifierEntry entry : tool.getModifierList()) {
                InteractionResult result = entry.getHook(ModifierHooks.ENTITY_INTERACT)
                        .afterEntityUse(tool, entry, playerIn, target, hand, InteractionSource.RIGHT_CLICK);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (stack.getCount() > 1) {
            return InteractionResultHolder.pass(stack);
        } else {
            ToolStack tool = ToolStack.from(stack);
            if (shouldInteract(playerIn, tool, hand)) {
                for (ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = entry.getHook(ModifierHooks.GENERAL_INTERACT)
                            .onToolUse(tool, entry, playerIn, hand, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return new InteractionResultHolder(result, stack);
                    }
                }
            }
            return new InteractionResultHolder(ToolInventoryCapability.tryOpenContainer(stack, tool, playerIn, Util.getSlotType(hand)), stack);
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        if (activeModifier != ModifierEntry.EMPTY) {
            activeModifier.getHook(ModifierHooks.GENERAL_INTERACT)
                    .onUsingTick(tool, activeModifier, entityLiving, timeLeft);
        }
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (super.canContinueUsing(oldStack, newStack) && oldStack != newStack) {
            GeneralInteractionModifierHook.finishUsing(ToolStack.from(oldStack));
        }
        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        if (activeModifier != ModifierEntry.EMPTY) {
            activeModifier.getHook(ModifierHooks.GENERAL_INTERACT)
                    .onFinishUsing(tool, activeModifier, entityLiving);
        }
        GeneralInteractionModifierHook.finishUsing(tool);
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        if (activeModifier != ModifierEntry.EMPTY) {
            activeModifier.getHook(ModifierHooks.GENERAL_INTERACT)
                    .onStoppedUsing(tool, activeModifier, entityLiving, timeLeft);
        }
        GeneralInteractionModifierHook.finishUsing(tool);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY
                ? activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseDuration(tool, activeModifier)
                : 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY
                ? activeModifier.getHook(ModifierHooks.GENERAL_INTERACT).getUseAction(tool, activeModifier)
                : UseAnim.NONE;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return stack.getCount() == 1 && ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    @Override
    public Component getName(ItemStack stack) {
        return TooltipUtil.getDisplayName(stack, this.getToolDefinition());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    @Override
    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    @Override
    public ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }
        return this.toolForRendering;
    }

    public static boolean shouldCauseReequip(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack == newStack) {
            return false;
        } else if (!slotChanged && oldStack.getItem() == newStack.getItem()) {
            ToolStack oldTool = ToolStack.from(oldStack);
            ToolStack newTool = ToolStack.from(newStack);

            if (!oldTool.getMaterials().equals(newTool.getMaterials())) {
                return true;
            } else if (!oldTool.getModifierList().equals(newTool.getModifierList())) {
                return true;
            } else {
                Multimap<Attribute, AttributeModifier> attributesNew = newStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                Multimap<Attribute, AttributeModifier> attributesOld = oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND);

                if (attributesNew.size() != attributesOld.size()) {
                    return true;
                } else {
                    for (Attribute attribute : attributesOld.keySet()) {
                        if (!attributesNew.containsKey(attribute)) {
                            return true;
                        }

                        Iterator<AttributeModifier> iter1 = attributesNew.get(attribute).iterator();
                        Iterator<AttributeModifier> iter2 = attributesOld.get(attribute).iterator();

                        while (iter1.hasNext() && iter2.hasNext()) {
                            if (!iter1.next().equals(iter2.next())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return this.shouldCauseReequipAnimation(oldStack, newStack, false);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return shouldCauseReequip(oldStack, newStack, slotChanged);
    }

    public static HitResult blockRayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return this.toolDefinition;
    }
}
