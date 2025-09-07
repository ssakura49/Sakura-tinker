package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.ssakura49.sakuratinker.common.tinkering.modules.EnvironmentalAdaptationModule;
import com.ssakura49.sakuratinker.common.tinkering.modules.MultiCurioAttributeModule;
import com.ssakura49.sakuratinker.data.generator.STModifierId;
import com.ssakura49.sakuratinker.library.tinkering.modules.StatOperation;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.item.ItemPredicate;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.LootingModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.Map;

public class STModifierProvider extends AbstractModifierProvider implements IConditionBuilder {

    public STModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        this.buildModifier(STModifierId.LORD_OF_EARTH)
                .tooltipDisplay(BasicModifier.TooltipDisplay.ALWAYS)
                .levelDisplay(ModifierLevelDisplay.DEFAULT)
                .addModule(new EnvironmentalAdaptationModule(
                        ToolStats.ATTACK_DAMAGE,
                        Map.of(
                                ResourceLocation.parse("minecraft:plains"), new EnvironmentalAdaptationModule.BiomeBoost(2.0f),
                                ResourceLocation.parse("minecraft:desert"), new EnvironmentalAdaptationModule.BiomeBoost(-1.5f)
                        ),
                        StatOperation.ADDITION,
                        0.5f,
                        true
                ))
                .build();
        this.buildModifier(STModifierId.CURIO_ATTR)
                .tooltipDisplay(BasicModifier.TooltipDisplay.ALWAYS)
                .levelDisplay(ModifierLevelDisplay.DEFAULT)
                .addModule(new MultiCurioAttributeModule(
                        STModifierId.CURIO_ATTR,
                        List.of(
                                new MultiCurioAttributeModule.AttributeEntry(
                                        ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse("minecraft:generic.max_health")),
                                        AttributeModifier.Operation.ADDITION,
                                        10.0
                                )
                        )
                ))
                .build();
//        IJsonPredicate<Item> harvest = ItemPredicate.tag(TinkerTags.Items.HARVEST);
//        LootingModule WEAPON_LOOTING = LootingModule.builder()
//                .toolItem(ItemPredicate.or(ItemPredicate.set(Items.AIR), ItemPredicate.tag(TinkerTags.Items.MELEE)))
//                .level(10)
//                .weapon();
//        EnchantmentModule CONSTANT_FORTUNE = EnchantmentModule.builder(Enchantments.BLOCK_FORTUNE)
//                .toolItem(harvest)
//                .level(10)
//                .mainHandHarvest(ResourceLocation.parse("sakuratinker:fortune_flag"));
//        buildModifier(STModifiers.SuperLuck.getId())
//                .addModules(WEAPON_LOOTING, CONSTANT_FORTUNE)
//                .levelDisplay(ModifierLevelDisplay.NO_LEVELS);

    }

    @Override
    public String getName() {
        return "Sakura Tinker Modifier Provider";
    }
}
