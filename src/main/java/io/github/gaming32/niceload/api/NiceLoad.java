package io.github.gaming32.niceload.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NiceLoad {
    private static final Map<String, LoadTask> toAddQueue = new LinkedHashMap<>();
    private static long lastRenderAttempt = System.currentTimeMillis();

    private NiceLoad() {
        throw new AssertionError();
    }

    public static SplashScreen getSplashScreen() {
        final Overlay overlay = MinecraftClient.getInstance().getOverlay();
        if (!(overlay instanceof SplashOverlay)) {
            return null;
        }
        final SplashScreen splash = (SplashScreen)overlay;
        if (!toAddQueue.isEmpty()) {
            toAddQueue.values().forEach(splash::addTask);
            toAddQueue.clear();
        }
        return splash;
    }

    public static LoadTask addTask(LoadTask task) {
        final SplashScreen splash = getSplashScreen();
        if (splash != null) {
            splash.addTask(task);
        } else {
            toAddQueue.put(task.getName(), task);
        }
        return task;
    }

    public static LoadTask getTask(String name) {
        final SplashScreen splash = getSplashScreen();
        if (splash != null) {
            return splash.getTask(name);
        } else {
            return toAddQueue.get(name);
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

    static void attemptRender() {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastRenderAttempt < 30) return;
        if (!MinecraftClient.getInstance().isOnThread()) return;
        lastRenderAttempt = currentTime;
        MinecraftClient.getInstance().render(false);
    }
}
