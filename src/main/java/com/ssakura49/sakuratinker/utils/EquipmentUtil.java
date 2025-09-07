package com.ssakura49.sakuratinker.utils;

import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class EquipmentUtil {
    public static final List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
    public static final List<EquipmentSlot> HAND = List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
    public static final List<EquipmentSlot> ALL = List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND);
    public static boolean isNullAlmightyAntiStun(LivingEntity entity){
        int level;
        level = ToolUtil.getModifierArmorAllLevel(entity, STModifiers.Null_Almighty.get());
        return level == 4;
    }
    public static boolean isBloodBurnAntiStun(LivingEntity entity) {
        int level;
        level = ToolUtil.getModifierArmorAllLevel(entity, STModifiers.BloodBurn.get());
        return level > 0;
    }
}
