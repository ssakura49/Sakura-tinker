package com.ssakura49.sakuratinker.common.items.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker.client.component.CuriosMutableComponent;
import com.ssakura49.sakuratinker.client.component.LoreHelper;
import com.ssakura49.sakuratinker.client.component.LoreStyle;
import com.ssakura49.sakuratinker.coremod.SakuraTinkerCore;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableCurioItem;
import com.ssakura49.sakuratinker.utils.STUtils;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class SimpleDescriptiveModifiableCurio extends ModifiableCurioItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    final @NotNull String slotIdentifier;
    public List<Component> tooltips = new ArrayList<>();
    Style descriptionStyle;
    boolean showHeader;
    List<CuriosMutableComponent> defaultDesc = new ArrayList<>();
    List<CuriosMutableComponent> head = new ArrayList<>();
    List<CuriosMutableComponent> tail = new ArrayList<>();
    List<Component> toArg = new ArrayList<>();
    private List<CuriosMutableComponent> tempAttributeList = new ArrayList<>();
    public SimpleDescriptiveModifiableCurio(Item.Properties properties, ToolDefinition toolDefinition, String slotIdentifier, Multimap<Attribute, AttributeModifier> defaultModifiers) {
        super(properties , toolDefinition);
        this.slotIdentifier = Objects.requireNonNull(slotIdentifier);
        this.showHeader = true;
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.YELLOW);
        this.defaultModifiers = defaultModifiers;
    }
    public SimpleDescriptiveModifiableCurio(Item.Properties properties, ToolDefinition toolDefinition, String slotIdentifier, Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers) {
        this(properties,toolDefinition, slotIdentifier, defaultModifiers.get());
    }
    public SimpleDescriptiveModifiableCurio(Item.Properties properties, ToolDefinition toolDefinition, String slotIdentifier) {
        this(properties,toolDefinition, slotIdentifier, ImmutableMultimap::of);
    }
    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {

        MutableComponent title = Component.translatable("curios.modifiers." + this.slotIdentifier).withStyle(ChatFormatting.GOLD);
        List<? extends Component> headList = this.getHeadDescriptionLines(stack);
        if (!headList.isEmpty()) {
            tooltips.add(Component.empty());
            tooltips.addAll(headList);
        }
        if (this.showHeader) {
            tooltips.add(Component.empty());
            tooltips.add(title);
        }
        List<? extends Component> descriptionLines = this.getDescriptionLines(stack);
        if (!descriptionLines.isEmpty()) {
            tooltips.addAll(descriptionLines);
        }

        return super.getSlotsTooltip(tooltips, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    static ChatFormatting textColorToCF(TextColor color) {
        return ChatFormatting.getByName(color.serialize());
    }
    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        try {

            if (tempAttributeList == null || TimeContext.Client.count % 5 == 0) {
                tempAttributeList = STUtils.safelyForEach(tooltips, (component) -> {
                    if (!component.equals(Component.empty())
                            && component instanceof MutableComponent mutableComponent
                            && mutableComponent.getContents() instanceof TranslatableContents contents) {
                        if (!I18n.exists(contents.getKey())) return null;
                        if (contents.getArgs().length != 2)
                            return null;
                        String key = contents.getKey();
                        if (contents.getArgs()[1] instanceof Component cArg) {
                            CuriosMutableComponent.FormatDescFunction descFunction = stack1 -> new Object[]{
                                    LoreHelper.codeMode(ChatFormatting.LIGHT_PURPLE).toString(),
                                    Float.valueOf(contents.getArgs()[0].toString()),
                                    cArg instanceof MutableComponent mcArg && mcArg.getStyle().getColor() != null && !mcArg.getStyle().getColor().serialize().equals(ChatFormatting.WHITE.getSerializedName()) ? LoreHelper.codeMode(textColorToCF(mcArg.getStyle().getColor())).toString() : LoreHelper.codeMode(ChatFormatting.GOLD).toString(),
                                    I18n.get(cArg.getContents() instanceof TranslatableContents tC ? tC.getKey() : "")
                            };
                            if (key.endsWith(".plus.1") || key.endsWith(".plus.2")) {
                                return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendAttributeFormat(
                                        1,
                                        descFunction
                                );
                            }
                            if (key.endsWith(".plus.0")) {
                                return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat(
                                        "%s+%s %s%s",
                                        descFunction
                                );
                            }
                            if (key.endsWith(".take.1") || key.endsWith(".take.2")) {
                                return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendNegAttributeFormat(
                                        1,
                                        descFunction
                                );
                            }
                            if (key.endsWith(".take.0")) {
                                return CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendFormat(
                                        "%s-%s %s%s",
                                        descFunction
                                );
                            }
                        }
                    }
                    return null;
                });
                if (toArg == null) toArg = new ArrayList<>();
                else toArg.clear();
                toArg.addAll(tempAttributeList.stream().map(cmc -> cmc.build(stack)).toList()) ;
                List<MutableComponent> tailList = this.getTailDescriptionLines(stack);
                if (!tailList.isEmpty()) {
                    toArg.add(Component.empty());
                    toArg.addAll(tailList);
                }
            }
            return super.getAttributesTooltip(  toArg, stack);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throwable.printStackTrace(SakuraTinkerCore.stream);
        }

        return tooltips;
    }
    public List<? extends Component> getDescriptionLines(ItemStack stack) {
        if (enableSimpleDesc())
            return List.of(this.getDescription(stack));
        else return defaultDesc.stream().map(cmc -> cmc.build(stack)).toList();
    }
    public List<MutableComponent> getHeadDescriptionLines(ItemStack stack) {
        if (head.isEmpty()) return List.of();
        return head.stream().map(cmc -> cmc.build(stack)).toList();
    }
    public List<MutableComponent> getTailDescriptionLines(ItemStack stack) {
        if (tail.isEmpty())
            return List.of();
        else
            return tail.stream().map(cmc -> cmc.build(stack)).toList();

    }
    public Component getDescription(ItemStack stack) {
        return Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(this.descriptionStyle);
    }
    public boolean enableSimpleDesc() {
        return true;
    }
    protected Item withHead(List<CuriosMutableComponent> head) {
        this.head = head;
        return this;
    }
    protected Item withTail(List<CuriosMutableComponent> tail) {
        this.tail = tail;
        return this;
    }
    protected Item defaultDesc(List<CuriosMutableComponent> defaultDesc) {
        this.defaultDesc = defaultDesc;
        return this;
    }
    protected SimpleDescriptiveModifiableCurio withHead(CuriosMutableComponent... head) {
        this.head = List.of(head);
        return this;
    }
    protected SimpleDescriptiveModifiableCurio withTail(CuriosMutableComponent... tail) {
        this.tail = List.of(tail);
        return this;
    }
    protected SimpleDescriptiveModifiableCurio defaultDesc(CuriosMutableComponent... defaultDesc) {
        this.defaultDesc = List.of(defaultDesc);
        return this;
    }
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotIdentifier.equals("curio")) {
            if (slotContext.identifier().equals("hands")) {
                return defaultModifiers;
            }
        }
        return (slotIdentifier.equals(slotContext.identifier())) ? defaultModifiers : ImmutableMultimap.of();
    }
}
