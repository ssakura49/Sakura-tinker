package com.ssakura49.sakuratinker.common.items.items;

import com.ssakura49.sakuratinker.client.component.ItemsMutableComponent;
import com.ssakura49.sakuratinker.client.component.LoreHelper;
import com.ssakura49.sakuratinker.client.component.LoreStyle;
import com.ssakura49.sakuratinker.utils.STUtils;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleDescriptiveItem extends Item {
    public List<ItemsMutableComponent> defaultDesc = new ArrayList<>();
    public List<ItemsMutableComponent> head = new ArrayList<>();
    public List<ItemsMutableComponent> tail = new ArrayList<>();
    public List<Component> toArg = new ArrayList<>();
    private List<ItemsMutableComponent> tempAttributeList = new ArrayList<>();
    public boolean showHeader = false;
    public Style descriptionStyle = Style.EMPTY.withColor(ChatFormatting.GRAY);

    public SimpleDescriptiveItem(Properties properties) {
        super(properties);
    }

    /** 默认只返回描述行，你可以在 override 中加上 shift 检测 */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!head.isEmpty()) {
            tooltip.add(Component.empty());
            tooltip.addAll(head.stream().map(h -> h.build(stack)).toList());
        }

        if (showHeader) {
            tooltip.add(Component.literal(I18n.get(this.getDescriptionId() + ".header")).withStyle(ChatFormatting.GOLD));
        }

        if (enableSimpleDesc()) {
            tooltip.add(getDescription(stack));
        } else {
            tooltip.addAll(defaultDesc.stream().map(c -> c.build(stack)).toList());
        }

        tooltip.addAll(this.getTailDescriptionLines(stack));
    }

    /** 属性描述处理，若有需要也可与 Curios 一样分析内容结构转格式化条目 */
    public List<Component> getAttributesTooltip(ItemStack stack, List<Component> vanillaAttributes) {
        if (tempAttributeList == null || TimeContext.Client.count % 5 == 0) {
            tempAttributeList = STUtils.safelyForEach(vanillaAttributes, component -> {
                if (!component.equals(Component.empty())
                        && component instanceof MutableComponent mutable
                        && mutable.getContents() instanceof TranslatableContents contents) {
                    if (!I18n.exists(contents.getKey())) return null;
                    if (contents.getArgs().length != 2) return null;

                    String key = contents.getKey();
                    if (contents.getArgs()[1] instanceof Component arg) {
                        ItemsMutableComponent.FormatDescFunction func = stack1 -> new Object[]{
                                LoreHelper.codeMode(ChatFormatting.AQUA),
                                Float.parseFloat(contents.getArgs()[0].toString()),
                                LoreHelper.codeMode(ChatFormatting.GOLD),
                                I18n.get(arg.getContents() instanceof TranslatableContents tC ? tC.getKey() : "")
                        };

                        if (key.endsWith(".plus.1") || key.endsWith(".plus.2")) {
                            return ItemsMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendAttributeFormat(1, func);
                        }
                        if (key.endsWith(".take.1") || key.endsWith(".take.2")) {
                            return ItemsMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendNegAttributeFormat(1, func);
                        }
                    }
                }
                return null;
            });
            toArg.clear();
            toArg.addAll(tempAttributeList.stream().map(a -> a.build(stack)).toList());
        }
        return toArg;
    }

    public List<? extends Component> getTailDescriptionLines(ItemStack stack) {
        if (tail.isEmpty()) return List.of();
        return tail.stream().map(c -> c.build(stack)).toList();
    }

    public Component getDescription(ItemStack stack) {
        return Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(this.descriptionStyle);
    }

    public boolean enableSimpleDesc() {
        return true;
    }

    public SimpleDescriptiveItem withHead(ItemsMutableComponent... head) {
        this.head = List.of(head);
        return this;
    }

    public SimpleDescriptiveItem withTail(ItemsMutableComponent... tail) {
        this.tail = List.of(tail);
        return this;
    }

    public SimpleDescriptiveItem defaultDesc(ItemsMutableComponent... defaultDesc) {
        this.defaultDesc = List.of(defaultDesc);
        return this;
    }
}

