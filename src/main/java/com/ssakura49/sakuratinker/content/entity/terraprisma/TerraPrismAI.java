package com.ssakura49.sakuratinker.content.entity.terraprisma;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.ssakura49.sakuratinker.content.entity.terraprisma.behavior.TerraPrismFollowBehavior;
import com.ssakura49.sakuratinker.content.entity.terraprisma.behavior.TerraPrismMeleeAttack;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Set;

public class TerraPrismAI {
    //基础参数
    public static final int FOLLOW_RANGE = 12;
    public static final float ATTACK_RANGE = 5.0f;
    public static final int ATTACK_COOLDOWN = 20;

    //记忆模块类型
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES =
            ImmutableList.<MemoryModuleType<?>>builder()
                    .add(MemoryModuleType.NEAREST_LIVING_ENTITIES)
                    .add(MemoryModuleType.ATTACK_TARGET)
                    .add(MemoryModuleType.ATTACK_COOLING_DOWN)
                    .add(MemoryModuleType.WALK_TARGET)
                    .add(MemoryModuleType.LOOK_TARGET)
                    .add(STMemoryModules.PRISM_INDEX.get())
                    .add(STMemoryModules.TOTAL_PRISMS.get())
                    .add(STMemoryModules.OWNER.get())
                    .build();

    //传感器类型
    protected static final List<SensorType<? extends Sensor<? super TerraPrismEntity1>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
    );

    //初始化AI
    public static Brain<?> makeBrain(TerraPrismEntity1 prism, Dynamic<?> dyn) {
        Brain.Provider<TerraPrismEntity1> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<TerraPrismEntity1> brain = provider.makeBrain(dyn);

        initCoreActivity(brain);
        initFollowActivity(brain);
        initAttackActivity(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE); // 通常默认活动是IDLE而不是FOLLOW
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<TerraPrismEntity1> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim(0.8F),
                new LookAtTargetSink(45, 90)
        ));
    }

    private static void initFollowActivity(Brain<TerraPrismEntity1> brain) {
        brain.addActivity(Activity.IDLE, 5, ImmutableList.of(
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F), // 使用 .create() 方法
                new TerraPrismFollowBehavior()
        ));
    }

    private static void initAttackActivity(Brain<TerraPrismEntity1> brain) {
        brain.addActivityWithConditions(
                Activity.FIGHT,
                ImmutableList.of(
                        Pair.of(0, new TerraPrismMeleeAttack(ATTACK_COOLDOWN)),
                        Pair.of(1, StopAttackingIfTargetInvalid.create()) // 使用 .create() 方法
                ),
                Set.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), // 仅当有攻击目标时触发
                        Pair.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.VALUE_ABSENT)
                )
        );
    }
}
