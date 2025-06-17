package com.ssakura49.sakuratinker.compat.DreadSteel.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ItemUtil;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.mindoth.dreadsteel.registries.DreadsteelEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DreadfulScytheModifier extends BaseModifier {
    private static final Random RANDOM = new Random();
    private static final float MIN_CHARGE_TIME = 0.8f;
    @SuppressWarnings("unchecked")
    private static final List<RegistryObject<EntityType<? extends Entity>>> SCYTHE_PROJECTILES = Arrays.asList(
            (RegistryObject<EntityType<? extends Entity>>)(RegistryObject<?>)DreadsteelEntities.SCYTHE_PROJECTILE_DEFAULT,
            (RegistryObject<EntityType<? extends Entity>>)(RegistryObject<?>)DreadsteelEntities.SCYTHE_PROJECTILE_BLACK,
            (RegistryObject<EntityType<? extends Entity>>)(RegistryObject<?>)DreadsteelEntities.SCYTHE_PROJECTILE_BRONZE,
            (RegistryObject<EntityType<? extends Entity>>)(RegistryObject<?>)DreadsteelEntities.SCYTHE_PROJECTILE_WHITE
    );

    public DreadfulScytheModifier() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (!level.isClientSide && isFullyCharged(tool, player)) {
            spawnRandomScytheProjectile(tool, entry, player, level);
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (!level.isClientSide && isFullyCharged(tool, player)) {
            spawnRandomScytheProjectile(tool, entry, player, level);
        }
    }

    private boolean isFullyCharged(IToolStackView tool, Player player) {
        float chargeTime = 1.0f / tool.getStats().getInt(ToolStats.ATTACK_SPEED);
        float cooldown = player.getAttackStrengthScale(0.5f);
        return cooldown >= MIN_CHARGE_TIME * chargeTime;
    }

    private void spawnRandomScytheProjectile(IToolStackView tool, ModifierEntry entry, Player player, Level level) {
        RegistryObject<EntityType<? extends Entity>> projectileType = getRandomScytheProjectile();
        Vec3 look = player.getLookAngle();
        Vec3 spawnPos = player.getEyePosition().add(look.scale(1.5));
        Entity projectile = projectileType.get().create(level);
        if (projectile != null) {
            projectile.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), player.getXRot());
            projectile.setDeltaMovement(look.scale(1.5));
            if (projectile instanceof AbstractArrow arrow) {
                arrow.setOwner(player);
                arrow.setBaseDamage(tool.getStats().getInt(ToolStats.ATTACK_DAMAGE));
            }
            level.addFreshEntity(projectile);
            ToolDamageUtil.damageAnimated(tool,1,player,InteractionHand.MAIN_HAND);
        }
    }

    private RegistryObject<EntityType<? extends Entity>> getRandomScytheProjectile() {
        return SCYTHE_PROJECTILES.get(RANDOM.nextInt(SCYTHE_PROJECTILES.size()));
    }
}
