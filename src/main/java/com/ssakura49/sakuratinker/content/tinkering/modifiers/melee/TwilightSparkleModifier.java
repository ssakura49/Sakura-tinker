package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.world.registration.TFGenerationSettings;

public class TwilightSparkleModifier extends BaseModifier {
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player != null && !isInTwilightForest(player.level())) {
            return damage + (baseDamage * 0.08f * modifier.getLevel());
        }
        return damage;
    }

    private boolean isInTwilightForest(Level level) {
        return level.dimension().equals(TFGenerationSettings.DIMENSION_KEY);
    }
}
