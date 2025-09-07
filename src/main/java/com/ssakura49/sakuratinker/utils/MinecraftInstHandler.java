package com.ssakura49.sakuratinker.utils;

import com.ssakura49.sakuratinker.coremod.PathSetter;
import com.ssakura49.sakuratinker.coremod.STClassProcessor;
import com.ssakura49.sakuratinker.coremod.SakuraTinkerCore;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

public class MinecraftInstHandler {
    public static StringBuilder stringBuilder = new StringBuilder();
    public static volatile Instrumentation instrumentation;

    static void init() {
        try {
            InstrumentationHelper.allowAttachSelf();
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static VirtualMachine getCurrent() {
        init();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String processName = runtimeMXBean.getName();
        try {
            return VirtualMachine.attach(processName.substring(0, processName.indexOf("@")));
        } catch (AttachNotSupportedException | IOException e) {
            e.printStackTrace();
        }
        SakuraTinkerCore.stream.println("Catch the current VM.");
        return null;
    }

    public static boolean attach(String args) {
        boolean result;
        out("Agent Attaching...");
        push();
        try {
            File file;
            out("Workingspace mode : " + STClassProcessor.isWorkingspaceMode());
            if (!STClassProcessor.isWorkingspaceMode()) {
                file = File.createTempFile("fe_agent", ".jar");
                out("Created temp agent.jar : %s", file);
                FileOutputStream out = new FileOutputStream(file);
                InputStream in = MinecraftInstHandler.class.getResourceAsStream("/agent.jar");
                out("Created input stream for temp agent jar : %s", in);
                int r;
                while ((r = in.read()) != -1)
                    out.write(r);
                in.close();
                out.close();
                out("Temp data wrote.");
            } else file = new File("fe_agent.jar");
            VirtualMachine vm;
            if ((vm = getCurrent()) != null) {
                out("Found current VM : %s", vm);
                vm.loadAgent(file.getAbsolutePath(), args);
                out("Agent load successfully!");
            }
            result = true;
        } catch (AgentLoadException | com.sun.tools.attach.AgentInitializationException | IOException e) {
            out("Could not load agent, %s", e);
            e.printStackTrace();
            result = false;
        } finally {
            pop();
        }
        return result;
    }

    public static Instrumentation getInstrumentation() {
        if (instrumentation == null) {
            init();
            try {
                PathSetter.appendToClassPathForInstrumentation();
                SakuraTinkerCore.stream.println("Appended to class path for inst:" + PathSetter.JAR);
                System.out.println("Appended to class path for inst:" + PathSetter.JAR);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            SakuraTinkerCore.stream.println("PreAttach");
            System.out.println("PreAttach");
            attach(MinecraftInstHandler.class.getName());
            SakuraTinkerCore.stream.println("PostAttach\n");
            System.out.println("PostAttach\n");
            SakuraTinkerCore.stream.flush();
        }
        return instrumentation;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(getInstrumentation());
    }

    public static void agent(String args, Instrumentation instrumentation) {
        MinecraftInstHandler.instrumentation = instrumentation;
        areaOut("Minecraft Instrumentation", () -> {
            out("LoadingThread:%s", Thread.currentThread());
            out("ClassLoader:%s", MinecraftInstHandler.class.getClassLoader());
        });
    }

    public static synchronized void out(Object o) {
        System.out.println(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]" + o);

    }

    public static synchronized void out(String text, Object... o) {
        System.out.printf(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]" + text, o);
        System.out.print("\n");
    }

    public static synchronized void out(Object... o) {
        System.out.print(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]");
        for (Object o1 : o)
            System.out.print(o1 + ", ");
        System.out.print("\n");
    }

    public static synchronized void outCollection(Collection<?> o) {
        System.out.println(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]" + Arrays.toString(o.toArray()));
    }

    public static synchronized void push() {
        stringBuilder.append(" ");
    }

    public static synchronized void pop() {
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }

    public static synchronized void push2() {
        push();
        push();
    }

    public static synchronized void pop2() {
        pop();
        pop();
    }

    public static synchronized void push3() {
        push2();
        push();
    }

    public static synchronized void pop3() {
        pop2();
        pop();
    }

    public static void areaOut(String title, Runnable... runnables) {
        String line = "--------------------------------";
        int totalWidth = line.length() + 1;
        String centeredText = String.format("%1$" + (totalWidth - title.length()) / 2 + "s%2$" + totalWidth + "s", "", title);
        try {
            out(line + line);
            out(centeredText);
            out("Info:");
            push2();
            for (Runnable runnable : runnables)
                runnable.run();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            pop2();
            out(line + line);
        }
    }
}
