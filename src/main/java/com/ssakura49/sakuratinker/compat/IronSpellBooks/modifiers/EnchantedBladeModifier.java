package com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.network.handler.PacketHandler;
import com.ssakura49.sakuratinker.network.packet.SyncManaPacket;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnchantedBladeModifier extends BaseModifier {
    @Override
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (target.level().isClientSide() || !(attacker instanceof ServerPlayer serverPlayer)) {
            return actualDamage;
        }
        MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
        int serverMana = (int) magicData.getMana();
        boolean noMana = serverMana <= 0;
        if (noMana) {
            return actualDamage;
        }
        float maxUsableMana = 80 + (20 * modifier.getLevel());
        float manaToUse = Math.min(serverMana, maxUsableMana);
        float bonusDamage = (float) Math.floor(manaToUse / 5f);
        float consumedMana = bonusDamage * 5;
        if (serverMana >= consumedMana) {
            int newMana = (int) (serverMana - consumedMana);
            magicData.setMana(newMana);
            PacketHandler.sendToPlayer(new SyncManaPacket(newMana), serverPlayer);
            actualDamage = baseDamage + bonusDamage;
        }
        return actualDamage;
    }
}
