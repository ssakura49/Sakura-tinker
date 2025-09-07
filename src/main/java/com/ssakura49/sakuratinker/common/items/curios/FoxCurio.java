package com.ssakura49.sakuratinker.common.items.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.sakuratinker.client.component.CuriosMutableComponent;
import com.ssakura49.sakuratinker.client.component.LoreHelper;
import com.ssakura49.sakuratinker.client.component.LoreStyle;
import com.ssakura49.sakuratinker.client.component.STFont;
import com.ssakura49.sakuratinker.client.component.ChatFormattingContext;
import com.ssakura49.sakuratinker.register.STAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class FoxCurio extends SimpleDescriptiveCurio {
    public FoxCurio(Properties properties, String slotIdentifier, Multimap<Attribute, AttributeModifier> defaultModifiers) {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC), "fox_mask" , () ->{
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder
                    .put(STAttributes.getRealitySuppression(), new AttributeModifier(UUID.fromString("128a0077-2aa0-4d7f-883d-14bb867e0fb2"), "Curios modifier", 4.0F, AttributeModifier.Operation.ADDITION))
                    .put(STAttributes.getRealitySuppressionResistance(), new AttributeModifier(UUID.fromString("bf4f56a1-3f19-4a75-8faa-fae0e88f555e"), "Curios modifier", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE))
                    .put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("4c5f72a1-73aa-4ba2-80b0-aa6aada17fc1"), "Curios modifier", 20, AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
        this.defaultDesc(
                CuriosMutableComponent.create(LoreStyle.ATTRIBUTE_PREFIX).appendAttributeFormat(1, stack ->
                        new Object[]{
                                LoreHelper.codeMode(ChatFormatting.GOLD),
                                95F,
                                LoreHelper.codeMode(ChatFormattingContext.SAKURA_ORIGIN()),
                                I18n.get("item.sakuratinker.fox_mask.desc")
                        }
                )
        );
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @Nullable Font getFont(ItemStack stack, FontContext context) {
                return STFont.INSTANCE;
            }
        });
        super.initializeClient(consumer);
    }
}
