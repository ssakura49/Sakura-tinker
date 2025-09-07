package com.ssakura49.sakuratinker.coremod.forge;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public interface IClassProcessor {
    boolean processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type classType);
}
