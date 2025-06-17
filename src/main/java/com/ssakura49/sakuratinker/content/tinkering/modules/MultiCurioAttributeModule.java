package com.ssakura49.sakuratinker.content.tinkering.modules;

import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.primitive.ResourceLocationLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

public record MultiCurioAttributeModule(ResourceLocation name, List<AttributeEntry> attributes) implements ModifierModule, CurioBuilderHook {
    public record AttributeEntry(Attribute attribute, AttributeModifier.Operation operation, double value) {
        public static final RecordLoadable<AttributeEntry> LOADER = RecordLoadable.create(
                Loadables.ATTRIBUTE.requiredField("attribute", AttributeEntry::attribute),
                TinkerLoadables.OPERATION.requiredField("operation", AttributeEntry::operation),
                DoubleLoadable.ANY.requiredField("value", AttributeEntry::value),
                AttributeEntry::new
        );
    }

    public static final RecordLoadable<MultiCurioAttributeModule> LOADER = RecordLoadable.create(
            ResourceLocationLoadable.DEFAULT.requiredField("name", MultiCurioAttributeModule::name),
            AttributeEntry.LOADER.list(0).requiredField("attributes", MultiCurioAttributeModule::attributes),
            MultiCurioAttributeModule::new
    );

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(STHooks.CURIO_BUILDER);
    }

    public static UUID getUUIDFromString(String str) {
        int hash = str.hashCode();
        Random r = new Random((long)hash);
        long l0 = r.nextLong();
        long l1 = r.nextLong();
        return new UUID(l0, l1);
    }

    @Override
    public void modifyCurioAttribute(IToolStackView curio, SlotContext context, UUID uuid, int level, BiConsumer<Attribute, AttributeModifier> consumer ){
        UUID baseUuid = getUUIDFromString(this.name.toString());
        for (AttributeEntry entry : attributes) {
            UUID attributeUuid = new UUID(
                    baseUuid.getMostSignificantBits() ^ entry.attribute().getDescriptionId().hashCode(),
                    baseUuid.getLeastSignificantBits()
            );
            consumer.accept(
                    entry.attribute(),
                    new AttributeModifier(
                            attributeUuid,
                            this.name.toString(),
                            entry.value() * level,
                            entry.operation()
                    )
            );
        }
    }
}
