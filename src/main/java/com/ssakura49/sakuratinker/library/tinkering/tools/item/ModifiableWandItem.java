package com.ssakura49.sakuratinker.library.tinkering.tools.item;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.*;
import com.Polarice3.Goety.common.blocks.entities.ArcaBlockEntity;
import com.Polarice3.Goety.common.blocks.entities.BrewCauldronBlockEntity;
import com.Polarice3.Goety.common.entities.neutral.AbstractVine;
import com.Polarice3.Goety.common.events.spell.GoetyEventFactory;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import com.Polarice3.Goety.common.items.magic.*;
import com.Polarice3.Goety.common.magic.spells.wind.FlyingSpell;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.server.SPlayEntitySoundPacket;
import com.Polarice3.Goety.common.network.server.SPlayPlayerSoundPacket;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.*;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedToolHook;
import slimeknights.tconstruct.library.tools.helper.*;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModifiableWandItem extends DarkWand implements IWand, IModifiableDisplay {
    public SpellType spellType;
    private final ToolDefinition toolDefinition;
    private final int maxStackSize;
    private ItemStack toolForRendering;

    public ModifiableWandItem(Properties properties, SpellType spellType, ToolDefinition toolDefinition, int maxStackSize) {
        super(spellType);
        this.toolDefinition = toolDefinition;
        this.spellType = spellType;
        this.maxStackSize = maxStackSize;
    }

    public ModifiableWandItem(Properties properties, SpellType spellType, ToolDefinition toolDefinition) {
        this(properties,spellType,toolDefinition,1);
    }
    @Override
    public SpellType getSpellType() {
        return this.spellType;
    }

    public static Properties wandProperties() {
        return (new Properties()).rarity(Rarity.RARE).setNoRepair().stacksTo(1);
    }
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            CompoundTag compound = stack.getOrCreateTag();
            if (stack.getTag() == null) {
                compound.putInt("Soul Use", this.SoulUse(livingEntity, stack));
                compound.putInt("Soul Cost", 0);
                compound.putInt("Cast Time", this.CastDuration(stack));
                compound.putInt("Cool", 0);
                compound.putInt("Shots", 0);
                compound.putInt("Seconds", 0);
            } else if (!compound.contains("Shots")) {
                compound.putInt("Shots", 0);
            }

            if (this.getSpell(stack) != null) {
                this.setSpellConditions(this.getSpell(stack), stack, livingEntity);
            } else {
                this.setSpellConditions((ISpell)null, stack, livingEntity);
            }

            compound.putInt("Soul Use", this.SoulUse(livingEntity, stack));
            compound.putInt("Cast Time", this.CastDuration(stack));
            if (IWand.getFocus(stack) != null) {
                IWand.getFocus(stack).inventoryTick(worldIn, entityIn, itemSlot, isSelected);
            }
        }
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        CompoundTag compound = pStack.getOrCreateTag();
        compound.putInt("Soul Use", this.SoulUse(pPlayer, pStack));
        compound.putInt("Soul Cost", 0);
        compound.putInt("Cast Time", this.CastDuration(pStack));
        compound.putInt("Cool", 0);
        compound.putInt("Seconds", 0);
        compound.putInt("Shots", 0);
        this.setSpellConditions((ISpell)null, pStack, pPlayer);
        ToolStack.ensureInitialized(pStack, this.getToolDefinition());
    }
    @Override
    public int SoulUse(LivingEntity entityLiving, ItemStack stack) {
        return IWand.getFocus(stack).isEnchanted() && (Boolean) SpellConfig.EnchantMultiCost.get() ? (int)((float)(this.SoulCost(stack) * 2) * SEHelper.soulDiscount(entityLiving)) : (int)((float)this.SoulCost(stack) * SEHelper.soulDiscount(entityLiving));
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        boolean flag = false;
        if (!player.level.isClientSide && entity instanceof LivingEntity target) {
            if (target instanceof IOwned owned) {
                if (owned.getTrueOwner() != player) {
                    LivingEntity summonedEntity = owned.getTrueOwner();
                    if (!(summonedEntity instanceof IOwned)) {
                        return flag;
                    }
                    IOwned owned1 = (IOwned)summonedEntity;
                    if (owned1.getTrueOwner() != player) {
                        return flag;
                    }
                }
                if (IWand.getFocus(stack).getItem() instanceof CallFocus && !CallFocus.hasSummon(IWand.getFocus(stack))) {
                    CompoundTag compoundTag = new CompoundTag();
                    if (IWand.getFocus(stack).hasTag()) {
                        compoundTag = IWand.getFocus(stack).getTag();
                    }
                    CallFocus.setSummon(compoundTag, target);
                    IWand.getFocus(stack).setTag(compoundTag);
                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    flag = true;
                } else if (IWand.getFocus(stack).getItem() instanceof TroopFocus && !TroopFocus.hasSummonType(IWand.getFocus(stack))) {
                    CompoundTag compoundTag = new CompoundTag();
                    if (IWand.getFocus(stack).hasTag()) {
                        compoundTag = IWand.getFocus(stack).getTag();
                    }
                    TroopFocus.setSummonType(compoundTag, target.getType());
                    IWand.getFocus(stack).setTag(compoundTag);
                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    flag = true;
                } else {
                    label109: {
                        if (IWand.getFocus(stack).getItem() instanceof CommandFocus && owned instanceof IServant) {
                            IServant servant = (IServant)owned;
                            if (servant.canBeCommanded() && !CommandFocus.hasServant(IWand.getFocus(stack))) {
                                CompoundTag compoundTag = new CompoundTag();
                                if (IWand.getFocus(stack).hasTag()) {
                                    compoundTag = IWand.getFocus(stack).getTag();
                                }

                                CommandFocus.setServant(compoundTag, target);
                                IWand.getFocus(stack).setTag(compoundTag);
                                player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                                ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                                flag = true;
                                break label109;
                            }
                        }

                        if (IWand.getFocus(stack).getItem() instanceof OrderFocus && owned instanceof IServant) {
                            IServant servant = (IServant)owned;
                            if (servant.canBeCommanded()) {
                                List<LivingEntity> list = OrderFocus.getServants(IWand.getFocus(stack));
                                if ((list.isEmpty() || list.size() < 8) && !list.contains(target)) {
                                    OrderFocus.setServants(IWand.getFocus(stack), player, target);
                                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                                    flag = true;
                                }
                            }
                        }
                    }
                }
                if (!flag) {
                    if (owned instanceof IServant summonedEntity) {
                        if (!player.isShiftKeyDown() && !player.isCrouching()) {
                            if ((Boolean)SpellConfig.OwnerHitCommand.get() && summonedEntity.canUpdateMove()) {
                                summonedEntity.updateMoveMode(player);
                                flag = true;
                            }
                        } else if ((Integer)SpellConfig.OwnerHitKill.get() == 0) {
                            summonedEntity.tryKill(player);
                            flag = true;
                        }
                    } else if (owned instanceof AbstractVine) {
                        AbstractVine vine = (AbstractVine)owned;
                        if ((player.isShiftKeyDown() || player.isCrouching()) && (Integer)SpellConfig.OwnerHitKill.get() == 0) {
                            vine.kill();
                            flag = true;
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (IWand.getFocus(stack).getItem() instanceof CommandFocus) {
            LivingEntity serverLevel = CommandFocus.getServant(IWand.getFocus(stack));
            if (serverLevel instanceof IServant) {
                IServant summoned = (IServant)serverLevel;
                if (summoned != target && summoned.getTrueOwner() == player && target.distanceTo(player) <= 64.0F) {
                    summoned.setCommandPosEntity(target);
                    player.playSound((SoundEvent) ModSounds.COMMAND.get(), 1.0F, 0.45F);
                    if (!player.level.isClientSide) {
                        ModNetwork.sendTo(player, new SPlayPlayerSoundPacket((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F));
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        } else if (IWand.getFocus(stack).getItem() instanceof OrderFocus && !OrderFocus.getServants(IWand.getFocus(stack)).isEmpty()) {
            int i = 0;
            for(LivingEntity livingEntity : OrderFocus.getServants(IWand.getFocus(stack))) {
                if (livingEntity instanceof IServant) {
                    IServant summoned = (IServant)livingEntity;
                    if (summoned != target && summoned.getTrueOwner() == player && target.distanceTo(player) <= 64.0F) {
                        summoned.setCommandPosEntityOrder(target);
                        ++i;
                    }
                }
            }
            if (i > 0) {
                player.playSound((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F);
                if (!player.level.isClientSide) {
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F));
                }

                return InteractionResult.SUCCESS;
            }
        }
        if (target instanceof IOwned owned) {
            label87: {
                if (owned.getTrueOwner() != player) {
                    LivingEntity var16 = owned.getTrueOwner();
                    if (!(var16 instanceof IOwned)) {
                        break label87;
                    }

                    IOwned owned1 = (IOwned)var16;
                    if (owned1.getTrueOwner() != player) {
                        break label87;
                    }
                }

                if (owned instanceof IServant summonedEntity) {
                    if (!player.isShiftKeyDown() && !player.isCrouching()) {
                        if (!(Boolean)SpellConfig.OwnerHitCommand.get() && summonedEntity.canUpdateMove()) {
                            summonedEntity.updateMoveMode(player);
                            return InteractionResult.SUCCESS;
                        }
                    } else if ((Integer)SpellConfig.OwnerHitKill.get() == 1) {
                        summonedEntity.tryKill(player);
                        return InteractionResult.SUCCESS;
                    }
                } else if (owned instanceof AbstractVine vine) {
                    if ((player.isShiftKeyDown() || player.isCrouching()) && (Integer)SpellConfig.OwnerHitKill.get() == 1) {
                        vine.kill();
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        ISpell var14 = this.getSpell(stack);
        if (var14 instanceof ITouchSpell touchSpells) {
            if (this.canCastTouch(stack, player.level, player)) {
                Level var18 = player.level;
                if (var18 instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)var18;
                    touchSpells.touchResult(serverLevel, player, target, stack, touchSpells.defaultStats());
                }

                return InteractionResult.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        InteractionHand hand = pContext.getHand();
        ItemStack stack = pContext.getItemInHand();
        if (player != null) {
            Item compoundtag = IWand.getFocus(stack).getItem();
            if (compoundtag instanceof RecallFocus) {
                RecallFocus recallFocus = (RecallFocus)compoundtag;
                CompoundTag compoundTag = IWand.getFocus(stack).getOrCreateTag();
                if (!RecallFocus.hasRecall(IWand.getFocus(stack))) {
                    BlockEntity tileEntity = level.getBlockEntity(blockpos);
                    if (tileEntity instanceof ArcaBlockEntity) {
                        ArcaBlockEntity arcaTile = (ArcaBlockEntity)tileEntity;
                        if (pContext.getPlayer() == arcaTile.getPlayer() && arcaTile.getLevel() != null) {
                            recallFocus.addRecallTags(arcaTile.getLevel().dimension(), arcaTile.getBlockPos(), compoundTag);
                            IWand.getFocus(stack).setTag(compoundTag);
                            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                            if (!level.isClientSide) {
                                ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                            }

                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                    }

                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.is(ModTags.Blocks.RECALL_BLOCKS)) {
                        recallFocus.addRecallTags(level.dimension(), blockpos, compoundTag);
                        IWand.getFocus(stack).setTag(compoundTag);
                        player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                        if (!level.isClientSide) {
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            } else if (IWand.getFocus(stack).getItem() instanceof CommandFocus) {
                if (CommandFocus.hasServant(IWand.getFocus(stack))) {
                    LivingEntity livingEntity = CommandFocus.getServant(IWand.getFocus(stack));
                    if (livingEntity instanceof IServant) {
                        IServant summoned = (IServant)livingEntity;
                        livingEntity = CommandFocus.getServant(IWand.getFocus(stack));
                        if (livingEntity != null && summoned.getTrueOwner() == player && livingEntity.distanceTo(player) <= 64.0F) {
                            BlockPos above = blockpos.above();
                            boolean flag = false;
                            if (summoned.canCommandToBlock(level, blockpos)) {
                                summoned.setCommandPos(blockpos);
                                flag = true;
                            } else if (summoned.canCommandToBlock(level, above)) {
                                summoned.setCommandPos(above);
                                flag = true;
                            }

                            if (flag) {
                                player.playSound((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F);
                                if (!level.isClientSide) {
                                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F));
                                }

                                return InteractionResult.sidedSuccess(level.isClientSide);
                            }
                        }
                    }
                }
            } else if (!(IWand.getFocus(stack).getItem() instanceof OrderFocus)) {
                ISpell spell2 = this.getSpell(stack);
                if (spell2 instanceof IBlockSpell) {
                    IBlockSpell blockSpell0 = (IBlockSpell)spell2;
                    spell2 = GoetyEventFactory.onBlockBasedSpell(player.level, blockpos, player.level.getBlockState(blockpos), blockSpell0, pContext.getClickedFace(), player);
                    if (spell2 instanceof IBlockSpell) {
                        IBlockSpell blockSpell = (IBlockSpell)spell2;
                        Level var32 = player.level;
                        if (var32 instanceof ServerLevel) {
                            ServerLevel serverLevel = (ServerLevel)var32;
                            if (blockSpell.rightBlock(serverLevel, player, blockpos, pContext.getClickedFace())) {
                                if (this.canCastTouch(stack, level, player)) {
                                    blockSpell.blockResult(serverLevel, player, stack, blockpos, pContext.getClickedFace());
                                }

                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                } else {
                    label147: {
                        if (level.getBlockState(blockpos).is(BlockTags.BANNERS)) {
                            BlockEntity var19 = level.getBlockEntity(blockpos);
                            if (var19 instanceof BannerBlockEntity) {
                                BannerBlockEntity bannerBlock = (BannerBlockEntity)var19;
                                if (!level.isClientSide) {
                                    CompoundTag compoundtags = BlockItem.getBlockEntityData(bannerBlock.getItem());
                                    if (compoundtags != null && compoundtags.contains("Patterns")) {
                                        SEHelper.setBannerBaseColor(player, bannerBlock.getBaseColor());
                                        SEHelper.setBannerPattern(player, compoundtags.getList("Patterns", 10));
                                        player.displayClientMessage(Component.translatable("info.goety.banner.add", new Object[]{player.getDisplayName()}), true);
                                        level.playSound((Player)null, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (SoundEvent)ModSounds.CAST_SPELL.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
                                        return InteractionResult.SUCCESS;
                                    }
                                }
                                break label147;
                            }
                        }

                        if (!level.getBlockState(blockpos).isAir() && !level.isClientSide) {
                            return level.getBlockState(blockpos).use(level, player, hand, new BlockHitResult(pContext.getClickLocation(), pContext.getClickedFace(), pContext.getClickedPos(), pContext.isInside()));
                        }
                    }
                }
            } else if (!OrderFocus.getServants(IWand.getFocus(stack)).isEmpty()) {
                int i = 0;

                for(LivingEntity livingEntity : OrderFocus.getServants(IWand.getFocus(stack))) {
                    if (livingEntity instanceof IServant) {
                        IServant summoned = (IServant)livingEntity;
                        if (summoned.canBeCommanded() && summoned.getTrueOwner() == player && livingEntity.distanceTo(player) <= 64.0F) {
                            BlockPos above = blockpos.above();
                            if (summoned.canCommandToBlock(level, blockpos)) {
                                summoned.setCommandPos(blockpos);
                                ++i;
                            } else if (summoned.canCommandToBlock(level, above)) {
                                summoned.setCommandPos(above);
                                ++i;
                            }
                        }
                    }
                }

                if (i > 0) {
                    player.playSound((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F);
                    if (!level.isClientSide) {
                        ModNetwork.sendTo(player, new SPlayPlayerSoundPacket((SoundEvent)ModSounds.COMMAND.get(), 1.0F, 0.45F));
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            if (!level.isClientSide) {
                BlockEntity var16 = level.getBlockEntity(blockpos);
                if (var16 instanceof BrewCauldronBlockEntity) {
                    BrewCauldronBlockEntity cauldronBlock = (BrewCauldronBlockEntity)var16;
                    if (MobUtil.isShifting(player) && stack.getItem() instanceof IWand) {
                        cauldronBlock.fullReset();
                        level.playSound((Player)null, blockpos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.playSound((Player)null, blockpos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            if (shouldInteract(pContext.getPlayer(), tool, hand)) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = ((BlockInteractionModifierHook)entry.getHook(ModifierHooks.BLOCK_INTERACT)).afterBlockUse(tool, entry, pContext, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }
        return super.useOn(pContext);
    }
    @Override
    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        ISpell iSpell = GoetyEventFactory.onStartSpell(livingEntityIn, stack, this.getSpell(stack));
        if (worldIn instanceof ServerLevel && this.cannotCast(livingEntityIn, stack, iSpell)) {
            livingEntityIn.stopUsingItem();
        } else {
            int CastTime = stack.getUseDuration() - count;
            if (livingEntityIn.getUseItem() == stack && iSpell != null && this.isNotInstant(iSpell, livingEntityIn, stack)) {
                SoundEvent soundevent = this.CastingSound(stack, livingEntityIn);
                if (CastTime == 1 && soundevent != null) {
                    if (worldIn instanceof ServerLevel) {
                        ServerLevel serverLevel = (ServerLevel)worldIn;
                        iSpell.startSpell(serverLevel, livingEntityIn, stack, iSpell.defaultStats());
                    }

                    worldIn.playSound((Player)null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundSource.PLAYERS, this.castingVolume(stack), this.castingPitch(stack));
                }
                if (worldIn instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)worldIn;
                    iSpell = GoetyEventFactory.onCastingSpell(livingEntityIn, stack, iSpell, CastTime);
                    if (iSpell != null) {
                        iSpell.useSpell(serverLevel, livingEntityIn, stack, CastTime, iSpell.defaultStats());
                    } else {
                        livingEntityIn.stopUsingItem();
                    }
                }
                if (iSpell != null) {
                    label65: {
                        if (iSpell instanceof IChargingSpell) {
                            IChargingSpell spell = (IChargingSpell)iSpell;
                            if (spell.castUp(livingEntityIn, stack) > 0) {
                                this.useParticles(worldIn, livingEntityIn, stack, iSpell);
                                break label65;
                            }
                        }

                        if (!(iSpell instanceof IChargingSpell)) {
                            this.useParticles(worldIn, livingEntityIn, stack, iSpell);
                        }
                    }

                    if (iSpell instanceof IChargingSpell) {
                        IChargingSpell spell = (IChargingSpell)iSpell;
                        if (stack.getTag() != null && (CastTime >= spell.castUp(livingEntityIn, stack) || spell.castUp(livingEntityIn, stack) <= 0)) {
                            stack.getTag().putInt("Cool", stack.getTag().getInt("Cool") + 1);
                            if (stack.getTag().getInt("Cool") >= this.Cooldown(stack)) {
                                stack.getTag().putInt("Cool", 0);
                                if (spell.shotsNumber(livingEntityIn, stack) > 0) {
                                    this.increaseShots(stack);
                                }

                                this.MagicResults(stack, worldIn, livingEntityIn, spell);
                            }
                        }

                        if (livingEntityIn instanceof Player) {
                            Player player = (Player)livingEntityIn;
                            if (!SEHelper.getSoulsAmount(player, iSpell.soulCost(player, stack)) && !player.isCreative()) {
                                player.stopUsingItem();
                            }
                        }
                    }
                } else {
                    livingEntityIn.stopUsingItem();
                }
            }
        }
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int useTimeRemaining) {
        if (level instanceof ServerLevel serverLevel) {
            int CastTime = stack.getUseDuration() - useTimeRemaining;
            ISpell spell = GoetyEventFactory.onStopSpell(livingEntity, stack, this.getSpell(stack), CastTime, useTimeRemaining);
            if (spell != null) {
                spell.stopSpell(serverLevel, livingEntity, stack, IWand.getFocus(stack), CastTime, spell.defaultStats());
                if (livingEntity instanceof Player) {
                    Player player = (Player)livingEntity;
                    if (spell instanceof IChargingSpell) {
                        IChargingSpell chargeSpell = (IChargingSpell)spell;
                        if (chargeSpell.shotsNumber(player, stack) > 0) {
                            if (this.ShotsFired(stack) > 0) {
                                float coolPercent = (float)this.ShotsFired(stack) / (float)chargeSpell.shotsNumber(player, stack);
                                this.setShots(stack, 0);
                                SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), Mth.floor((float)chargeSpell.spellCooldown() * coolPercent));
                            }
                        } else {
                            SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), Mth.floor((float)chargeSpell.spellCooldown()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Cast Time")) {
            return stack.getTag().getInt("Cast Time");
        }
        return this.CastDuration(stack);
    }
    @Nonnull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        ISpell iSpell = GoetyEventFactory.onCastSpell(entityLiving, this.getSpell(stack));
        if (iSpell != null && (!(iSpell instanceof IChargingSpell) || this.isNotInstant(iSpell, entityLiving, stack) || this.notTouch(iSpell)) && !this.cannotCast(entityLiving, stack)) {
            this.MagicResults(stack, worldIn, entityLiving, iSpell);
        }

        if (stack.getTag() != null) {
            if (stack.getTag().getInt("Cool") > 0) {
                stack.getTag().putInt("Cool", 0);
            }

            if (stack.getTag().getInt("Shots") > 0) {
                stack.getTag().putInt("Shots", 0);
            }
        }
        return stack;
    }
    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        ItemStack focus = IWand.getFocus(itemstack);
        if (focus.getItem() instanceof CommandFocus && playerIn.isCrouching()) {
            if (CommandFocus.hasServant(focus) && focus.getTag() != null) {
                focus.getTag().remove("Servant");
                playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                if (!worldIn.isClientSide) {
                    ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                }
            }
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else if (focus.getItem() instanceof OrderFocus && playerIn.isCrouching()) {
            if (focus.getTag() != null) {
                focus.getTag().remove(OrderFocus.SERVANT_LIST);
                focus.getTag().remove(OrderFocus.SERVANT_CLIENT_LIST);
                playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                if (!worldIn.isClientSide) {
                    ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                }
            }
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else if (focus.getItem() instanceof CallFocus && playerIn.isCrouching()) {
            if (CallFocus.hasSummon(focus) && focus.getTag() != null) {
                focus.getTag().remove("Summoned");
                playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                if (!worldIn.isClientSide) {
                    ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                }
            }
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else {
            if (this.getSpell(itemstack) != null) {
                if (this.cannotCast(playerIn, itemstack)) {
                    return InteractionResultHolder.pass(itemstack);
                }

                if (this.isNotInstant(this.getSpell(itemstack), playerIn, itemstack)) {
                    if ((SEHelper.getSoulsAmount(playerIn, this.getSpell(itemstack).soulCost(playerIn, itemstack)) || playerIn.getAbilities().instabuild) && !worldIn.isClientSide) {
                        playerIn.startUsingItem(handIn);
                    }
                } else if (this.notTouch(this.getSpell(itemstack))) {
                    playerIn.swing(handIn);
                    ISpell iSpell = GoetyEventFactory.onCastSpell(playerIn, this.getSpell(itemstack));
                    this.MagicResults(itemstack, worldIn, playerIn, iSpell);
                }
            }
            return InteractionResultHolder.consume(itemstack);
        }
    }
    @Override
    public void setSpellConditions(@Nullable ISpell spell, ItemStack stack, LivingEntity livingEntity) {
        if (stack.getTag() != null) {
            if (spell != null) {
                stack.getTag().putInt("Soul Cost", spell.soulCost(livingEntity, stack));
                stack.getTag().putInt("Duration", spell.castDuration(livingEntity, stack));
                if (spell instanceof IChargingSpell) {
                    IChargingSpell chargingSpell = (IChargingSpell)spell;
                    stack.getTag().putInt("Cooldown", chargingSpell.Cooldown(livingEntity, stack, stack.getTag().contains("Shots") ? stack.getTag().getInt("Shots") : 0));
                } else {
                    stack.getTag().putInt("Cooldown", 0);
                }
            } else {
                stack.getTag().putInt("Soul Cost", 0);
                stack.getTag().putInt("Duration", 0);
                stack.getTag().putInt("Cooldown", 0);
            }
        }

    }
    @Override
    public int SoulCost(ItemStack itemStack) {
        return itemStack.getTag() == null ? 0 : itemStack.getTag().getInt("Soul Cost");
    }
    @Override
    public int CastDuration(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getInt("Duration") : 0;
    }
    @Override
    public int Cooldown(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getInt("Cooldown") : 0;
    }
    @Override
    public int ShotsFired(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getInt("Shots") : 0;
    }
    @Override
    public void increaseShots(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            itemStack.getTag().putInt("Shots", this.ShotsFired(itemStack) + 1);
        }

    }
    @Override
    public void setShots(ItemStack itemStack, int amount) {
        if (itemStack.getTag() != null) {
            itemStack.getTag().putInt("Shots", amount);
        }

    }
    @Override
    @Nullable
    public SoundEvent CastingSound(ItemStack stack, LivingEntity caster) {
        return this.getSpell(stack) != null ? this.getSpell(stack).CastingSound(caster) : null;
    }
    @Override
    public float castingVolume(ItemStack stack) {
        return this.getSpell(stack) != null ? this.getSpell(stack).castingVolume() : 0.5F;
    }
    @Override
    public float castingPitch(ItemStack stack) {
        return this.getSpell(stack) != null ? this.getSpell(stack).castingPitch() : 1.0F;
    }
    @Override
    public boolean canCastTouch(ItemStack stack, Level worldIn, LivingEntity caster) {
        Player playerEntity = (Player)caster;
        if (!worldIn.isClientSide) {
            ISpell spell = GoetyEventFactory.onTouchBasedSpell(caster, stack, this.getSpell(stack));
            if (spell != null && !this.cannotCast(caster, stack, spell)) {
                if (playerEntity.isCreative()) {
                    SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                    return stack.getTag() != null;
                }

                if (SEHelper.getSoulsAmount(playerEntity, this.SoulUse(caster, stack)) && stack.getTag() != null) {
                    SEHelper.decreaseSouls(playerEntity, this.SoulUse(caster, stack));
                    SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                    SEHelper.sendSEUpdatePacket(playerEntity);
                    return true;
                }
            }
        }

        return false;
    }
    @Override
    public void MagicResults(ItemStack stack, Level worldIn, LivingEntity caster, ISpell spell) {
        if (spell != null && caster instanceof Player playerEntity) {
            if (!worldIn.isClientSide) {
                ServerLevel serverWorld = (ServerLevel)worldIn;
                if (playerEntity.isCreative()) {
                    if (stack.getTag() != null) {
                        spell.SpellResult(serverWorld, caster, stack, spell.defaultStats());
                        boolean flag = false;
                        if (spell instanceof IChargingSpell) {
                            IChargingSpell chargingSpell = (IChargingSpell)spell;
                            if (chargingSpell.shotsNumber(playerEntity, stack) > 0 && this.ShotsFired(stack) >= chargingSpell.shotsNumber(playerEntity, stack)) {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }

                        if (flag) {
                            this.setShots(stack, 0);
                            SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                        }
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, this.SoulUse(caster, stack))) {
                    boolean spent = true;
                    if (spell instanceof IChargingSpell) {
                        IChargingSpell spell1 = (IChargingSpell)spell;
                        if (spell1.everCharge() && stack.getTag() != null) {
                            stack.getTag().putInt("Seconds", stack.getTag().getInt("Seconds") + 1);
                            if (stack.getTag().getInt("Seconds") != 20) {
                                spent = false;
                            } else {
                                stack.getTag().putInt("Seconds", 0);
                            }
                        }
                    }

                    if (spent) {
                        SEHelper.decreaseSouls(playerEntity, this.SoulUse(caster, stack));
                        SEHelper.sendSEUpdatePacket(playerEntity);
                        if ((Integer) MobsConfig.VillagerHateSpells.get() > 0) {
                            for(Villager villager : caster.level.getEntitiesOfClass(Villager.class, caster.getBoundingBox().inflate((double)16.0F))) {
                                if (villager.hasLineOfSight(caster)) {
                                    villager.getGossips().add(caster.getUUID(), GossipType.MINOR_NEGATIVE, (Integer)MobsConfig.VillagerHateSpells.get());
                                }
                            }
                        }
                    }

                    if (stack.getTag() != null) {
                        spell.SpellResult(serverWorld, caster, stack, spell.defaultStats());
                        boolean flag = false;
                        if (spell instanceof IChargingSpell) {
                            IChargingSpell chargingSpell = (IChargingSpell)spell;
                            if (chargingSpell.shotsNumber(playerEntity, stack) > 0 && this.ShotsFired(stack) >= chargingSpell.shotsNumber(playerEntity, stack)) {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }

                        if (flag) {
                            this.setShots(stack, 0);
                            SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown());
                        }
                    }
                } else {
                    worldIn.playSound((Player)null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
                }
            }

            if (worldIn.isClientSide) {
                if (playerEntity.isCreative()) {
                    if (spell instanceof IBreathingSpell) {
                        IBreathingSpell breathingSpells = (IBreathingSpell)spell;
                        breathingSpells.showWandBreath(caster);
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, this.SoulUse(caster, stack))) {
                    if (spell instanceof IBreathingSpell) {
                        IBreathingSpell breathingSpells = (IBreathingSpell)spell;
                        breathingSpells.showWandBreath(caster);
                    }
                } else {
                    this.failParticles(worldIn, caster);
                }
            }
        } else {
            this.failParticles(worldIn, caster);
            worldIn.playSound((Player)null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }

    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        TooltipUtil.addInformation(this, stack, worldIn, tooltip, SafeClientAccess.getTooltipKey(), flagIn);
    }
    @Override
    public void addInformationAfterShift(Item item, List<Component> tooltip) {
        tooltip.add(Component.translatable(item.getDescriptionId() + ".info").withStyle(ChatFormatting.GRAY));
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new DarkWandClient());
    }

    public static class DarkWandClient implements IClientItemExtensions {
        private static final HumanoidModel.ArmPose SPELL = HumanoidModel.ArmPose.create("GOETY_SPELL", false, (model, entity, arm) -> {
            float f5 = entity.walkAnimation.position(Minecraft.getInstance().getPartialTick());
            if (arm == HumanoidArm.RIGHT) {
                ModelPart var10000 = model.rightArm;
                var10000.xRot -= MathHelper.modelDegrees(105.0F);
                model.rightArm.zRot = Mth.cos(f5 * 0.6662F) * 0.25F;
                var10000 = model.leftArm;
                var10000.xRot += MathHelper.modelDegrees(25.0F);
            } else {
                ModelPart var5 = model.leftArm;
                var5.xRot -= MathHelper.modelDegrees(105.0F);
                model.leftArm.zRot = -Mth.cos(f5 * 0.6662F) * 0.25F;
                var5 = model.rightArm;
                var5.xRot += MathHelper.modelDegrees(25.0F);
            }

        });
        private static final HumanoidModel.ArmPose FLIGHT_POSE = HumanoidModel.ArmPose.create("GOETY_FLYING", false, (model, entity, arm) -> {
            float f5 = 1.0F;
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(105.0F);
                model.rightArm.zRot = Mth.cos(f5 * 0.6662F) * 0.25F;
                model.leftArm.xRot = MathHelper.modelDegrees(25.0F);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(105.0F);
                model.leftArm.zRot = -Mth.cos(f5 * 0.6662F) * 0.25F;
                model.rightArm.xRot = MathHelper.modelDegrees(25.0F);
            }

            model.rightLeg.xRot = MathHelper.modelDegrees(17.5F);
            model.leftLeg.xRot = MathHelper.modelDegrees(17.5F);
            ModelPart var10000 = model.rightLeg;
            var10000.xRot += 1.0F * Mth.sin(Minecraft.getInstance().getPartialTick() * 0.067F) * 0.05F;
            var10000 = model.leftLeg;
            var10000.xRot += -1.0F * Mth.sin(Minecraft.getInstance().getPartialTick() * 0.067F) * 0.05F;
        });
        private static final HumanoidModel.ArmPose HOLD_STAFF = HumanoidModel.ArmPose.create("HOLD_STAFF", false, (model, entity, arm) -> {
            float f5 = entity.walkAnimation.position(Minecraft.getInstance().getPartialTick());
            if (arm == HumanoidArm.RIGHT) {
                ModelPart var10000 = model.rightArm;
                var10000.xRot -= MathHelper.modelDegrees(90.0F);
                model.rightArm.zRot = Mth.cos(f5 * 0.6662F) * 0.1F;
            } else {
                ModelPart var4 = model.leftArm;
                var4.xRot -= MathHelper.modelDegrees(90.0F);
                model.leftArm.zRot = -Mth.cos(f5 * 0.6662F) * 0.1F;
            }

        });

        public DarkWandClient() {
        }

        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof IWand && entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                ISpell spell = WandUtil.getSpell(entityLiving);
                if (spell != null) {
                    if (spell instanceof FlyingSpell) {
                        return FLIGHT_POSE;
                    }

                    return SPELL;
                }
            }

            return HumanoidModel.ArmPose.EMPTY;
        }

        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            if (player.isUsingItem()) {
                this.applyItemArmTransform(poseStack, arm, equipProcess);
                poseStack.translate((double)((float)i * -0.2785682F), (double)0.18344387F, (double)0.15731531F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-13.935F));
                poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 35.3F));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * -9.785F));
                float f8 = (float)itemInHand.getUseDuration() - ((float)player.getUseItemRemainingTicks() - partialTick + 1.0F);
                float f12 = f8 / 20.0F;
                f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                if (f12 > 1.0F) {
                    f12 = 1.0F;
                }

                if (f12 > 0.1F) {
                    float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                    float f18 = f12 - 0.1F;
                    float f20 = f15 * f18;
                    poseStack.translate((double)(f20 * 0.0F), (double)(f20 * 0.004F), (double)(f20 * 0.0F));
                }

                poseStack.translate((double)(f12 * 0.0F), (double)(f12 * 0.0F), (double)(f12 * 0.04F));
                poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                poseStack.mulPose(Axis.YN.rotationDegrees((float)i * 45.0F));
            } else {
                float f5 = -0.4F * Mth.sin(Mth.sqrt(swingProcess) * (float)Math.PI);
                float f6 = 0.2F * Mth.sin(Mth.sqrt(swingProcess) * ((float)Math.PI * 2F));
                float f10 = -0.2F * Mth.sin(swingProcess * (float)Math.PI);
                poseStack.translate((double)((float)i * f5), (double)f6, (double)f10);
                this.applyItemArmTransform(poseStack, arm, equipProcess);
                this.applyItemArmAttackTransform(poseStack, arm, swingProcess);
            }

            return true;
        }

        private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((double)((float)i * 0.56F), (double)(-0.52F + equipProcess * -0.6F), (double)-0.72F);
        }

        private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm humanoidArm, float swingProcess) {
            int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
            float f = Mth.sin(swingProcess * swingProcess * (float)Math.PI);
            poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
            float f1 = Mth.sin(Mth.sqrt(swingProcess) * (float)Math.PI);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
        }
    }

    public int getMaxStackSize(ItemStack stack) {
        return stack.isDamaged() ? 1 : this.maxStackSize;
    }

    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        SoulUsingItemCapability soulCap = new SoulUsingItemCapability(stack);
        ToolCapabilityProvider tinkerCap = new ToolCapabilityProvider(stack);

        return new ICapabilitySerializable<Tag>() {
            @Override
            public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                LazyOptional<T> fromSoul = soulCap.getCapability(cap, side);
                if (fromSoul.isPresent()) return fromSoul;

                LazyOptional<T> fromTinker = tinkerCap.getCapability(cap, side);
                if (fromTinker.isPresent()) return fromTinker;

                return LazyOptional.empty();
            }

            @Override
            public Tag serializeNBT() {
                return soulCap.serializeNBT();
            }

            @Override
            public void deserializeNBT(Tag nbt) {
                soulCap.deserializeNBT(nbt);
            }
        };
    }
//    @Nullable
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
//        return new ToolCapabilityProvider(stack);
//    }
    @Override
    public void verifyTagAfterLoad(@NotNull CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return RarityModule.getRarity(stack);
    }
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(world, original, stack);
    }
    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }
    @Override
    public boolean canBeDepleted() {
        return true;
    }
    @Override
    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
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
        return stack.getCount() == 1 && DurabilityDisplayModifierHook.showDurabilityBar(stack);
    }
    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }
    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }


    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return AttributesModifierHook.getHeldAttributeModifiers(tool, slot);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && slot.getType() == EquipmentSlot.Type.HAND ? this.getAttributeModifiers((IToolStackView)ToolStack.from(stack), (EquipmentSlot)slot) : ImmutableMultimap.of();
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this.canPerformAction(stack, TinkerToolActions.SHIELD_DISABLE);
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return IsEffectiveToolHook.isEffective(ToolStack.from(stack), state);
    }
    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull BlockState state, @NotNull BlockPos pos, LivingEntity entityLiving) {
        return ToolHarvestLogic.mineBlock(stack, worldIn, state, pos, entityLiving);
    }
    @Override
    public float getDestroySpeed(ItemStack stack, @NotNull BlockState state) {
        return stack.getCount() == 1 ? MiningSpeedToolHook.getDestroySpeed(stack, state) : 0.0F;
    }
    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.getCount() > 1 || ToolHarvestLogic.handleBlockBreak(stack, pos, player);
    }
    @Override
    public boolean overrideStackedOnOther(ItemStack held, Slot slot, ClickAction action, Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
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
                for(ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = ((BlockInteractionModifierHook)entry.getHook(ModifierHooks.BLOCK_INTERACT)).beforeBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (super.canContinueUsing(oldStack, newStack) && oldStack != newStack) {
            GeneralInteractionModifierHook.finishUsing(ToolStack.from(oldStack));
        }

        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        UsingToolModifierHook.afterStopUsing(tool, entity, timeLeft);
        GeneralInteractionModifierHook.finishUsing(tool);
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
    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    @Override
    public @NotNull ItemStack getRenderTool() {
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
                    for(Attribute attribute : attributesOld.keySet()) {
                        if (!attributesNew.containsKey(attribute)) {
                            return true;
                        }

                        Iterator<AttributeModifier> iter1 = attributesNew.get(attribute).iterator();
                        Iterator<AttributeModifier> iter2 = attributesOld.get(attribute).iterator();

                        while(iter1.hasNext() && iter2.hasNext()) {
                            if (!((AttributeModifier)iter1.next()).equals(iter2.next())) {
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

    public static BlockHitResult blockRayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
    }
    @Override
    public @NotNull ToolDefinition getToolDefinition() {
        return this.toolDefinition;
    }
}
