package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;

public class DisslovingModifier extends BaseModifier {
    public DisslovingModifier() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private final Random random = new Random();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @SubscribeEvent
    public void onXpDrop(LivingExperienceDropEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide()) {
            Player player = event.getAttackingPlayer();
            if (player != null) {
                ItemStack heldItem = player.getMainHandItem();
                IToolStackView tool = ToolStack.from(heldItem);
                if (ToolUtil.checkTool(heldItem)) {
                    if (tool.getModifierLevel(this) > 0) {
                        float r = random.nextFloat();
                        if (r <= 0.75) {
                            event.setDroppedExperience(0);
                        } else if (r > 0.95) {
                            event.setDroppedExperience(event.getDroppedExperience() * (random.nextInt(3) + 2));
                        }
                    }
                }
            }
        }
    }
}
