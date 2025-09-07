package com.ssakura49.sakuratinker.coremod;

import com.mojang.logging.LogUtils;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SakuraTinkerMixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean pass(String targetClassName) {
        return targetClassName.startsWith("com.llamalad7.mixinextras") || targetClassName.startsWith("sun") || targetClassName.startsWith("org.openjdk") || targetClassName.startsWith("com/llamalad7/mixinextras") || targetClassName.startsWith("it.unimi") || targetClassName.startsWith("jdk") || targetClassName.startsWith("java") || targetClassName.startsWith("com.mojang") || targetClassName.startsWith("org.objectweb") || targetClassName.startsWith("org.spongepowered") || targetClassName.startsWith("cpw") || targetClassName.startsWith("com.google") || targetClassName.startsWith("com.sun") || targetClassName.startsWith("oshi") || targetClassName.equals("com.mega.uom.util.entity.EntityASMUtil");
    }
    public static void log(String str, Object... args) {
        LOGGER.debug(String.format(str, args));
    }


    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

}
