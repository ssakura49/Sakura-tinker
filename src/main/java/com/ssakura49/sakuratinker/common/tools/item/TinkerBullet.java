package com.ssakura49.sakuratinker.common.tools.item;

import com.ssakura49.sakuratinker.common.entity.BulletEntity;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileBuild;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableBulletItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class TinkerBullet extends ModifiableBulletItem implements IProjectileBuild {
    public TinkerBullet(Item.Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public BulletEntity createBullet(Level level, ItemStack stack, LivingEntity entity) {
        ToolStack toolStack = ToolStack.from(stack);
        BulletEntity bullet = (BulletEntity)this.createProjectile(stack, level, entity);
        float attack = toolStack.getStats().get(ToolStats.ATTACK_DAMAGE);
        this.addInfoToProjectile(bullet, stack, level, entity);
        bullet.setTool(stack);
        bullet.setDamage(attack);
        return bullet;
    }

    @Override
    public Projectile createProjectile(ItemStack tool, Level level, LivingEntity entity) {
        return new BulletEntity(level, entity);
    }

    @Override
    public void addInfoToProjectile(Projectile projectile, ItemStack tool, Level level, LivingEntity entity) {
        if (projectile instanceof BulletEntity bulletEntity) {
            bulletEntity.setTool(tool);
        }
    }

    private List<Component> getTinkerBulletStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.addDurability();
        }
        builder.add(ToolStats.ATTACK_DAMAGE);
        builder.addAllFreeSlots();
        for(ModifierEntry entry : tool.getModifierList()) {
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }

    @Override
    public List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag flag) {
        return this.getTinkerBulletStats(tool, player, tooltips, key, flag);
    }
}
