package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.c2h6s.etstlib.content.misc.vibration.VibrationContext;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.VibrationListeningModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

public class DangerSenseModifier extends EtSTBaseModifier implements VibrationListeningModifierHook {
    public static final UUID DS_UUID = UUID.fromString("f9b61a91-4c18-4686-a6ba-421aa5783d8f");
    public static final String KEY_DS = "resonating";
    public static final ResourceLocation KEY = SakuraTinker.location(KEY_DS);
    private static final int BASE_RANGE = 10;
    private static final int RANGE_PER_LEVEL = 10;
    private static final int GLOW_DURATION = 100;
    private static final int BUFF_DURATION = 200;

    @Override
    public @NotNull Component getDisplayName(int level) {
        return super.getDisplayName(level);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.VIBRATION_LISTENING, ModifierHooks.EQUIPMENT_CHANGE);
    }

    @Override
    public void onAcceptorTick(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, int acceptorLevel) {
        if (player.tickCount % 200 == 0) {
            onReceivingVibration(tool, modifier, player, level, slot,
                    new VibrationContext(level, player.blockPosition(),
                            GameEvent.ENTITY_ROAR, null, null, 0, 0, null));
        }
    }

    @Override
    public void onReceivingVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
        int range = listenRange(tool, modifier, player, level, slot, BASE_RANGE);
        Vec3 playerPos = player.position();
        AABB detectionArea = new AABB(
                playerPos.x - range, playerPos.y - range, playerPos.z - range,
                playerPos.x + range, playerPos.y + range, playerPos.z + range
        );
        boolean foundDanger = false;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, detectionArea)) {
            if (entity instanceof Monster || entity.getMobType() == MobType.UNDEAD || entity.getMobType() == MobType.ILLAGER) {
                entity.addEffect(new MobEffectInstance(
                        MobEffects.GLOWING,
                        GLOW_DURATION,
                        0,
                        false,
                        true
                ));
                foundDanger = true;
            }
        }
        if (foundDanger) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    BUFF_DURATION,
                    1, // 速度II
                    false,
                    true
            ));
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST,
                    BUFF_DURATION,
                    1, // 力量II
                    false,
                    true
            ));
        }
    }

    @Override
    public UUID getAcceptorUUID(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot) {
        return DS_UUID;
    }

    @Override
    public int listenRange(IToolStackView tool, ModifierEntry modifier, Player player, Level level, EquipmentSlot slot, int range) {
        return BASE_RANGE + modifier.getLevel() * RANGE_PER_LEVEL;
    }

    @Override
    public boolean canReceiveVibration(IToolStackView tool, ModifierEntry modifier, Player player, ServerLevel level, EquipmentSlot slot, VibrationContext context) {
        if (context.directEntity==player) return false;
        return tool.getPersistentData().getInt(KEY)<=0;
    }
}
