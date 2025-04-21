package com.ssakura49.sakuratinker.library.tools;

import com.ssakura49.sakuratinker.SakuraTinker;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class STToolStats {
    public static final FloatToolStat MOVEMENT_SPEED = (FloatToolStat) ToolStats.register(new FloatToolStat(name("movement_speed"), -8871731, 0.0F, 0.0F, 20480.0F));
    public static final FloatToolStat ARMOR = (FloatToolStat)ToolStats.register(new FloatToolStat(name("armor"), -8042548, 0.0F, 0.0F, 300.0F));
    public static final FloatToolStat ARMOR_TOUGHNESS = (FloatToolStat)ToolStats.register(new FloatToolStat(name("armor_toughness"), -8042548, 0.0F, 0.0F, 300.0F));
    public static final FloatToolStat ATTACK_DAMAGE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("damage"), -2661276, 0.0F, 0.0F, 20480.0F));
    public static final FloatToolStat ARROW_DAMAGE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("arrow_damage"), -2661276, 0.0F, 0.0F, 20480.0F));
    public static final FloatToolStat COOLDOWN = (FloatToolStat)ToolStats.register(new FloatToolStat(name("cooldown"), -10887823, 1.0F, 0.0F, (float)Integer.MAX_VALUE));
    public static final FloatToolStat RANGE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("range"), -3135232, 1.0F, 1.0F, (float)Integer.MAX_VALUE));
    public static final FloatToolStat MAX_HEALTH = (FloatToolStat) ToolStats.register(new FloatToolStat(name("health"), -2661276, 0.0F, 0.0F, 20480.0F));
    public static final FloatToolStat ENERGY_STORE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("energy_capacity"), -3135232, 0.0F, 0.0F, (float)Integer.MAX_VALUE));

    public STToolStats() {
    }

    public static void register() {
    }

    private static ToolStatId name(String name) {
        return new ToolStatId(SakuraTinker.MODID, name);
    }
}
