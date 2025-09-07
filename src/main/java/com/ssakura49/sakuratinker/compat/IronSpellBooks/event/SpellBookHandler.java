package com.ssakura49.sakuratinker.compat.IronSpellBooks.event;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableSpellBookItem;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SpellBookHandler {
    public SpellBookHandler(){
    }

    public static void onSpellDamage(SpellDamageEvent event){
        if(SafeClassUtil.ISSLoaded){
            if(event.getSpellDamageSource().getEntity() instanceof Player player){
                LivingEntity livingTarget = event.getEntity();
                Entity target = event.getEntity();
                ItemStack itemStack = Utils.getPlayerSpellbookStack(player);
                if(itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ModifiableSpellBookItem){
                    ToolStack toolStack = ToolStack.from(itemStack);
                    if(toolStack.getModifierLevel(ISSCompat.ArcaneTinkering.get()) > 0){
                        ToolAttackContext context = new ToolAttackContext(player, player, InteractionHand.MAIN_HAND, target, livingTarget, false, 0, false);
                        float originalDamage = event.getAmount();
                        float spellPower = toolStack.getStats().get(STToolStats.SPELL_POWER);
                        float attackDamage = toolStack.getStats().get(ToolStats.ATTACK_DAMAGE);
                        float baseSpellDamage = toolStack.getStats().get(STToolStats.BASE_SPELL_DAMAGE);
                        float actualDamage;
                        if (STConfig.Common.ENABLE_TINKER_SPELL_BOOK_ATK_BONUS.get()){
                            actualDamage = (float) (Math.sqrt(originalDamage*originalDamage + attackDamage*attackDamage + baseSpellDamage*baseSpellDamage) * (spellPower + 1));
                        } else {
                            actualDamage = (float) (Math.sqrt(originalDamage*originalDamage + baseSpellDamage*baseSpellDamage) * (spellPower + 1));
                        }
                        float damage = actualDamage;
                        for(ModifierEntry entry : toolStack.getModifierList()){
                            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(toolStack, entry, context, actualDamage, damage);
                        }
                        if (damage <= 0) {
                            event.setCanceled(true);
                            return;
                        }
                        event.setAmount(damage);
                        for(ModifierEntry entry : toolStack.getModifierList()){
                            entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(toolStack, entry, context, damage, 0, 0);
                        }
                        for(ModifierEntry entry : toolStack.getModifierList()){
                            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(toolStack, entry, context, damage);
                        }
                    }
                }
            }
        }
    }

    public static void onCastSpell(SpellOnCastEvent event){
        ItemStack bookStack = Utils.getPlayerSpellbookStack(event.getEntity());
        if(bookStack != null && !bookStack.isEmpty() && bookStack.getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(bookStack);
            float reduce = toolStack.getStats().get(STToolStats.SPELL_REDUCE);
            if (reduce > 0) {
                int originalMana = event.getManaCost();
                int reducedMana = Math.max(0, Math.round(originalMana * (1.0f - reduce)));
                event.setManaCost(reducedMana);
            }
        }
    }
}
