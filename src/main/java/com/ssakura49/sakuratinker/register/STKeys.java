package com.ssakura49.sakuratinker.register;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class STKeys {
    public static final Map<String, KeyMapping> KEY_MAPPINGS = new HashMap<>();

    public static final String CATEGORY = "key.categories.sakuratinker";

    public static final String SUMMON_KEY_ID = "key.sakuratinker.summon";
    public static final String TREASURE_TOGGLE_LOOT_KEY_ID = "key.sakuratinker.treasure_toggle_loot";
    public static final String BLOOD_BURN_KEY_ID = "key.sakuratinker.blood_burn";
    public static final String SELECTED_BULLET_KEY_ID = "key.sakuratinker.selected_bullet";

    public static void registerKeyBindings() {
        registerKey(
                SUMMON_KEY_ID,
                GLFW.GLFW_KEY_G,
                CATEGORY
        );
        registerKey(
                TREASURE_TOGGLE_LOOT_KEY_ID,
                GLFW.GLFW_KEY_L,
                CATEGORY
        );
        registerKey(
                BLOOD_BURN_KEY_ID,
                GLFW.GLFW_KEY_J,
                CATEGORY
        );
        registerKey(
                SELECTED_BULLET_KEY_ID,
                GLFW.GLFW_KEY_K,
                CATEGORY
        );
    }

    private static void registerKey(String id, int defaultKey, String category) {
        KEY_MAPPINGS.put(id, new KeyMapping(
                id,
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                defaultKey,
                category
        ));
    }

    @Nullable
    public static KeyMapping getKeyMappingById(String id) {
        return KEY_MAPPINGS.get(id);
    }

    public static KeyMapping getSummonKey() {
        return KEY_MAPPINGS.get(SUMMON_KEY_ID);
    }

    public static KeyMapping getTreasureToggleLootKey() {
        return KEY_MAPPINGS.get(TREASURE_TOGGLE_LOOT_KEY_ID);
    }

    public static KeyMapping getBloodBurnKey() {
        return KEY_MAPPINGS.get(BLOOD_BURN_KEY_ID);
    }

    public static KeyMapping getSelectedBullet(){
        return KEY_MAPPINGS.get(SELECTED_BULLET_KEY_ID);
    }
}
