package com.ssakura49.sakuratinker.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToolUtils {
    public ToolUtils() {
    }

    @Nullable
    public static ToolStack checkTool(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Items.MODIFIABLE) ? ToolStack.from(stack) : null;
    }

    @Nullable
    public static ToolStack getToolInHand(LivingEntity entity) {
        ItemStack offItem = entity.getOffhandItem();
        ItemStack mainItem = entity.getMainHandItem();
        ItemStack stack = mainItem.isEmpty() ? (offItem.isEmpty() ? null : offItem) : mainItem;
        return stack != null ? checkTool(stack) : null;
    }

    @Nullable
    public static ToolStack getHeldTool(LivingEntity entity, EquipmentSlot slot) {
        return Modifier.getHeldTool(entity, slot);
    }

    public static boolean hasModifierInHeldTool(LivingEntity entity, Modifier modifier) {
        ToolStack tool = getToolInHand(entity);
        if (isNotBrokenOrNull(tool)) {
            return tool.getModifierLevel(modifier) != 0;
        } else {
            return false;
        }
    }

    public static int getModifierInHeldTool(LivingEntity entity, Modifier modifier) {
        ToolStack tool = getToolInHand(entity);
        return isNotBrokenOrNull(tool) ? tool.getModifierLevel(modifier) : 0;
    }

    @Nullable
    public static ToolStack getNotBrokenAndCooldownArmor(LivingEntity entity) {
        for(ItemStack stack : entity.getArmorSlots()) {
            ToolStack tool = checkTool(stack);
            if (isNotBrokenOrNull(tool) && ItemUtils.noCooldown(entity, tool)) {
                return tool;
            }
        }

        return null;
    }

    @Nullable
    public static ToolStack getNotBrokenArmor(LivingEntity entity) {
        for(ItemStack stack : entity.getArmorSlots()) {
            ToolStack tool = checkTool(stack);
            if (isNotBrokenOrNull(tool)) {
                return tool;
            }
        }

        return null;
    }

    @Nullable
    public static ToolStack getNotBrokenToolInHand(LivingEntity entity) {
        if (getHeldTool(entity, EquipmentSlot.OFFHAND) != null) {
            return getHeldTool(entity, EquipmentSlot.OFFHAND);
        } else {
            return getHeldTool(entity, EquipmentSlot.MAINHAND) != null ? getHeldTool(entity, EquipmentSlot.MAINHAND) : null;
        }
    }

    public static int getHeadModifierLevel(LivingEntity entity, Modifier modifier) {
        ToolStack toolStack = ToolStack.from(entity.getItemBySlot(EquipmentSlot.HEAD));
        return !toolStack.isBroken() ? toolStack.getModifierLevel(modifier) : 0;
    }

    public static int getChestModifierLevel(LivingEntity entity, Modifier modifier) {
        ToolStack toolStack = ToolStack.from(entity.getItemBySlot(EquipmentSlot.CHEST));
        return !toolStack.isBroken() ? toolStack.getModifierLevel(modifier) : 0;
    }

    public static int getLegsModifierLevel(LivingEntity entity, Modifier modifier) {
        ToolStack toolStack = ToolStack.from(entity.getItemBySlot(EquipmentSlot.LEGS));
        return !toolStack.isBroken() ? toolStack.getModifierLevel(modifier) : 0;
    }

    public static int getFeetModifierLevel(LivingEntity entity, Modifier modifier) {
        ToolStack toolStack = ToolStack.from(entity.getItemBySlot(EquipmentSlot.FEET));
        return !toolStack.isBroken() ? toolStack.getModifierLevel(modifier) : 0;
    }

    public static boolean hasModifierInAllArmor(LivingEntity entity, Modifier modifier) {
        return getHeadModifierLevel(entity, modifier) > 0 && getChestModifierLevel(entity, modifier) > 0 && getLegsModifierLevel(entity, modifier) > 0 && getFeetModifierLevel(entity, modifier) > 0;
    }

    public static int getSingleModifierArmorAllLevel(LivingEntity entity, Modifier modifier) {
        return getHeadModifierLevel(entity, modifier) + getChestModifierLevel(entity, modifier) + getLegsModifierLevel(entity, modifier) + getFeetModifierLevel(entity, modifier);
    }

    public static boolean isNotBrokenOrNull(IToolStackView tool) {
        return tool != null && !tool.isBroken();
    }

    public static boolean hasModifierIn(IToolStackView tool, Modifier modifier) {
        return tool.getModifierLevel(modifier) > 0;
    }

    public static boolean hasModifierIn(IToolStackView tool, ResourceLocation id) {
        return tool.getModifierLevel(new ModifierId(id)) > 0;
    }

    public static boolean hasMetaIn(ToolStack tool, String id) {
        Iterator<MaterialVariant> var2 = tool.getMaterials().getList().iterator();
        if (var2.hasNext()) {
            MaterialVariant material = (MaterialVariant)var2.next();
            return material.get().getIdentifier().toString().equals(id);
        } else {
            return false;
        }
    }

    public static class Curios {
        public Curios() {
        }

        public static List<ItemStack> getStacks(LivingEntity entity) {
            List<ItemStack> list = new ArrayList<>();
            LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
            if (handler.resolve().isEmpty()) {
                return list;
            } else {
                for(ICurioStacksHandler curios : ((ICuriosItemHandler)handler.resolve().get()).getCurios().values()) {
                    for(int i = 0; i < curios.getSlots(); ++i) {
                        ItemStack stack = curios.getStacks().getStackInSlot(i);
                        if (!stack.isEmpty() && stack.is(Items.MODIFIABLE)) {
                            list.add(stack);
                        }
                    }
                }

                return list;
            }
        }
    }
}
