package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;

public class OmnipotenceModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

//    public OmnipotenceModifier() {
//        MinecraftForge.EVENT_BUS.addListener(this::onLivingDamage);
//    }
//
//    @SubscribeEvent
//    public void onLivingDamage(LivingDamageEvent event) {
//        DamageSource source = event.getSource();
//        LivingEntity target = event.getEntity();
//        if (source.getEntity() instanceof Player player) {
//            ItemStack item = player.getMainHandItem();
//            if (this.getLevel(item) > 0) {
//                target.setHealth(0);
//                target.dropAllDeathLoot(source);
//               EntityUtil.die(target,source);
//            }
//        }
//    }
//    private int getLevel(ItemStack stack) {
//        if (stack.getItem() instanceof IModifiable) {
//            IToolStackView tool = ToolStack.from(stack);
//            return tool.getModifierLevel(this);
//        }
//        return 0;
//    }
}

