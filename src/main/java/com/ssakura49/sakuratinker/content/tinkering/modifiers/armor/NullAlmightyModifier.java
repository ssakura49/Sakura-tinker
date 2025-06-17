package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = "sakuratinker")
public class NullAlmightyModifier extends BaseModifier {

    public static final ModifierId NULL_ALMIGHTY = new ModifierId(new ResourceLocation("sakuratinker", "null_almighty"));

    public static int getNullAlmightyLevel(Player player) {
        int maxLevel = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty()) {
                IToolStackView tool = ToolStack.from(stack);
                ModifierEntry modifier = tool.getModifiers().getEntry(NULL_ALMIGHTY);
                maxLevel = Math.max(maxLevel, modifier.getLevel());
            }
        }
        return maxLevel;
    }
//    @SubscribeEvent
//    public static void onKnockback(LivingKnockBackEvent event) {
//        if (event.getEntity() instanceof Player player) {
//            int level = getNullAlmightyLevel(player);
//            if (level >= 4) {
//                event.setCanceled(true);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onPlayerHurt(LivingHurtEvent event) {
//        LivingEntity entity = event.getEntity();
//        if (entity instanceof Player player) {
//            float damageReduction = 0;
//            for (ItemStack stack : player.getArmorSlots()) {
//                if (stack != null && !stack.isEmpty()) {
//                    IToolStackView tool = ToolStack.from(stack);
//                    ModifierEntry modifier = tool.getModifiers().getEntry(STModifiers.Null_Almighty.getId());
//                    damageReduction += 0.25f * modifier.getLevel();
//                }
//            }
//            float originalDamage = event.getAmount();
//            float reducedDamage = originalDamage * (1 - damageReduction);
//            event.setAmount(reducedDamage);
//        }
//    }
}
