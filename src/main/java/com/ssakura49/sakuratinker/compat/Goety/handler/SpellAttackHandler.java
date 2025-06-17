package com.ssakura49.sakuratinker.compat.Goety.handler;

import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class SpellAttackHandler {
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();

        if (source instanceof ModDamageSource damageSource && source.getEntity() instanceof Player player) {
            ItemStack wandStack = WandUtil.findWand(player);
             if (!wandStack.isEmpty()) {
                 ToolStack tool = ToolStack.from(wandStack);

                 if (!tool.isBroken()) {
                     ToolAttackContext context = new ToolAttackContext(
                             player,
                             player,
                             InteractionHand.MAIN_HAND,
                             target,
                             target,
                             false,
                             0,
                             false
                     );

                     float baseDamage = event.getAmount();
                     float modifiedDamage = baseDamage;

                     for (ModifierEntry entry : tool.getModifierList()) {
                         modifiedDamage = entry.getHook(ModifierHooks.MELEE_DAMAGE)
                                 .getMeleeDamage(tool, entry, context, baseDamage, modifiedDamage);
                     }

                     for (ModifierEntry entry : tool.getModifierList()) {
                         entry.getHook(ModifierHooks.MELEE_HIT)
                                 .beforeMeleeHit(tool, entry, context, modifiedDamage, 0, 0);
                     }

                     event.setAmount(modifiedDamage);

                     for (ModifierEntry entry : tool.getModifierList()) {
                         entry.getHook(ModifierHooks.MELEE_HIT)
                                 .afterMeleeHit(tool, entry, context, modifiedDamage);
                     }
                 }
            }
        }
    }
}
