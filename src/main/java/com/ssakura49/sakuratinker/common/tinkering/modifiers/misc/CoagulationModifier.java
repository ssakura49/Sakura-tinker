package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.Random;

public class CoagulationModifier extends BaseModifier {
    private static final float CHANCE_PER_LEVEL = 0.30f;
    private static final float MAX_CHANCE = 0.90f;
    private static final Random RANDOM = new Random();

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        OverslimeModifier overSlime = (OverslimeModifier) TinkerModifiers.overslime.get();
        float currentSlime = (float)overSlime.getShield(tool);
        if (currentSlime > 0) {
            float chance = Math.min(modifier.getLevel() * CHANCE_PER_LEVEL, MAX_CHANCE);
            if (RANDOM.nextFloat() < chance) {
                return 0;
            }
        }
        return amount;
    }
}
