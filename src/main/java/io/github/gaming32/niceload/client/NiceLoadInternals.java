package io.github.gaming32.niceload.client;

import net.minecraft.client.MinecraftClient;

public class NiceLoadInternals {
    private static long lastRenderAttempt = System.currentTimeMillis();

    private NiceLoadInternals() {
        throw new AssertionError();
    }

    public static void attemptRender() {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastRenderAttempt < 30) return;
        if (!MinecraftClient.getInstance().isOnThread()) return;
        lastRenderAttempt = currentTime;
        MinecraftClient.getInstance().render(false);
    }
}
