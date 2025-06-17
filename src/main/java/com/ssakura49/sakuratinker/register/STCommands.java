package com.ssakura49.sakuratinker.register;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.entity.terraprisma.TerraPrismEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class STCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("summon_terra_prism")
                        .requires(source -> source.hasPermission(3))
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal("Terra Prism command executed."), true);
                            return 1;
                        })
        );
    }

    private static int summonPrism(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        Vec3 spawnPos = player.position().add(0, 1.5, 0); // 生成在玩家头顶

        TerraPrismEntity prism = new TerraPrismEntity(STEntities.TERRA_PRISMA.get(), level);
        prism.moveTo(spawnPos);
        prism.setOwner(player);
        level.addFreshEntity(prism);

        context.getSource().sendSuccess(() -> Component.literal("已召唤泰拉棱镜"), true);
        return 1;
    }
}
