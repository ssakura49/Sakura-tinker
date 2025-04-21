package com.ssakura49.sakuratinker.utils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MixinTemp {
    public static boolean isProcessingDamageSource;
    public static float damageBeforeArmorAbs;

    public static Entity arrowHit ;
    public static float entityHealth ;
    public static boolean hasBeenShot;
    public static boolean leftOwner;
    public static boolean onGround;

    public static class attackUtilTemp{
        public static IToolStackView tool;
        public static LivingEntity attacker;
        public static InteractionHand hand;
        public static Entity target;
        public static EquipmentSlot sourceSlot;
        public static boolean isFullyCharged;
        public static boolean isExtraAttack;
        public static boolean isCritical;
    }
}