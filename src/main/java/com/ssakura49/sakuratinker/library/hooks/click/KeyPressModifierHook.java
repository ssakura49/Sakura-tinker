package com.ssakura49.sakuratinker.library.hooks.click;

import com.ssakura49.sakuratinker.register.STKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface KeyPressModifierHook {
    /**
     * 玩家按下按键时触发
     *
     * @param tool       当前饰品或工具
     * @param modifier   当前 Modifier
     * @param player     当前玩家
     * @param key        按下的按键标识（可以是自定义的 String）
     */
    default void onKeyPress(IToolStackView tool, ModifierEntry modifier, Player player, String key){};

    @Nullable
    default String getKeyId(IToolStackView tool, ModifierEntry modifier) {
        return null;
    }

    default String getKeyDisplay(IToolStackView tool, ModifierEntry modifier) {
        String id = getKeyId(tool, modifier);
        if (id == null) return "???";
        KeyMapping mapping = STKeys.getKeyMappingById(id);
        return mapping != null ? mapping.getTranslatedKeyMessage().getString() : "???";
    }

    record AllMerger(Collection<KeyPressModifierHook> modules) implements KeyPressModifierHook {
        @Override
        public void onKeyPress(IToolStackView tool, ModifierEntry modifier, Player player, String key) {
            for (KeyPressModifierHook module : modules) {
                module.onKeyPress(tool, modifier, player, key);
            }
        }
    }
}
