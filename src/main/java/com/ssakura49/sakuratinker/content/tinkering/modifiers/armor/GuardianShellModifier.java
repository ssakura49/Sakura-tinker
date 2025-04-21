package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Objects;

public class GuardianShellModifier extends BaseModifier {
    public GuardianShellModifier() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private static final String COOLDOWN_TAG = "guardian_shell_cooldown";
    private static final String BROKEN_TAG = "guardian_shell_broken";
    private static final int COOLDOWN = 200 * 20;


    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player) {
            ModDataNBT data = tool.getPersistentData();
            if (data.getInt(Objects.requireNonNull(ResourceLocation.tryParse(COOLDOWN_TAG))) <= 0 && !data.getBoolean(Objects.requireNonNull(ResourceLocation.tryParse(BROKEN_TAG)))) {
                if (player.getHealth() <= player.getMaxHealth()/2 ) {
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100 * 20, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 1));
                    data.putInt(Objects.requireNonNull(ResourceLocation.tryParse(COOLDOWN_TAG)), COOLDOWN);
                    data.putBoolean(Objects.requireNonNull(ResourceLocation.tryParse(BROKEN_TAG)), true);
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        ModDataNBT data = tool.getPersistentData();
        int cooldown = data.getInt(Objects.requireNonNull(ResourceLocation.tryParse(COOLDOWN_TAG)));
        if (cooldown > 0) {
            data.putInt(Objects.requireNonNull(ResourceLocation.tryParse(COOLDOWN_TAG)), cooldown - 1);
        }
    }
}
