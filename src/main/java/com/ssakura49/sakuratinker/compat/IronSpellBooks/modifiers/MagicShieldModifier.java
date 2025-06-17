package com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import com.ssakura49.sakuratinker.network.handler.PacketHandler;
import com.ssakura49.sakuratinker.network.packet.SyncManaPacket;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MagicShieldModifier extends BaseModifier {
        private static final float BLOCK_PERCENT_PER_LEVEL = 0.05f;
        private static final float MANA_PER_DAMAGE = 10f;
        private static final int MAX_LEVEL = 12;
        @Override
        public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
            LivingEntity entity = context.getEntity();
            if (entity.level().isClientSide() || !(entity instanceof ServerPlayer serverPlayer)) {
                return amount;
            }
            MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
            int currentMana = (int) magicData.getMana();
            boolean noMana = currentMana <= 0;
            if (noMana) {
                return amount;
            }
            int effectiveLevel = Math.min(ToolUtil.getSingleModifierArmorAllLevel(serverPlayer, this), MAX_LEVEL);
            float damageToBlock = amount * (BLOCK_PERCENT_PER_LEVEL * effectiveLevel);
            float manaCost = damageToBlock * MANA_PER_DAMAGE;
            float actualBlock = Math.min(damageToBlock, currentMana / MANA_PER_DAMAGE);

            ChangeManaEvent event = new ChangeManaEvent(serverPlayer, magicData, currentMana, currentMana - manaCost);
            if (actualBlock > 0) {
                magicData.setMana(event.getNewMana());
                PacketHandler.sendToPlayer(new SyncManaPacket((int) (currentMana - manaCost)), serverPlayer);
                amount -= actualBlock;
            }
            return amount;
        }
}