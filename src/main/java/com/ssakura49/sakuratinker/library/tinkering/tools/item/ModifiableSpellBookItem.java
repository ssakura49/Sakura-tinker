package com.ssakura49.sakuratinker.library.tinkering.tools.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModifiableSpellBookItem extends SpellBook implements IModifiableDisplay {
    private final ToolDefinition toolDefinition;
    private ItemStack toolForRendering;
    private final int maxStackSize;
    protected final int maxSpellSlots;
    protected final SpellRarity spellRarity;

    public ModifiableSpellBookItem(int maxSpellSlots,Item.Properties properties, SpellRarity spellRarity, ToolDefinition toolDefinition) {
        this(maxSpellSlots ,properties, spellRarity, toolDefinition ,1);
    }

    public ModifiableSpellBookItem(int maxSpellSlots, Item.Properties properties, SpellRarity spellRarity,ToolDefinition toolDefinition, int maxStackSize) {
        super(maxSpellSlots, SpellRarity.LEGENDARY, new Item.Properties().stacksTo(maxStackSize));
        this.toolDefinition = toolDefinition;
        this.maxSpellSlots = maxSpellSlots;
        this.maxStackSize = maxStackSize;
        this.spellRarity = SpellRarity.LEGENDARY;
    }

    @Override
    public SpellRarity getRarity() {
        return this.rarity;
    }

    @Override
    public int getMaxSpellSlots() {
        return this.maxSpellSlots;
    }

    @Override
    public boolean isUnique() {
        return false;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (this.isUnique()) {
            tooltip.add(Component.translatable("tooltip.irons_spellbooks.spellbook_rarity", new Object[]{Component.translatable("tooltip.irons_spellbooks.spellbook_unique").withStyle(TooltipsUtils.UNIQUE_STYLE)}).withStyle(ChatFormatting.GRAY));
        }
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player != null && ISpellContainer.isSpellContainer(stack)) {
            ISpellContainer spellList = ISpellContainer.get(stack);
            tooltip.add(Component.translatable("tooltip.irons_spellbooks.spellbook_spell_count", new Object[]{spellList.getMaxSpellCount()}).withStyle(ChatFormatting.GRAY));
            List<SpellData> activeSpellSlots = spellList.getActiveSpells();
            if (!activeSpellSlots.isEmpty()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("tooltip.irons_spellbooks.press_to_cast", new Object[]{Component.keybind("key.irons_spellbooks.spellbook_cast")}).withStyle(ChatFormatting.GOLD));
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("tooltip.irons_spellbooks.spellbook_tooltip").withStyle(ChatFormatting.GRAY));
                SpellSelectionManager spellSelectionManager = ClientMagicData.getSpellSelectionManager();

                for(int i = 0; i < activeSpellSlots.size(); ++i) {
                    MutableComponent spellText = TooltipsUtils.getTitleComponent((SpellData)activeSpellSlots.get(i), (LocalPlayer)player).setStyle(Style.EMPTY);
                    if (MinecraftInstanceHelper.getPlayer() != null && Utils.getPlayerSpellbookStack(MinecraftInstanceHelper.getPlayer()) == stack && spellSelectionManager.getCurrentSelection().equipmentSlot.equals(Curios.SPELLBOOK_SLOT) && i == spellSelectionManager.getSelectionIndex()) {
                        List<MutableComponent> shiftMessage = TooltipsUtils.formatActiveSpellTooltip(stack, spellSelectionManager.getSelectedSpellData(), CastSource.SPELLBOOK, (LocalPlayer)player);
                        shiftMessage.remove(0);
                        TooltipsUtils.addShiftTooltip(tooltip, Component.literal("> ").append(spellText).withStyle(ChatFormatting.YELLOW), (List)shiftMessage.stream().map((component) -> Component.literal(" ").append(component)).collect(Collectors.toList()));
                    } else {
                        tooltip.add(Component.literal(" ").append(spellText.withStyle(Style.EMPTY.withColor(8947966))));
                    }
                }
            }
        }
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.isDamaged() ? 1 : this.maxStackSize;
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
        this.initializeSpellContainer(stack);
        return new ToolCapabilityProvider(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return AttributesModifierHook.getHeldAttributeModifiers(tool, slot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && slot.getType() == EquipmentSlot.Type.HAND ? this.getAttributeModifiers((IToolStackView) ToolStack.from(stack), (EquipmentSlot)slot) : ImmutableMultimap.of();
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
    public @NotNull Rarity getRarity(ItemStack stack) {
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
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
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

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                ISpellContainer spellContainer = ISpellContainer.create(this.getMaxSpellSlots(), true, true);
                spellContainer.save(itemStack);
            }

        }
    }

    @Override
    public @NotNull ToolDefinition getToolDefinition() {
        return this.toolDefinition;
    }
}
