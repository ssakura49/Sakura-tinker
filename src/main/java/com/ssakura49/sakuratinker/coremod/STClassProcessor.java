package com.ssakura49.sakuratinker.coremod;

import com.ssakura49.sakuratinker.coremod.forge.IClassProcessor;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.*;

public class STClassProcessor implements IClassProcessor {
    private static final Map<ILaunchPluginService.Phase, Set<String>> toTransformClasses = new HashMap<>();
    private static final String NAMESPACED_WRAPPER = "net.minecraftforge.registries.NamespacedWrapper".replace('.', '/');
    private static final String MCMAPPING = "com.ssakura49.sakuratinker.util.MCMapping".replace('.', '/');
    private static volatile int isWorkingspace;
    static {
        toTransformClasses.put(ILaunchPluginService.Phase.BEFORE, Collections.synchronizedSet(new HashSet<>()));
        toTransformClasses.put(ILaunchPluginService.Phase.AFTER, Collections.synchronizedSet(new HashSet<>()));
        toTransformClasses.get(ILaunchPluginService.Phase.BEFORE).add(NAMESPACED_WRAPPER);
        toTransformClasses.get(ILaunchPluginService.Phase.BEFORE).add(MCMAPPING);
    }
    public static boolean isWorkingspaceMode() {
        if (isWorkingspace == 0) {
            File file = new File("fe_agent.jar");
            if (file.exists()) {
                isWorkingspace = 1;
            } else isWorkingspace = 2;


        }
        return isWorkingspace == 1;
    }
    @Override
    public boolean processClass(ILaunchPluginService.Phase phase, ClassNode classNode, Type classType) {
        String name = classNode.name;
        Set<String> classesToTrans = toTransformClasses.get(phase);
        if (!classesToTrans.isEmpty() && classesToTrans.contains(name)) {
            SakuraTinkerMixinPlugin.LOGGER.debug("STMod:Detected class " + name);
            if (name.equals(NAMESPACED_WRAPPER)) {
                classNode.methods.forEach(methodNode -> {
                    methodNode.instructions.forEach(insnNode -> {
                        int opcode = insnNode.getOpcode();
                        if (opcode == Opcodes.PUTFIELD && insnNode instanceof FieldInsnNode fieldInsnNode) {
                            //FantasyEndingMixinPlugin.log("%s_%s(%s, %s, %s)", insnNode.getClass().getSimpleName(), opcode, fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
                            if (fieldInsnNode.name.equals("frozen"))
                                methodNode.instructions.remove(fieldInsnNode);
                        }
                    });
                });
            } else if (name.equals(MCMAPPING)) {
                classNode.methods.forEach(methodNode -> {
                    if (methodNode.name.equals("get")) {
                        methodNode.instructions.clear();
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insnList.add(new FieldInsnNode(
                                Opcodes.GETFIELD,
                                "com/ssakura49/sakuratinker/utils/MCMapping",
                                isWorkingspaceMode() ? "workspace" : "normal",
                                "Ljava/lang/String;"
                        ));
                        insnList.add(new InsnNode(Opcodes.ARETURN));
                        methodNode.instructions.add(insnList);
                    }

                });
            }
            classesToTrans.remove(name);
            return true;
        }
        return false;
    }
}
