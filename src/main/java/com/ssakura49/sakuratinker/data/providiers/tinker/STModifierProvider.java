package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.content.tinkering.modules.EnvironmentalAdaptationModule;
import com.ssakura49.sakuratinker.content.tinkering.modules.MultiCurioAttributeModule;
import com.ssakura49.sakuratinker.data.STModifierId;
import com.ssakura49.sakuratinker.library.tinkering.modules.StatOperation;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.Map;

public class STModifierProvider extends AbstractModifierProvider {

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
                                new ResourceLocation("minecraft:plains"), new EnvironmentalAdaptationModule.BiomeBoost(2.0f),
                                new ResourceLocation("minecraft:desert"), new EnvironmentalAdaptationModule.BiomeBoost(-1.5f)
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
                                        ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft:generic.max_health")),
                                        AttributeModifier.Operation.ADDITION,
                                        10.0
                                )
                        )
                ))
                .build();
    }

    @Override
    public String getName() {
        return "Sakura Tinker Modifier Provider";
    }
}
