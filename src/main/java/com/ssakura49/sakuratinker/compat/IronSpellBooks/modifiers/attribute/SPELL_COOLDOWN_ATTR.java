package com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.attribute;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.generic.CurioModifier;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;
import java.util.function.BiConsumer;

public class SPELL_COOLDOWN_ATTR extends CurioModifier {
    private static final UUID ID = UUID.fromString("e1d36d67-05d0-42b6-9d3c-37edb54103a1");

    @Override
    public int getPriority() {
        return 500;
    }
    @Override
    public void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(ID, "cooldown_attr", 0.1f*entry.getLevel(), AttributeModifier.Operation.ADDITION));
    }
}
