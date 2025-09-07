package com.ssakura49.sakuratinker.client.component;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemsMutableComponent {
    public static final ItemsMutableComponent EMPTY = ItemsMutableComponent.create();

    final MutableComponent base;
    public LoreStyle style = LoreStyle.NONE;
    List<Object> parts = new ArrayList<>();

    ItemsMutableComponent(MutableComponent base) {
        this.base = base;
    }

    public static ItemsMutableComponent create() {
        return new ItemsMutableComponent(Component.literal("")).loreStyle(LoreStyle.NONE);
    }

    public static ItemsMutableComponent create(LoreStyle style) {
        return new ItemsMutableComponent(Component.literal("")).loreStyle(style);
    }

    public static ItemsMutableComponent create(MutableComponent base, LoreStyle style) {
        return new ItemsMutableComponent(base).loreStyle(style);
    }

    private ItemsMutableComponent loreStyle(LoreStyle style) {
        this.style = style;
        return this;
    }

    public ItemsMutableComponent appendFormat(String format, FormatDescFunction args, boolean onlyClient) {
        parts.add(FormatWrapper.wrap(FormatPair.of(format, args)).onlyClient(onlyClient));
        return this;
    }

    public ItemsMutableComponent appendAttributeFormat(int p, FormatDescFunction args, boolean onlyClient) {
        parts.add(FormatWrapper.wrap(FormatPair.of("%s+%." + p + "f%% %s%s", args)).onlyClient(onlyClient));
        return this;
    }

    public ItemsMutableComponent appendTranslation(String key, FormatDescFunction args, boolean onlyClient) {
        parts.add(FormatWrapper.wrap(TranslationFormatPair.of(key, args)).onlyClient(onlyClient));
        return this;
    }

    public ItemsMutableComponent appendComponent(Component component) {
        parts.add(component);
        return this;
    }

    // 默认非客户端限制的快捷重载
    public ItemsMutableComponent appendFormat(String format, FormatDescFunction args) {
        return appendFormat(format, args, false);
    }

    public ItemsMutableComponent appendAttributeFormat(int p, FormatDescFunction args) {
        return appendAttributeFormat(p, args, false);
    }

    public ItemsMutableComponent appendTranslation(String key, FormatDescFunction args) {
        return appendTranslation(key, args, false);
    }

    public ItemsMutableComponent appendTranslation(String key) {
        return appendTranslation(key, null, false);
    }

    public ItemsMutableComponent appendNegAttributeFormat(int p, FormatDescFunction args) {
        return this.appendNegAttributeFormat(p, args, false);
    }

    public ItemsMutableComponent appendNegAttributeFormat(int p, FormatDescFunction args, boolean onlyClient) {
        parts.add(FormatWrapper.wrap(FormatPair.of("%s-%." + p + "f%% %s%s", args)).onlyClient(onlyClient));
        return this;
    }

    // 构建最终 tooltip 展示的 Component
    public MutableComponent build(ItemStack stack) {
        MutableComponent component = this.base.copy();
        component = this.style.getDelegate().call(component);

        Dist dist = FMLEnvironment.dist;
        for (Object o : parts) {
            if (o instanceof FormatWrapper wrapper) {
                if (!wrapper.canUse(dist)) {
                    component.append(Component.literal("[NULL]Only in dist:" + dist));
                    continue;
                }

                if (wrapper.pair instanceof FormatPair pair) {
                    component.append(Component.literal(String.format(pair.left(), pair.right().apply(stack))));
                } else if (wrapper.pair instanceof TranslationFormatPair tpair) {
                    if (tpair.right() != null) {
                        component.append(Component.translatable(tpair.left(), tpair.right().apply(stack)));
                    } else {
                        component.append(Component.translatable(tpair.left()));
                    }
                }
            } else if (o instanceof Component c) {
                component.append(c);
            }
        }

        return component;
    }

    // 用于打包格式字符串 + 参数的包装器
    public static class FormatWrapper {
        private final Pair<String, FormatDescFunction> pair;
        public boolean onlyClient = false;

        private FormatWrapper(Pair<String, FormatDescFunction> pair) {
            this.pair = pair;
        }

        public static FormatWrapper wrap(Pair<String, FormatDescFunction> pair) {
            return new FormatWrapper(pair);
        }

        public FormatWrapper onlyClient(boolean flag) {
            this.onlyClient = flag;
            return this;
        }

        public boolean canUse(Dist dist) {
            return !onlyClient || dist != Dist.DEDICATED_SERVER;
        }
    }

    public interface FormatDescFunction extends Function<ItemStack, Object[]> {}

    public interface FormatPair extends Pair<String, FormatDescFunction> {
        static FormatPair of(String format, FormatDescFunction function) {
            return new FormatPair() {
                @Override public String left() { return format; }
                @Override public FormatDescFunction right() { return function; }
            };
        }
    }

    public interface TranslationFormatPair extends Pair<String, FormatDescFunction> {
        static TranslationFormatPair of(String key, FormatDescFunction function) {
            return new TranslationFormatPair() {
                @Override public String left() { return key; }
                @Override public FormatDescFunction right() { return function; }
            };
        }
    }
}
