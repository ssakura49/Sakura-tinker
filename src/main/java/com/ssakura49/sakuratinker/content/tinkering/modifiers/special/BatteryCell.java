package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.ssakura49.sakuratinker.content.tools.capability.EnergyCapability;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.util.List;

public class BatteryCell extends BaseModifier implements VolatileDataModifierHook, ValidateModifierHook, ModifierRemovalHook, CustomBarDisplayModifierHook, TooltipModifierHook {
    public BatteryCell() {
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,
                ModifierHooks.VALIDATE,
                ModifierHooks.TOOLTIP,
                ModifierHooks.VOLATILE_DATA,
                ModifierHooks.REMOVE,
                EtSTLibHooks.CUSTOM_BAR);
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        int max = tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY);
        if (tool.getPersistentData().getInt(EnergyCapability.STORED_ENERGY) > max) {
            tool.getPersistentData().putInt(EnergyCapability.STORED_ENERGY, max);
        }
        return null;
    }

    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY) == 0) {
            tool.getPersistentData().remove(EnergyCapability.STORED_ENERGY);
        }

        return null;
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        int currentMaxEnergy = volatileData.getInt(EnergyCapability.MAX_ENERGY);
        int addedCapacity = this.getCapacity(context, modifier, volatileData) * modifier.getLevel();
        volatileData.putInt(EnergyCapability.MAX_ENERGY, currentMaxEnergy + addedCapacity);

        if (!volatileData.contains(EnergyCapability.ENERGY_OWNER, 8)) {
            volatileData.putString(EnergyCapability.ENERGY_OWNER, this.getId().toString());
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (tool instanceof ToolStack && this.isOwner(tool.getVolatileData())) {
            int energy_store = tool.getStats().getInt(STToolStats.ENERGY_STORE);
            if (energy_store > 0) {
                list.add(Component.translatable("tooltip.sakuratinker.modifier.stored_energy", new Object[]{tool.getPersistentData().getInt(EnergyCapability.STORED_ENERGY), tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY) + energy_store}).withStyle((style) -> style.withColor(TextColor.fromRgb(this.getColor()))));
            } else {
                list.add(Component.translatable("tooltip.sakuratinker.modifier.stored_energy", new Object[]{tool.getPersistentData().getInt(EnergyCapability.STORED_ENERGY), tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY)}).withStyle((style) -> style.withColor(TextColor.fromRgb(this.getColor()))));
            }
        }

    }

    public int getCapacity(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        return 10000;
    }

    public boolean isOwner(IModDataView volatileData) {
        return this.getId().toString().equals(volatileData.getString(EnergyCapability.ENERGY_OWNER));
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "sakuratinker:energy_bar"; // 自定义能量条的ID
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        // 仅在工具拥有能量时显示条
        return tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY) > 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int currentEnergy = getEnergy(tool);
        int maxEnergy = getMaxEnergy(tool);
        if (maxEnergy > 0) {
            // 精确匹配原版耐久条长度：13像素宽，2像素高
            int width = Math.min(13, 13 * currentEnergy / maxEnergy);
            return new Vec2(width, 1); // 完全匹配原版耐久条尺寸
        }
        return new Vec2(0, 0);
    }

    @Override
    public Vec2 getBarXYPos(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        // 位置下移1像素（原版基础上+1）
        return new Vec2(2, 14 - barsHadBeenShown * 3);
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        // 渐变效果：从蓝(0xFF3CB371)到深蓝(0xFF1E90FF)
        float percent = (float) getEnergy(tool) / getMaxEnergy(tool);
        int r = (int) (0x1E + (0x3C - 0x1E) * percent);
        int g = (int) (0x90 + (0xB3 - 0x90) * percent);
        int b = (int) (0xFF + (0x71 - 0xFF) * percent);
        return FastColor.ARGB32.color(255, r, g, b);
    }

    @Override
    public Vec2 getShadowXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        // 匹配原版阴影尺寸
        return barsHadBeenShown > 0 ? new Vec2(13, 1) : new Vec2(13, 2);
    }

    @Override
    public Vec2 getShadowXYOffset(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        // 匹配原版阴影偏移
        return new Vec2(0, 0);
    }

    // 辅助方法：获取当前能量
    private int getEnergy(IToolStackView tool) {
        return tool.getPersistentData().getInt(EnergyCapability.STORED_ENERGY);
    }

    // 辅助方法：获取最大能量
    private int getMaxEnergy(IToolStackView tool) {
        return tool.getVolatileData().getInt(EnergyCapability.MAX_ENERGY);
    }
}
