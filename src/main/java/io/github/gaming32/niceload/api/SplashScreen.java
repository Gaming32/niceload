package io.github.gaming32.niceload.api;

import net.minecraft.client.gui.screen.SplashOverlay;

public interface SplashScreen {
    SplashOverlay getOverlay();

    LoadTask addTask(LoadTask task);

    LoadTask getTask(String name);

    default LoadTask beginTask(String name, int maxProgress) {
        return addTask(new LoadTask(name, maxProgress));
    }

    default LoadTask beginTask(String name) {
        return beginTask(name, 1);
    }
}
