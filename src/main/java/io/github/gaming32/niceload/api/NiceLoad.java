package io.github.gaming32.niceload.api;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReloader;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class NiceLoad {
    private static final Map<String, LoadTask> TO_ADD_QUEUE = new LinkedHashMap<>();
    private static final Set<String> REGISTERED_RELOADERS = new HashSet<>();

    private NiceLoad() {
        throw new AssertionError();
    }

    public static SplashScreen getSplashScreen() {
        final Overlay overlay = MinecraftClient.getInstance().getOverlay();
        if (!(overlay instanceof SplashOverlay)) {
            return null;
        }
        final SplashScreen splash = (SplashScreen)overlay;
        if (!TO_ADD_QUEUE.isEmpty()) {
            TO_ADD_QUEUE.values().forEach(splash::addTask);
            TO_ADD_QUEUE.clear();
        }
        return splash;
    }

    public static LoadTask addTask(LoadTask task) {
        final SplashScreen splash = getSplashScreen();
        if (splash != null) {
            splash.addTask(task);
        } else {
            TO_ADD_QUEUE.put(task.getName(), task);
        }
        return task;
    }

    public static LoadTask getTask(String name) {
        final SplashScreen splash = getSplashScreen();
        if (splash != null) {
            return splash.getTask(name);
        } else {
            return TO_ADD_QUEUE.get(name);
        }
    }

    public static LoadTask beginTask(String name, int maxProgress) {
        return addTask(new LoadTask(name, maxProgress));
    }

    public static LoadTask beginTask(String name) {
        return beginTask(name, 1);
    }

    public static void endTask(String name) {
        final LoadTask task = getTask(name);
        if (task != null) task.finish();
    }

    public static void addTaskProgress(String name, int progress) {
        final LoadTask task = getTask(name);
        if (task != null) task.addProgress(progress);
    }

    public static void addTaskProgress(String name) {
        addTaskProgress(name, 1);
    }

    public static void setTaskProgress(String name, int progress) {
        final LoadTask task = getTask(name);
        if (task != null) task.setProgress(progress);
    }

    public static void setTaskDescription(String name, Object description) {
        final LoadTask task = getTask(name);
        if (task != null) task.setDescription(description);
    }

    public static void registerReloader(String name) {
        REGISTERED_RELOADERS.add(name);
    }

    public static void registerReloader(ResourceReloader reloader) {
        REGISTERED_RELOADERS.add(getReloaderName(reloader));
    }

    public static boolean isReloaderRegistered(String name) {
        return REGISTERED_RELOADERS.contains(name);
    }

    public static boolean isReloaderRegistered(ResourceReloader reloader) {
        return REGISTERED_RELOADERS.contains(getReloaderName(reloader));
    }

    public static String getReloaderName(ResourceReloader reloader) {
        String name = reloader.getName();
        if (
            reloader instanceof IdentifiableResourceReloadListener &&
            name.equals(reloader.getClass().getSimpleName())
        ) {
            return ((IdentifiableResourceReloadListener)reloader).getFabricId().toString();
        }
        if (name.equals("") && reloader.getClass().getDeclaringClass() != null) {
            name = reloader.getClass().getDeclaringClass().getSimpleName();
        }
        return name;
    }


}
