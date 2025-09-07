/**package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.common.entity.terraprisma.TerraPrismEntity;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class TerraPrismaSummonModifier extends BaseModifier{
    private static final String SUMMON_KEY_ID = "key.sakuratinker.summon";

    @Override
    public void onKeyPress(IToolStackView tool, ModifierEntry entry, Player player, String key) {
        if (!SUMMON_KEY_ID.equals(key)) {
            return;
        }
        if (player.level().isClientSide()) {
            return;
        }
        int level = entry.getLevel();
        int maxSummons = level * 2;
        int currentCount = countPlayerPrisms(player);
        if (currentCount >= maxSummons) {
            despawnAllPrisms(player);
            player.displayClientMessage(
                    Component.literal("已清除所有棱镜").withStyle(ChatFormatting.RED),
                    true
            );
            return;
        }
        summonPrism(player, entry, tool, currentCount, maxSummons);
    }
    private void despawnAllPrisms(Player player) {
        List<TerraPrismEntity> prisms = player.level().getEntitiesOfClass(
                TerraPrismEntity.class,
                player.getBoundingBox().inflate(64),
                e -> e.getOwnerUUID() != null && e.getOwnerUUID().equals(player.getUUID())
        );
        for (TerraPrismEntity prism : prisms) {
            prism.discard();
        }
    }

    private int countPlayerPrisms(Player player) {
        return player.level().getEntitiesOfClass(
                TerraPrismEntity.class,
                player.getBoundingBox().inflate(64),
                e -> e.getOwnerUUID() != null && e.getOwnerUUID().equals(player.getUUID())
        ).size();
    }

    private void sendLimitMessage(Player player, int current, int max) {
        player.displayClientMessage(
                Component.literal("召唤数量已达上限: " + current + "/" + max),
                true
        );
    }

    private void summonPrism(Player player, ModifierEntry entry, IToolStackView tool, int currentCount, int maxSummons) {
        try {
            TerraPrismEntity prism = new TerraPrismEntity(player, player.level());
            ToolStack toolStack = (ToolStack) tool;
            float damage = tool.getStats().getInt(ToolStats.ATTACK_DAMAGE);
            prism.setPos(player.getX(), player.getY() + 1, player.getZ());
            prism.setOwner(player);
            prism.setTool(toolStack.createStack());

            prism.updateStats(builder -> builder
                    .maxHealth(builder.getMaxHealth() + 50.0f * entry.getLevel())
                    .armor(builder.getArmor() + 20 * entry.getLevel())
                    .attackDamage(builder.getAttackDamage() + damage * entry.getLevel())
                    .attackCooldown(10)
            );


            if (player.level().addFreshEntity(prism)) {
                player.displayClientMessage(
                        Component.literal("成功召唤棱镜 (剩余 " + (maxSummons - currentCount - 1) + ")")
                                .withStyle(ChatFormatting.GREEN),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("召唤失败!").withStyle(ChatFormatting.RED),
                        true
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getKeyId(IToolStackView tool, ModifierEntry modifier) {
        return SUMMON_KEY_ID;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> tooltip, TooltipKey key, TooltipFlag flag) {
        String keyName = getKeyDisplay(tool, entry);
        tooltip.add(Component.literal("按 [")
                .append(Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .append("] 召唤棱镜 / 清除")
                .withStyle(ChatFormatting.GRAY));
    }

}*/
