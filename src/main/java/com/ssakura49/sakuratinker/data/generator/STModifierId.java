package com.ssakura49.sakuratinker.data.generator;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.Botania.init.BotaniaModifier;
import com.ssakura49.sakuratinker.register.STModifiers;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class STModifierId {
    public static final ModifierId LORD_OF_EARTH = new ModifierId(SakuraTinker.location("lord_of_the_earth"));
    public static final ModifierId CURIO_ATTR = new ModifierId(SakuraTinker.location("curio_attr"));
    public static final ModifierId ATTACK_MODIFIER = new ModifierId(SakuraTinker.location("attack_modifier"));


    public static final ModifierId ENDER_ATTR = new ModifierId(SakuraTinker.location("ender_attr"));
    public static final ModifierId FIRE_ATTR = new ModifierId(SakuraTinker.location("fire_attr"));
    public static final ModifierId LIGHTNING_ATTR = new ModifierId(SakuraTinker.location("lightning_attr"));
    public static final ModifierId HOLY_ATTR = new ModifierId(SakuraTinker.location("holy_attr"));
    public static final ModifierId BLOOD_ATTR = new ModifierId(SakuraTinker.location("blood_attr"));
    public static final ModifierId ICE_ATTR = new ModifierId(SakuraTinker.location("ice_attr"));
    public static final ModifierId EVOCATION_ATTR = new ModifierId(SakuraTinker.location("evocation_attr"));
    public static final ModifierId NATURE_ATTR = new ModifierId(SakuraTinker.location("nature_attr"));
    public static final ModifierId MANA_ATTR = new ModifierId(SakuraTinker.location("mana_attr"));
    public static final ModifierId SPELL_COOLDOWN_ATTR = new ModifierId(SakuraTinker.location("spell_cooldown_attr"));
    public static final ModifierId SPELL_PROTECTION_ATTR = new ModifierId(SakuraTinker.location("spell_protection_attr"));

    public static final ModifierId BOTANY_ID_TECH_MOD = new ModifierId(BotaniaModifier.BotanyIsTech.getId());
}
