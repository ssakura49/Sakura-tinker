package com.ssakura49.sakuratinker.utils.component;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DynamicComponentUtil {
    /**
     * <h4>滚动字类,这类为从左到右变换颜色的</h4>
     * <h6>想要调用请直接用{@link ScrollColorfulText#getColorfulText(String, String, int[], int, int, boolean)}这个静态方法</h6>
     * <h6>其他的均为逻辑处理和转化,无需在意</h6>
     *
     * ScrollColorfulText (滚动变色文字)
     */
    public static class ScrollColorfulText {
        /**
         * <h4>这个是入口方法,
         * <br>和你看到的一样,他仅在客户端上工作.在服务端的supplier会直接返回正常的translate
         * </h4>
         * @param translatableText 需要变色的文本本地化键
         * @param append 文本后缀类,如数值等等
         * @param colors 颜色数组,需要new int[]{}
         * @param step 步数,也是过渡的颜色数,这个数值越大过度越平滑,总长度也会越长
         * @param durationMs 一个颜色变换进程所需要的时间,单位毫秒
         * @param isTranslatable 是否为本地化键文本,如果为false则直接把1号形参转化为只读文本
         * @return 彩色变换字的Component文本
         * <h3>如果你还是不会用就来看个示例</h3><pre>{@code
         *@Override
         *     public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
         *         int[] color=new int[]{0xffea95,0xffaaff,0x55c4ff};
         *         float line=Math.round(tool.getStats().get(OrdinarytinkerStat.KILLTHRESHOLD) * 100f);
         *         tooltip.add(DynamicComponentUtil.ScrollColorfulText.getColorfulText("当前魂戈的斩杀线是",line+"%",color,40,30,false));
         *     }
         */


        public static Component getColorfulText(String translatableText, String append, int[] colors, int step, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildGradientText(translatableText, append, colors, step, durationMs, isTranslatable),
                    () -> () -> Component.translatable(translatableText)
            );
        }
        private static MutableComponent buildGradientText(String textKey, @Nullable String append, int[] colors, int step, int durationMs, boolean isTranslatable) {
            // 基础参数预处理
            String safeAppend = append != null ? append : "";
            String localizedText = isTranslatable
                    ? Language.getInstance().getOrDefault(textKey)
                    : textKey;
            String fullText = localizedText + safeAppend;
            // 生成渐变颜色数组
            int[] gradientColors = generateLinearGradient(colors, step);
            int cycleLength = 2 * (gradientColors.length - 1);
            long timestamp = System.currentTimeMillis();
            // 统一字符处理逻辑
            MutableComponent result = Component.empty();
            for (int i = 0; i < fullText.length(); i++) {
                int progress = (i + (int) (timestamp / durationMs)) % cycleLength;
                int colorIndex = (gradientColors.length - 1) - Math.abs(progress - (gradientColors.length - 1));
                result.append(Component.literal(String.valueOf(fullText.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(gradientColors[colorIndex])))
                );
            }
            return result;
        }

        private static int[] generateLinearGradient(int[] colors, int totalSteps) {
            int[] gradient = new int[totalSteps];
            int segments = colors.length - 1;
            int stepsPerSegment = totalSteps / segments;
            int remainder = totalSteps % segments;
            int startIndex = 0;
            for (int i = 0; i < segments; i++) {
                int currentSteps = stepsPerSegment;
                if (i == segments - 1) {
                    // 将余数分配给最后一段,防止数组不能被整除从而突然几把的黑一下
                    currentSteps += remainder;
                }
                int startColor = colors[i];
                int endColor = colors[i + 1];
                float r1 = (startColor >> 16) & 0xFF;
                float g1 = (startColor >> 8) & 0xFF;
                float b1 = startColor & 0xFF;
                float r2 = (endColor >> 16) & 0xFF;
                float g2 = (endColor >> 8) & 0xFF;
                float b2 = endColor & 0xFF;
                for (int j = 0; j < currentSteps; j++) {
                    float t = j / (float) (currentSteps - 1);
                    int r = (int) (r1 + (r2 - r1) * t);
                    int g = (int) (g1 + (g2 - g1) * t);
                    int b = (int) (b1 + (b2 - b1) * t);
                    int index = startIndex + j;
                    if (index < totalSteps) {
                        gradient[index] = (r << 16) | (g << 8) | b;
                    }
                }
                // 更新下一段起始的实际位置
                startIndex += currentSteps;
            }
            return gradient;
        }
    }

    /**
     * <h4>呼吸灯效果的文字
     * <br>这类为从浅到深色再到浅色变换颜色的
     * <br>这是单颜色呼吸灯效果
     * </h4>
     * <h6>想要调用请直接用{@link DynamicComponentUtil.BreathColorfulText#getColorfulText(String, String, int, int, int, boolean)}这个静态方法</h6>
     * <h6>其他的均为逻辑处理和转化,无需在意</h6>
     */
    public static class BreathColorfulText {
        /**
         * <h4>这个是入口方法,
         * <br>和你看到的一样,他仅在客户端上工作.在服务端的supplier会直接返回正常的translate
         * </h4>
         * @param translatableText 需要变色的文本本地化键
         * @param append 文本后缀类,如数值等等
         * @param color 颜色,是个int值
         * @param totalSteps 总变化步数,越长越平滑
         * @param durationMs 这是整个进程的毫秒数,而非单个变换的毫秒数
         * @param isTranslatable 是否翻译为本地化键的内容
         * @return 呼吸灯效果的Component文本
         * <h3>如果你还是不会用就来看个示例</h3><pre>{@code
         *          @Override
         *     public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
         *         if (player != null) {
         *             int level = modifier.getLevel();
         *             float a = (Math.max(player.getMaxHealth() * 0.2f, 1) * Math.max(player.getArmorValue() * 0.6f, 1) * Math.max(player.totalExperience * 0.001f, 1)) * 0.5f * level;
         *             list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("当前回声点数", String.valueOf(a),0x99b1ff,60,1000,false));
         *             list.add(DynamicComponentUtil.BreathColorfulText.getColorfulText("每点回声所增幅的伤害", level * 0.5f + "攻击力",0x99b1ff,60,1000,false));
         *         }
         *     }
         */
        public static Component getColorfulText(String translatableText, @Nullable String append, int color, int totalSteps, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildBreathText(translatableText, append, color, totalSteps, durationMs,isTranslatable),
                    () -> () -> Component.translatable(translatableText)
            );
        }

        private static MutableComponent buildBreathText(String textKey, @Nullable String append, int color, int totalSteps, int durationMs, boolean isTranslatable) {
            // 预处理文本
            String fullText = getLocalizedText(textKey, isTranslatable) + Optional.ofNullable(append).orElse("");
            // 生成呼吸周期颜色数组
            int[] breathColors = generateRGBBreathWave(color, totalSteps);
            // 计算当前颜色相位
            long cyclePosition = System.currentTimeMillis() % durationMs;
            int colorIndex = (int) (cyclePosition * totalSteps / durationMs) % totalSteps;
            // 构建组件
            return buildColoredComponents(fullText, breathColors[colorIndex]);
        }

        private static String getLocalizedText(String key, boolean translatable) {
            return translatable ? Language.getInstance().getOrDefault(key) : key;
        }

        private static MutableComponent buildColoredComponents(String text, int color) {
            MutableComponent component = Component.empty();
            //保证去除alpha通道
            TextColor textColor = TextColor.fromRgb(color & 0xFFFFFF);
            for (char c : text.toCharArray()) {
                component.append(Component.literal(String.valueOf(c)).setStyle(Style.EMPTY.withColor(textColor)));
            }
            return component;
        }

        private static int[] generateRGBBreathWave(int baseColor, int totalSteps) {
            // 提取RGB分量
            int r = (baseColor >> 16) & 0xFF;
            int g = (baseColor >> 8) & 0xFF;
            int b = baseColor & 0xFF;
            int[] wave = new int[totalSteps];
            for (int i = 0; i < totalSteps; i++) {
                // 使用正弦波控制亮度 (0.2 ~ 1.0)
                float ratio = 0.8f * (float) Math.abs(Math.sin(Math.PI * i / totalSteps)) + 0.2f;
                // 应用亮度系数并限制范围
                int dr = Mth.clamp((int) (r * ratio), 0, 255);
                int dg = Mth.clamp((int) (g * ratio), 0, 255);
                int db = Mth.clamp((int) (b * ratio), 0, 255);
                wave[i] = (dr << 16) | (dg << 8) | db;
            }
            return wave;
        }
    }

    /**
     * <h4>波动颜色文本</h4>
     * <p>这个类实现的是波动式的颜色变换文本（颜色像波浪一样在文字中流动）。</p>
     * <p>适用于展示状态变化、魔法效果、灵动感提示等。</p>
     * <p>调用方法：{@link WaveColorText#getColorfulText(String, String, int, int, int, boolean)}</p>
     */
    public static class WaveColorText {
        /**
         * 主入口方法。
         * <br>仅客户端启用波动效果；在服务端则仅返回普通文本。
         * @param translatableText 文本的本地化键或直接显示内容
         * @param append 可选的后缀（如“: 12%”）
         * @param baseColor 起始颜色（基础 RGB 值）
         * @param stepCount 波动步数，决定波形精度
         * @param durationMs 一个波动循环周期的时间（毫秒）
         * @param isTranslatable 是否将文本视作本地化键
         * @return 构造后的彩色文本组件
         */
        public static Component getColorfulText(String translatableText, @Nullable String append, int baseColor, int stepCount, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildWaveText(translatableText, append, baseColor, stepCount, durationMs, isTranslatable),
                    () -> () -> Component.translatable(translatableText)
            );
        }

        private static MutableComponent buildWaveText(String textKey, @Nullable String append, int baseColor, int stepCount, int durationMs, boolean isTranslatable) {
            String safeAppend = append != null ? append : "";
            String localizedText = isTranslatable ? Language.getInstance().getOrDefault(textKey) : textKey;
            String fullText = localizedText + safeAppend;

            int[] wave = generateWaveColorArray(baseColor, stepCount);
            long time = System.currentTimeMillis();
            int phase = (int) ((time % durationMs) * stepCount / durationMs);

            MutableComponent result = Component.empty();
            for (int i = 0; i < fullText.length(); i++) {
                int index = (i + phase) % stepCount;
                int color = wave[index];
                result.append(Component.literal(String.valueOf(fullText.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            }
            return result;
        }

        /**
         * 根据基础颜色生成一个颜色波动数组。
         * @param baseColor 原始颜色（RGB）
         * @param steps 波动步数
         * @return 渐变颜色数组
         */
        private static int[] generateWaveColorArray(int baseColor, int steps) {
            int[] wave = new int[steps];
            int r = (baseColor >> 16) & 0xFF;
            int g = (baseColor >> 8) & 0xFF;
            int b = baseColor & 0xFF;
            for (int i = 0; i < steps; i++) {
                float ratio = 0.4f + 0.6f * (float) Math.sin(Math.PI * i / steps);
                int cr = Mth.clamp((int) (r * ratio), 0, 255);
                int cg = Mth.clamp((int) (g * ratio), 0, 255);
                int cb = Mth.clamp((int) (b * ratio), 0, 255);
                wave[i] = (cr << 16) | (cg << 8) | cb;
            }
            return wave;
        }
    }

    /**
     * <h4>火焰渐变文本</h4>
     * <p>该类实现模拟火焰颜色流动的文本效果（红橙黄渐变），适用于火焰类技能、警告、高热状态等视觉强化。</p>
     * <p>调用方法：{@link FireGradientText#getColorfulText(String, String, int, int, int, boolean)}</p>
     */
    public static class FireGradientText {
        /**
         * 主入口方法。
         * <br>仅客户端启用动态火焰渐变效果；在服务端则仅返回普通文本。
         * @param translatableText 文本的本地化键或直接显示内容
         * @param append 可选的后缀（如“: 12%”）
         * @param baseColor 起始颜色（将会过渡到黄橙红）
         * @param stepCount 渐变颜色数量，越大越平滑
         * @param durationMs 一个颜色循环周期的时间（毫秒）
         * @param isTranslatable 是否将文本视作本地化键
         * @return 构造后的彩色文本组件
         */
        public static Component getColorfulText(String translatableText, @Nullable String append, int baseColor, int stepCount, int durationMs, boolean isTranslatable) {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> buildFireText(translatableText, append, baseColor, stepCount, durationMs, isTranslatable),
                    () -> () -> Component.translatable(translatableText)
            );
        }

        private static MutableComponent buildFireText(String textKey, @Nullable String append, int baseColor, int stepCount, int durationMs, boolean isTranslatable) {
            String safeAppend = append != null ? append : "";
            String localizedText = isTranslatable ? Language.getInstance().getOrDefault(textKey) : textKey;
            String fullText = localizedText + safeAppend;

            int[] fireColors = generateFireGradient(stepCount);
            long time = System.currentTimeMillis();
            int phase = (int) ((time % durationMs) * stepCount / durationMs);

            MutableComponent component = Component.empty();
            for (int i = 0; i < fullText.length(); i++) {
                int index = (phase + i) % fireColors.length;
                int color = fireColors[index];
                component.append(Component.literal(String.valueOf(fullText.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            }
            return component;
        }

        /**
         * 生成火焰渐变颜色数组（红 → 橙 → 黄）。
         * @param stepCount 总步数
         * @return 渐变色数组
         */
        private static int[] generateFireGradient(int stepCount) {
            int[] gradient = new int[stepCount];
            int[] fireBase = new int[]{0xFF3300, 0xFF6600, 0xFF9900, 0xFFFF00};
            int seg = fireBase.length - 1;
            int per = stepCount / seg;

            for (int i = 0; i < seg; i++) {
                int start = fireBase[i];
                int end = fireBase[i + 1];

                float r1 = (start >> 16) & 0xFF;
                float g1 = (start >> 8) & 0xFF;
                float b1 = 0;
                float r2 = (end >> 16) & 0xFF;
                float g2 = (end >> 8) & 0xFF;
                float b2 = 0;

                for (int j = 0; j < per; j++) {
                    float t = j / (float) per;
                    int r = (int) (r1 + (r2 - r1) * t);
                    int g = (int) (g1 + (g2 - g1) * t);
                    int b = (int) (b1 + (b2 - b1) * t);
                    gradient[i * per + j] = (r << 16) | (g << 8) | b;
                }
            }
            return gradient;
        }
    }
}
